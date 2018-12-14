package StockSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChartPanel extends JPanel {
	
	private int[] data;
	private int maxValue;
	private int maxDay;
	private int tempDay;
	private Point pt1, pt2;
	private Calendar cal;
	private Stock stock;
	private JLabel[] chartVal;
	private JLabel[] chartDay;
	protected int setDay; //그래프 기간 단위
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
	
	public ChartPanel(Stock stock) {
		
		this.setPreferredSize(new Dimension(580, 270));
		this.setBackground(Color.lightGray);
		this.setLayout(null);
		
		this.stock = stock;
		
		pt1 = new Point();
		pt2 = new Point();
		
		data = stock.getPrice(); //해당 주식의 가격 값
		
		setDay = 1; //기본값은 하루
		
		tempDay = getDayVal(-(setDay * 10)); //가로축 눈금이 11개 이므로 10칸 전 날짜로 이동
		maxDay = tempDay;
		
		for(int i=0; i<((setDay * 10) + 1); i++) { //지정한 날짜만큼 전부터 오늘까지의 최대값 탐색. + 1은 오늘
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(data[tempDay] > 0) { //0이 저장된 경우는 데이터가 없는 경우
				if(data[maxDay] < data[tempDay]) {
					maxDay = tempDay;
				} //if
			} //if
		} //for
		
		maxValue = data[maxDay];

		maxValue = maxValue * 105 / 100; //최대값의 1.05배의 값에서 백의자리에서 반올린한 값을 그래프의 최대값으로 설정
		maxValue += 500; //백의 자리에서 반올림
		maxValue = maxValue - (maxValue % 500);
		
		chartVal = new JLabel[6]; //세로 눈금자에 들어갈 값 설정
		for(int i=0; i<6; i++) {
			int value = maxValue - (maxValue / 5) * i; //위에서 얻은 최대값에 비례해서 설정
			chartVal[i] = new JLabel(String.format("%,d", value));
			chartVal[i].setBounds(530, 2 + (i * 48), 50, 10);
			chartVal[i].setFont(new Font("Verdana", Font.PLAIN, 10));
			this.add(chartVal[i]);
		} //for
		
		cal = Calendar.getInstance();
		cal.setTime(MainPanel.currentDate); //프로그램 상 현재 날짜를 가져옴
		cal.add(Calendar.DAY_OF_YEAR, -(setDay * 10)); //10칸 전에 들어갈 날짜를 계산
		
		chartDay = new JLabel[11]; //가로 눈금자에 들어갈 날짜 설정
		for(int i=0; i<11; i++) {
			chartDay[i] = new JLabel(sdf.format(cal.getTime()));
			chartDay[i].setBounds(-12 + (i * 50), 260, 50, 10);
			chartDay[i].setHorizontalAlignment(SwingConstants.CENTER);
			chartDay[i].setFont(new Font("Verdana", Font.PLAIN, 8));
			this.add(chartDay[i]);
			
			cal.add(Calendar.DAY_OF_YEAR, setDay); //지정한 날짜만큼 갱신
		} //for
		
		
	} //ChartPanel()
	
	public void paintComponent(Graphics page) {
		
		super.paintComponent(page);
		
		Graphics2D page2 = (Graphics2D)page;
		page2.setColor(Color.lightGray);
		
		for(int i=0; i<11; i++) { //가로 눈금 선 긋기
			page.drawLine(10 + (i * 50), 3, 10 + (i * 50), 251);
		} //for
		
		for(int i=0; i<6; i++) { //세로 눈금 선 긋기
			page.drawLine(10, 3 + (i * 48), 518, 3 + (i * 48));
		} //for
		
		page2.setColor(Color.black);
		page.drawRect(10, 3, 500, 240); //틀	그리기
		page2.setColor(Color.blue);
		
		for(int i=0; i<(setDay * 10); i++) { //그래프 그리기
			pt1.x = 10 + (int)(i * (50.0 / setDay));
			pt2.x = 10 + (int)((i + 1) * (50.0 / setDay));
			
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(data[tempDay] > 0) { //데이터 없는 날짜는 그리지 않음
				pt1.y = 243 - (data[tempDay] * 240 / maxValue);
				tempDay = getDayVal(-(setDay * 10) + i + 1);
				pt2.y = 243 - (data[tempDay] * 240 / maxValue);
				
				page.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
			} //if
		} //for
		
	} //paintComponent()
	
	public int getDayVal(int i) { //프로그램 상 현재로부터 지정한 날짜만큼 이동한 날짜의 id 반환
		int day = MainPanel.dateId;
		
		day = (day + i + 1095) % 1095;
		
		return day;
	} //getDayVal()
	
	public void updateChart() { //그래프 갱신하여 그리기
		
		data = stock.getPrice();
		
		tempDay = getDayVal(-(setDay * 10)); //갱신된 날짜에서 그래프에 출력할 첫번째 날짜의 id 계산
		maxValue = data[tempDay];
		
		for(int i=0; i<(setDay * 10) + 1; i++) { //지정한 날짜 부터 오늘까지의 최대값 탐색
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(maxValue < data[tempDay]) maxValue = data[tempDay];
		} //for
		
		maxValue = maxValue * 105 / 100; //최대값에서 105%가 그래프 눈금의 최대값
		maxValue += 500; //백의 자리에서 반올림
		maxValue = maxValue - (maxValue % 500);
		
		for(int i=0; i<6; i++) { //세로축 값 갱신
			int value = maxValue - (maxValue / 5) * i;
			chartVal[i].setText(String.format("%,d", value));
		} //for
		
		cal.setTime(MainPanel.currentDate);
		cal.add(Calendar.DAY_OF_YEAR, -(setDay *10));
		
		for(int i=0; i<11; i++) { //가로축 값 갱신
			chartDay[i].setText(sdf.format(cal.getTime()));		
			cal.add(Calendar.DAY_OF_YEAR, setDay);
		} //for
		
		this.repaint();
		
	} //updateChart()
	
} //ChartPanel class

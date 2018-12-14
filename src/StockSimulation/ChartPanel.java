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
	protected int setDay; //�׷��� �Ⱓ ����
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
	
	public ChartPanel(Stock stock) {
		
		this.setPreferredSize(new Dimension(580, 270));
		this.setBackground(Color.lightGray);
		this.setLayout(null);
		
		this.stock = stock;
		
		pt1 = new Point();
		pt2 = new Point();
		
		data = stock.getPrice(); //�ش� �ֽ��� ���� ��
		
		setDay = 1; //�⺻���� �Ϸ�
		
		tempDay = getDayVal(-(setDay * 10)); //������ ������ 11�� �̹Ƿ� 10ĭ �� ��¥�� �̵�
		maxDay = tempDay;
		
		for(int i=0; i<((setDay * 10) + 1); i++) { //������ ��¥��ŭ ������ ���ñ����� �ִ밪 Ž��. + 1�� ����
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(data[tempDay] > 0) { //0�� ����� ���� �����Ͱ� ���� ���
				if(data[maxDay] < data[tempDay]) {
					maxDay = tempDay;
				} //if
			} //if
		} //for
		
		maxValue = data[maxDay];

		maxValue = maxValue * 105 / 100; //�ִ밪�� 1.05���� ������ �����ڸ����� �ݿø��� ���� �׷����� �ִ밪���� ����
		maxValue += 500; //���� �ڸ����� �ݿø�
		maxValue = maxValue - (maxValue % 500);
		
		chartVal = new JLabel[6]; //���� �����ڿ� �� �� ����
		for(int i=0; i<6; i++) {
			int value = maxValue - (maxValue / 5) * i; //������ ���� �ִ밪�� ����ؼ� ����
			chartVal[i] = new JLabel(String.format("%,d", value));
			chartVal[i].setBounds(530, 2 + (i * 48), 50, 10);
			chartVal[i].setFont(new Font("Verdana", Font.PLAIN, 10));
			this.add(chartVal[i]);
		} //for
		
		cal = Calendar.getInstance();
		cal.setTime(MainPanel.currentDate); //���α׷� �� ���� ��¥�� ������
		cal.add(Calendar.DAY_OF_YEAR, -(setDay * 10)); //10ĭ ���� �� ��¥�� ���
		
		chartDay = new JLabel[11]; //���� �����ڿ� �� ��¥ ����
		for(int i=0; i<11; i++) {
			chartDay[i] = new JLabel(sdf.format(cal.getTime()));
			chartDay[i].setBounds(-12 + (i * 50), 260, 50, 10);
			chartDay[i].setHorizontalAlignment(SwingConstants.CENTER);
			chartDay[i].setFont(new Font("Verdana", Font.PLAIN, 8));
			this.add(chartDay[i]);
			
			cal.add(Calendar.DAY_OF_YEAR, setDay); //������ ��¥��ŭ ����
		} //for
		
		
	} //ChartPanel()
	
	public void paintComponent(Graphics page) {
		
		super.paintComponent(page);
		
		Graphics2D page2 = (Graphics2D)page;
		page2.setColor(Color.lightGray);
		
		for(int i=0; i<11; i++) { //���� ���� �� �߱�
			page.drawLine(10 + (i * 50), 3, 10 + (i * 50), 251);
		} //for
		
		for(int i=0; i<6; i++) { //���� ���� �� �߱�
			page.drawLine(10, 3 + (i * 48), 518, 3 + (i * 48));
		} //for
		
		page2.setColor(Color.black);
		page.drawRect(10, 3, 500, 240); //Ʋ	�׸���
		page2.setColor(Color.blue);
		
		for(int i=0; i<(setDay * 10); i++) { //�׷��� �׸���
			pt1.x = 10 + (int)(i * (50.0 / setDay));
			pt2.x = 10 + (int)((i + 1) * (50.0 / setDay));
			
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(data[tempDay] > 0) { //������ ���� ��¥�� �׸��� ����
				pt1.y = 243 - (data[tempDay] * 240 / maxValue);
				tempDay = getDayVal(-(setDay * 10) + i + 1);
				pt2.y = 243 - (data[tempDay] * 240 / maxValue);
				
				page.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
			} //if
		} //for
		
	} //paintComponent()
	
	public int getDayVal(int i) { //���α׷� �� ����κ��� ������ ��¥��ŭ �̵��� ��¥�� id ��ȯ
		int day = MainPanel.dateId;
		
		day = (day + i + 1095) % 1095;
		
		return day;
	} //getDayVal()
	
	public void updateChart() { //�׷��� �����Ͽ� �׸���
		
		data = stock.getPrice();
		
		tempDay = getDayVal(-(setDay * 10)); //���ŵ� ��¥���� �׷����� ����� ù��° ��¥�� id ���
		maxValue = data[tempDay];
		
		for(int i=0; i<(setDay * 10) + 1; i++) { //������ ��¥ ���� ���ñ����� �ִ밪 Ž��
			tempDay = getDayVal(-(setDay * 10) + i);
			
			if(maxValue < data[tempDay]) maxValue = data[tempDay];
		} //for
		
		maxValue = maxValue * 105 / 100; //�ִ밪���� 105%�� �׷��� ������ �ִ밪
		maxValue += 500; //���� �ڸ����� �ݿø�
		maxValue = maxValue - (maxValue % 500);
		
		for(int i=0; i<6; i++) { //������ �� ����
			int value = maxValue - (maxValue / 5) * i;
			chartVal[i].setText(String.format("%,d", value));
		} //for
		
		cal.setTime(MainPanel.currentDate);
		cal.add(Calendar.DAY_OF_YEAR, -(setDay *10));
		
		for(int i=0; i<11; i++) { //������ �� ����
			chartDay[i].setText(sdf.format(cal.getTime()));		
			cal.add(Calendar.DAY_OF_YEAR, setDay);
		} //for
		
		this.repaint();
		
	} //updateChart()
	
} //ChartPanel class

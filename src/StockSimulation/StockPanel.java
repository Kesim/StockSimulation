package StockSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class StockPanel extends JPanel {
	
	private RealTimeStock realTimeStock;
	private JPanel infoPanel;
	private JPanel myInfoPanel;
	private JLabel[][] infolbl;
	private JLabel[] myInfolbl;
	private JLabel sumlbl;
	private MyStock myStock;
	private Border border;
	private ImageIcon backgroundImg;
	protected Stock stock;
	protected ChartPanel chartPanel;
	protected int stockId;
	protected JLabel sumVallbl;
	protected JButton backBt;
	protected JButton buyBt, sellBt;
	protected JTextField buyfd, sellfd;
	protected JComboBox<String> dayBox;
	
	private MainListener stockL;

	protected final String setDay[] = {"1일", "일주일", "한달", "3달"}; //주식 기간 설정 값
	
	public StockPanel(RealTimeStock realTimeStock, int stockId, MyStock myStock, MainListener mainL) {	
		
		this.setPreferredSize(new Dimension(600, 480));
		this.setLayout(null);
		stockL = mainL;
		mainL.stockPanel = this;

		backgroundImg = new ImageIcon("images/background2.png");
		
		this.realTimeStock = realTimeStock;
		this.stockId = stockId;
		this.stock = realTimeStock.stocks[stockId];
		this.myStock = myStock;
		
		infoPanel = new JPanel();
		infoPanel.setBounds(40, 50, 520, 80);
		infoPanel.setLayout(null);
		this.add(infoPanel);
		
		myInfoPanel = new JPanel();
		myInfoPanel.setBounds(40, 130, 520, 65);
		myInfoPanel.setBackground(Color.white);
		myInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		myInfoPanel.setLayout(null);
		this.add(myInfoPanel);
		
		//주식 정보 출력
		infolbl = new JLabel[2][6];
		infolbl[0][0] = new JLabel("종목명");
		infolbl[0][1] = new JLabel(stock.getStockName() + " ");
		infolbl[0][2] = new JLabel("발행 주식수");
		infolbl[0][3] = new JLabel(realTimeStock.getNumShare(stockId) + " ");
		infolbl[0][4] = new JLabel("시가 총액");
		infolbl[0][5] = new JLabel(getTotalVal(stockId) +  " ");
		infolbl[1][0] = new JLabel("현재가");
		infolbl[1][1] = new JLabel(realTimeStock.getPrice(stockId) + " ");
		infolbl[1][2] = new JLabel("전일비");
		infolbl[1][3] = new JLabel(realTimeStock.getFlucVal(stockId));
		infolbl[1][4] = new JLabel("등락률");
		infolbl[1][5] = new JLabel(realTimeStock.getFlucPer(stockId));
		
		infolbl[0][0].setBounds(0, 0, 70, 40);
		infolbl[0][1].setBounds(70, 0, 100, 40);
		infolbl[0][2].setBounds(170, 0, 75, 40);
		infolbl[0][3].setBounds(245, 0, 105, 40);
		infolbl[0][4].setBounds(350, 0, 70, 40);
		infolbl[0][5].setBounds(420, 0, 100, 40);
		infolbl[1][0].setBounds(0, 40, 70, 40);
		infolbl[1][1].setBounds(70, 40, 100, 40);
		infolbl[1][2].setBounds(170, 40, 75, 40);
		infolbl[1][3].setBounds(245, 40, 105, 40);
		infolbl[1][4].setBounds(350, 40, 70, 40);
		infolbl[1][5].setBounds(420, 40, 100, 40);
		
		border = BorderFactory.createLineBorder(Color.darkGray);
		
		for(int i=0; i<2; i++) { //각 라벨의 글자 정렬과 배경색 설정
			for(int j=0; j<6; j++) {
				infoPanel.add(infolbl[i][j]);
				infolbl[i][j].setOpaque(true); //배경색 불투명
				
				if(j%2 == 0) {
					infolbl[i][j].setHorizontalAlignment(SwingConstants.CENTER);

				} else {
					infolbl[i][j].setHorizontalAlignment(SwingConstants.RIGHT);
					infolbl[i][j].setBackground(Color.white);
				} //if else
				
				infolbl[i][j].setBorder(border);
				infolbl[i][j].setFont(new Font("굴림", Font.PLAIN, 12));
			} //for
		} //for
			
		//사용자 정보 출력
		myInfolbl = new JLabel[6];
		myInfolbl[0] = new JLabel("보유 금액");
		myInfolbl[1] = new JLabel(myStock.getMoneyStr() + " 원");
		myInfolbl[2] = new JLabel("보유량");
		myInfolbl[3] = new JLabel(myStock.getHoldStr(stockId) + " ");
		myInfolbl[4] = new JLabel("구매 대비");
		myInfolbl[5] = new JLabel("");
		
		myInfolbl[0].setBounds(0, 0, 70, 35);
		myInfolbl[1].setBounds(70, 0, 100, 35);
		myInfolbl[2].setBounds(170, 0, 75, 35);
		myInfolbl[3].setBounds(245, 0, 105, 35);
		myInfolbl[4].setBounds(350, 0, 70, 35);
		myInfolbl[5].setBounds(420, 0, 100, 35);
		
		for(int i=0; i<6; i++) { //각 라벨의 글자 정렬과 배경색 설정
			myInfoPanel.add(myInfolbl[i]);
			myInfolbl[i].setOpaque(true); //배경색 불투명
			
			if(i%2 == 0) {
				myInfolbl[i].setHorizontalAlignment(SwingConstants.CENTER);

			} else {
				myInfolbl[i].setHorizontalAlignment(SwingConstants.RIGHT);
				myInfolbl[i].setBackground(Color.white);
			} //if else
			
			myInfolbl[i].setBorder(border);
			myInfolbl[i].setFont(new Font("굴림", Font.PLAIN, 12));
		} //for
			
		buyfd = new JTextField();
		buyfd.setBounds(5, 40, 70, 20);
		buyfd.addActionListener(stockL.sBtnL);
		buyfd.addKeyListener(stockL.textL);
		myInfoPanel.add(buyfd);
		
		buyBt = new JButton("구매");
		buyBt.setBounds(80, 37, 60, 26);
		buyBt.setFont(new Font("굴림", Font.PLAIN, 9));
		buyBt.addActionListener(stockL.sBtnL);
		myInfoPanel.add(buyBt);
		
		sellfd = new JTextField();
		sellfd.setBounds(150, 40, 70, 20);
		sellfd.addActionListener(stockL.sBtnL);
		sellfd.addKeyListener(stockL.textL);
		myInfoPanel.add(sellfd);
		
		sellBt = new JButton("판매");
		sellBt.setBounds(225, 37, 60, 26);
		sellBt.setFont(new Font("굴림", Font.PLAIN, 9));
		sellBt.addActionListener(stockL.sBtnL);
		myInfoPanel.add(sellBt);
		
		sumlbl = new JLabel("합계 :");
		sumlbl.setBounds(300, 40, 40, 20);
		myInfoPanel.add(sumlbl);
		
		sumVallbl = new JLabel("원");
		sumVallbl.setBounds(350, 40, 100, 20);
		sumVallbl.setHorizontalAlignment(SwingConstants.RIGHT);
		myInfoPanel.add(sumVallbl);
		
		chartPanel = new ChartPanel(stock);
		chartPanel.setBounds(10, 200, 580, 270);
		chartPanel.setBackground(Color.white);
		chartPanel.setLayout(null);
		chartPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(chartPanel);
		
		backBt = new JButton("뒤로");
		backBt.setBounds(520, 10, 60, 20);
		backBt.setFont(new Font("굴림", Font.PLAIN, 11));
		backBt.addActionListener(stockL.sBtnL);
		this.add(backBt);
		
		dayBox = new JComboBox<String>(setDay);
		dayBox.setBounds(20, 10, 75, 20);
		dayBox.setFont(new Font("돋움", Font.PLAIN, 11));
		dayBox.addActionListener(stockL.sBtnL);
		dayBox.setFocusable(false);
		this.add(dayBox);
		
		updateVal();
		updateStartVal();
	
	} //StockPanel()
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		page.drawImage(backgroundImg.getImage(), 0, 0, this);
	} //paintComponent()
	
	public void updateVal() { //주식 가격과 변동 출력 갱신
			
		infolbl[0][5].setText(getTotalVal(stockId) +  " ");
		infolbl[1][1].setText(realTimeStock.getPrice(stockId) + " ");
		infolbl[1][3].setText(realTimeStock.getFlucVal(stockId));
		infolbl[1][5].setText(realTimeStock.getFlucPer(stockId));
		
		if(realTimeStock.getFluc(stockId) >= 0) { //변동에 따라 글자색 갱신
			infolbl[1][3].setForeground(Color.red);
			infolbl[1][5].setForeground(Color.red);
		} else {
			infolbl[1][3].setForeground(Color.blue);
			infolbl[1][5].setForeground(Color.blue);
		} //if else

	} //updateVal()
	
	public String getTotalVal(int stockId) { //현재 가격 * 발행 주식 수 를 계산한 주식의 총 가치 반환
		
		String total;
		
		long value = (long)stock.getPrice()[MainPanel.dateId] * stock.getNumShare();
		long big = 100000000;
		long small = 10000;
	
		if(value > big) { //억, 만단위로만 끊음
			total = (value / big) + "억";
		} else if(value > small) {
			total = (value / small) + "만";
		} else {
			total = value + "원";
		} //if .. else
		
		return total;
	} //getTotalVal()
	
	public void updateMyVal() { //보유 돈, 주식 수 갱신
		myInfolbl[1].setText(myStock.getMoneyStr() + " 원");
		myInfolbl[3].setText("" + myStock.numHoldingStock[stockId]);
		updateStartVal();
	} //updateMyVal()
	
	public void setEmpty() { //입력 필드, 합산 초기화
		buyfd.setText("");
		sellfd.setText("");
		sumVallbl.setText("원");
	} //setEmpty(
	
	public void updateStartVal() { //마지막으로 구입한 가격과의 변동률 갱신
		if(myStock.startValStock[stockId] == 0) {
			myInfolbl[5].setText("");
		} else {
			String str = realTimeStock.getFlucPerFromStart(stockId, myStock.startValStock[stockId]);
			int fluc = realTimeStock.getFlucFromStart(stockId, myStock.startValStock[stockId]);
			myInfolbl[5].setText(str);
			
			if(fluc >= 0) {
				myInfolbl[5].setForeground(Color.red);
			} else {
				myInfolbl[5].setForeground(Color.blue);
			}
		} //if else
	} //updateStartVal()
	
} //StockPanel class

package StockSimulation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MyStockPanel extends JPanel {
	private RealTimeStock realTimeStock;
	private MyStock myStock;
	private JScrollPane stockScroll;
	private JLabel myMoneylbl, totalMoneylbl;
	private ImageIcon backgroundImg;
	private Object[][] tableData;
	private Object[] tempData;
	private Object[] rowData;
	private DefaultTableCellRenderer alignCenter, alignRight;
	protected JLabel myMoneyVallbl, totalMoneyVallbl;
	protected JButton nextBt, backBt;
	protected JButton exitBt;
	protected int[] holdStockArr; //테이블에 출력할 보유한 주식의 인덱스 값
	protected int totalHoldNum; //보유한 주식 종류 개수
	protected int currentPage; //0~2의 값
	protected JTable stockTable;
	protected DefaultTableModel stockDTM;
	
	private MainListener myStockL;
	
	private final Object[] tableHeader = {"종목명","현재가","전일비","등락률","보유량","구매대비"};
	
	public MyStockPanel(RealTimeStock realTimeStock, MyStock myStock, MainListener mainL) {
		
		this.setPreferredSize(new Dimension(600, 450));
		this.setLayout(null);
		
		this.realTimeStock = realTimeStock;
		this.myStock = myStock;
		myStockL = mainL;
		mainL.myStockPanel = this;
		
		backgroundImg = new ImageIcon("images/background3.png");
		
		holdStockArr = new int[24];
		tableData = new String[1][6];
		rowData = new Object[6];
		totalHoldNum = 0;
		currentPage = 0;
		
		stockDTM = new DefaultTableModel(tableData ,tableHeader) { //빈 데이터와 헤더를 가지는 테이블 모델 생성
			public boolean isCellEditable(int row, int col) { //테이블 수정 불가
				return false;
			} //isCellEditable()
		};
		
		alignCenter = new DefaultTableCellRenderer();
		alignCenter.setHorizontalAlignment(SwingConstants.CENTER); //가운데 정렬 설정
		alignRight = new DefaultTableCellRenderer();
		alignRight.setHorizontalAlignment(SwingConstants.RIGHT); //우측 정렬 설정
		
		stockTable = new JTable (stockDTM) { //위에서 생성한 테이블 모델을 가지는 테이블 생성
		    public Component prepareRenderer(TableCellRenderer tableRenderer, int row, int col) { //글자색 변경을 위한 override
		    	Component comp = super.prepareRenderer(tableRenderer, row, col);
		    	
		    	if(totalHoldNum > 0) {
			    	int myStockId = (currentPage * 8) + row;
			    	int stockId = holdStockArr[myStockId];
			    	int startFluc = realTimeStock.getFlucFromStart(stockId, myStock.startValStock[stockId]);
		    		
			        if (realTimeStock.getFluc(stockId) >= 0) { //증가할 때 빨간색
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.red);
			        	} //if
			        } else { //감소할 때 파란색
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.blue);
			        	} //if
			        } //if else
			        
			        if(col == 4) { //기본 데이터는 검정색
			        	comp.setForeground(Color.black);
			        } //if
			        
			        if(startFluc >= 0) { //시작 가격 비교
			        	if(col == 5) {
			        		comp.setForeground(Color.red);
			        	} //if
			        } else {
			        	if(col == 5) {
			        		comp.setForeground(Color.blue);
			        	} //if
			        } //if else
			        
			        if(col == 1) {
			        	comp.setForeground(Color.black);
			        } //if	
			    } //if

		        return comp;
		    } //PrepareRenderer()
		}; //JTable
		
		stockTable.getTableHeader().setReorderingAllowed(false); //이동 불가
		stockTable.getTableHeader().setResizingAllowed(false); //크기 조절 불가
		stockTable.getTableHeader().setPreferredSize(new Dimension(500, 30)); //헤더 높이 30
		stockTable.getColumnModel().getColumn(0).setCellRenderer(alignCenter); //가운데 정렬
		stockTable.getColumnModel().getColumn(0).setPreferredWidth(95); //너비 95
		stockTable.getColumnModel().getColumn(1).setCellRenderer(alignRight); //우측 정렬
		stockTable.getColumnModel().getColumn(1).setPreferredWidth(85); //너비 85
		stockTable.getColumnModel().getColumn(2).setCellRenderer(alignRight); //우측 정렬
		stockTable.getColumnModel().getColumn(3).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(4).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(5).setCellRenderer(alignRight);
		stockTable.setRowHeight(30); //셀 높이 변경
		stockTable.addMouseListener(myStockL.tableL);
		
		stockScroll = new JScrollPane(stockTable);
		stockScroll.setBounds(50, 90, 500, 272); //270이 높이. 2는 유격
		stockScroll.setBorder(BorderFactory.createLineBorder(Color.black));
		stockScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //스크롤바 사용 안함
		stockScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.add(stockScroll);
		
		stockDTM.removeRow(0); //생성 후 쓸모없는 데이터 삭제
		
		backBt = new JButton(new ImageIcon("images/backArrow.png")); //테이블 이전 버튼
		backBt.setBounds(370, 370, 20, 20);
		backBt.setBorderPainted(false);
		backBt.setContentAreaFilled(false);
		backBt.setFocusPainted(false);
		backBt.setVisible(false);
		backBt.addActionListener(myStockL.sBtnL);
		this.add(backBt);
		
		nextBt = new JButton(new ImageIcon("images/nextArrow.png")); //테이블 다음 버튼
		nextBt.setBounds(410, 370, 20, 20);
		nextBt.setBorderPainted(false);
		nextBt.setContentAreaFilled(false);
		nextBt.setFocusPainted(false);
		nextBt.setVisible(false);
		nextBt.addActionListener(myStockL.sBtnL);
		this.add(nextBt);
		
		exitBt = new JButton("뒤로"); //창 닫기 버튼
		exitBt.setBounds(500, 7, 80, 30);
		exitBt.setFont(new Font("굴림", Font.PLAIN, 8));
		exitBt.addActionListener(myStockL.sBtnL);
		this.add(exitBt);
		
		myMoneylbl = new JLabel("보유 금액 :");
		myMoneylbl.setBounds(20, 400, 70, 30);
		this.add(myMoneylbl);
		
		myMoneyVallbl = new JLabel("150,000 원");
		myMoneyVallbl.setBounds(30, 400, 130, 30);
		myMoneyVallbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(myMoneyVallbl);
		
		totalMoneylbl = new JLabel("총 보유자산 :");
		totalMoneylbl.setBounds(220, 400, 100, 30);
		this.add(totalMoneylbl);
		
		totalMoneyVallbl = new JLabel("150,000 원");
		totalMoneyVallbl.setBounds(250, 400, 130, 30);
		totalMoneyVallbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(totalMoneyVallbl);
		
	} //MyStockPanel()
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		page.drawImage(backgroundImg.getImage(), 0, 0, this);
	} //paintComponent()
	
	public void updateHoldStock() { //보유한 주식의 id 갱신
		totalHoldNum = 0;
		
		for(int i=0; i<24; i++) {
			if(myStock.numHoldingStock[i] > 0) {
				holdStockArr[totalHoldNum] = i;
				totalHoldNum += 1;
			} //if
		} //for
	} //updateHoldStock()
	
	public void updateTable() {
		int maxIndex;
		int stockId;
		
		while(stockDTM.getRowCount() > 0) { //먼저 테이블에 있던 행들 제거
			stockDTM.removeRow(0);
		} //while
		
		if((currentPage + 1) * 8 <= totalHoldNum) { //현재 페이지의 테이블에 출력할 주식 개수 계산
			maxIndex = 8;
		} else {
			maxIndex = totalHoldNum % 8;
		} //if else

		for(int i=0; i<maxIndex; i++) { //새로운 테이블 데이터를 받아 추가
			stockId = holdStockArr[(currentPage * 8) + i];
			tempData = realTimeStock.getTableRow(stockId);
			
			for(int j=0; j<4; j++) {
				rowData[j] = tempData[j];
			} //for
			
			rowData[4] = myStock.numHoldingStock[stockId];
			rowData[5] = getStartFluc(stockId);
			
			stockDTM.addRow(rowData);
		} //for
		
	} //updateTable()
	
	public String getStartFluc(int stockId) { //마지막으로 구입한 때의 가격과의 변동률 반환
		String str = realTimeStock.getFlucPerFromStart(stockId, myStock.startValStock[stockId]);

		return str;
	} //getStartFluc()
	
	public void updateButton() { //버튼 유무 갱신
		if(totalHoldNum > (currentPage + 1) * 8) { //현재 페이지 값보다 더 많아질 때 버튼이 보임
			nextBt.setVisible(true);
		} else {
			nextBt.setVisible(false);
		} //if else
		
		if(currentPage > 0) { //0 페이지가 아니면 버튼이 보임
			backBt.setVisible(true);
		} else { //0 페이지일 때는 뒤 버튼이 필요 없음
			backBt.setVisible(false);
		} //if else
	} //updateButton()
	
	public void updatePage() {
		if(totalHoldNum / 8 < currentPage) { //주식 수가 갱신되어 현재 페이지가 필요 없을 때
			currentPage = totalHoldNum / 8;
		} //if
	} //updatePage()
	
	public void updateMyVal() { //사용자 보유 값 갱신
		int total = myStock.havingMoney;
		int stockId;
		int num;
		int price;
		
		myMoneyVallbl.setText(myStock.getMoneyStr() + " 원");
		
		for(int i=0;i<totalHoldNum; i++) { //보유한 주식의 총 가치 계산
			stockId = holdStockArr[i];
			num = myStock.numHoldingStock[stockId];
			price = realTimeStock.stocks[stockId].getPrice()[MainPanel.dateId];
			
			total += num * price;
		} //for
		
		totalMoneyVallbl.setText(String.format("%,d", total) + " 원");
	} //updateMyVal()
	
} //MyStockPanel class

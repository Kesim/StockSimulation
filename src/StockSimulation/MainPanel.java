package StockSimulation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MainPanel extends JPanel {
	protected static Date currentDate;
	protected static int dateId; //프로그램 사용할 가상의 날짜 (0~1094)
	private Calendar cal;
	private SimpleDateFormat sdf;
	private SaveData saveData; //MainPanel의 일부 변수 값 저장을 하기 위한 변수
	private String eventStr;
	private Object[][] tableData;
	private DefaultTableCellRenderer alignCenter, alignRight;
	private JScrollPane eventScroll, tableScroll;
	private JLabel datelbl;
	private ImageIcon backgroundImg;
	protected JFrame stockFrame, myStockFrame;
	protected StockPanel stockPanel;
	protected MyStockPanel myStockPanel;
	protected RealTimeStock realTimeStock;
	protected MyStock myStock;
	protected int currentPage; //화면에 보여줄 Table 페이지 (0~2)
	protected EventText eventText;
	protected JTable stockTable;
	protected DefaultTableModel stockDTM;
	protected JButton myInfoBt;
	protected JButton nextBt, backBt;
	protected JButton exitBt;
	protected JTextArea eventTextArea;
	protected JLabel myHoldlbl, myHoldMoneylbl;
	protected JLabel timerlbl;
	
	private MainListener mainL;
	private RealTimeThread rtThread;
	
	private final Object[] tableHeader = {"종목명","현재가","전일비","등락률"}; //테이블 헤더
	private final String[] alrertMsg = {"예", "아니오", "취소"}; //알림창 버튼 표시 문자열
	private final String[] savePath = {"RTStock.ser", "MyStock.ser", "SData.ser"}; //저장할 파일 경로
	
	public MainPanel() {	
		cal = Calendar.getInstance(); //현재 날짜를 가져옴
		dateId = Calendar.DAY_OF_YEAR % 1095; //프로그램 사용할 가상의 날짜
		currentDate = new Date();
		currentDate = cal.getTime(); //현재 날짜
		

		realTimeStock = new RealTimeStock();
		myStock = new MyStock();
				
		setMain();
	} //MainPanel()
	
	public MainPanel(RealTimeStock realTimeStock, MyStock myStock, SaveData saveData) {
		this.realTimeStock = realTimeStock;
		this.myStock = myStock;
		this.saveData = saveData;
		
		setSaveVal(); //저장된 변수값 할당
		
		setMain();
		
		myStockPanel.updateHoldStock(); //보유한 주식을 보이기 하기 위해 MyStockPanel를 갱신
		myStockPanel.updatePage();
		myStockPanel.updateTable();
		myStockPanel.updateButton();
		myStockPanel.updateMyVal();
	} //MainPanel()
	
	public void setMain() { //이 클래스 객체 생성에 중복되는 부분
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(600, 450));
		this.setLayout(null);
						
		backgroundImg = new ImageIcon("images/background.png");
		
		currentPage = 0; //테이블 페이지는 0으로 초기화
		
		mainL = new MainListener(this);
		
		stockFrame = new JFrame("주식 상세정보");
		stockFrame.setResizable(false);
		stockFrame.setSize(600, 480);
		stockFrame.setLocationRelativeTo(null);
			
		stockFrame.pack();
		stockFrame.setVisible(false);
		
		myStockFrame = new JFrame("내 정보");
		myStockFrame.setResizable(false);
		myStockFrame.setSize(600, 450);
		myStockFrame.setLocationRelativeTo(null);
		
		myStockPanel = new MyStockPanel(realTimeStock, myStock, mainL);
		myStockFrame.getContentPane().add(myStockPanel);
		
		myStockFrame.pack();
		myStockFrame.setVisible(false);
		
		eventTextArea = new JTextArea(eventStr);
		eventTextArea.setFont(new Font("돋움", Font.PLAIN, 11));
		eventTextArea.setEditable(false);

		eventScroll = new JScrollPane(eventTextArea);
		eventScroll.setBounds(0, 0, 380, 80);
		eventScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(eventScroll);
		
		//주식 테이블 설정
		tableData = new String[8][4];
		for(int i=(8*currentPage); i<(8*(currentPage+1)); i++){ //테이블 데이터 가져오기
			tableData[i] = realTimeStock.getTableRow(i);
		} //for
		
		stockDTM = new DefaultTableModel(tableData, tableHeader) { //위의 데이터와 헤더로 테이블 모델 생성
			public boolean isCellEditable(int row, int col) { //테이블 수정 불가
				return false;
			} //isCellEditable()
		};
		
		alignCenter = new DefaultTableCellRenderer();
		alignCenter.setHorizontalAlignment(SwingConstants.CENTER); //가운데 정렬 설정
		alignRight = new DefaultTableCellRenderer();
		alignRight.setHorizontalAlignment(SwingConstants.RIGHT); //우측 정렬 설정
		
		stockTable = new JTable(stockDTM) { //위에서 생성한 데이터 모델을 가지는 테이블 생성
			
		    public Component prepareRenderer(TableCellRenderer tableRenderer, int row, int col) { //글자색 변경을 위한 override

		    	Component comp = super.prepareRenderer(tableRenderer, row, col);
	    		
		    	if(stockDTM.getRowCount() > 0) {
			        if (realTimeStock.getFluc((currentPage * 8) + row) >= 0) { //값 증가일 때 빨간색
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.red);
			        	} //if
			        } else { //값 감소일 때 파란색
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.blue);
			        	} //if
			        } //if else
			        
			        if(col == 1) { //다른 열은 까만색
			        	comp.setForeground(Color.black);
			        } //if	
		    	} //if
		    	
		        return comp;
		    } //PrepareRenderer()
		};
		
		stockTable.getTableHeader().setReorderingAllowed(false); //이동 불가
		stockTable.getTableHeader().setResizingAllowed(false); //크기 조절 불가
		stockTable.getTableHeader().setPreferredSize(new Dimension(450, 30)); //높이 조절
		stockTable.getColumnModel().getColumn(0).setCellRenderer(alignCenter); //가운데 정렬
		stockTable.getColumnModel().getColumn(0).setPreferredWidth(110); //너비 설정
		stockTable.getColumnModel().getColumn(1).setCellRenderer(alignRight); //우측 정렬
		stockTable.getColumnModel().getColumn(2).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(3).setCellRenderer(alignRight);
		stockTable.setRowHeight(30); //셀 높이 변경
		stockTable.addMouseListener(mainL.tableL);
		
		tableScroll = new JScrollPane(stockTable);
		tableScroll.setBounds(75, 90, 450, 272); //270이 높이. 2는 유격
		tableScroll.setBorder(BorderFactory.createLineBorder(Color.black));
		tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //스크롤 제거
		tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.add(tableScroll);
			
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		datelbl = new JLabel(sdf.format(currentDate)); //날짜 표시
		datelbl.setBackground(Color.blue);
		datelbl.setBounds(480, 10, 100, 30);
		this.add(datelbl);
		
		myHoldlbl = new JLabel("보유 금액 :");
		myHoldlbl.setBounds(20, 400, 70, 30);
		this.add(myHoldlbl);
		
		myHoldMoneylbl = new JLabel(myStock.getMoneyStr() + " 원");
		myHoldMoneylbl.setBounds(100, 400, 130, 30);
		myHoldMoneylbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(myHoldMoneylbl);
		
		backBt = new JButton(new ImageIcon("images/backArrow.png")); //테이블 이전 버튼
		backBt.setBounds(370, 370, 20, 20);
		backBt.setBorderPainted(false);
		backBt.setContentAreaFilled(false);
		backBt.setFocusPainted(false);
		backBt.setVisible(false);
		backBt.addActionListener(mainL.btnL);
		this.add(backBt);
		
		nextBt = new JButton(new ImageIcon("images/nextArrow.png")); //테이블 다음 버튼
		nextBt.setBounds(410, 370, 20, 20);
		nextBt.setBorderPainted(false);
		nextBt.setContentAreaFilled(false);
		nextBt.setFocusPainted(false);
		nextBt.addActionListener(mainL.btnL);
		this.add(nextBt);
		
		myInfoBt = new JButton("내 정보");
		myInfoBt.setBounds(250, 400, 100, 30);
		myInfoBt.addActionListener(mainL.btnL);
		this.add(myInfoBt);
		
		exitBt = new JButton("종료");
		exitBt.setBounds(530, 410, 60, 30);
		exitBt.addActionListener(mainL.btnL);
		this.add(exitBt);
		
		eventText = new EventText(this);
		
		rtThread = new RealTimeThread(this);
		rtThread.start();
	} //setMain()
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		page.drawImage(backgroundImg.getImage(), 0, 0, this);
	} //paintComponent()
	
	public static int getYesterday() {
		int day = dateId - 1;
		
		if(day < 0) day = 1094;
		
		return day;
	} //getYesterDay()
	
	public static void updateDateId() {
		dateId = (dateId + 1) % 1095;	
	} //updateDay()
	
	public void openStockPanel(RealTimeStock realTimeStock, int stockId, MyStock myStock) { //테이블 클릭 시 주식 창 열어줌
		
		if(stockPanel != null) { //기존에 생성되있으면 제거
			removeStockPanel();
		} //if
		
		stockPanel = new StockPanel(realTimeStock, stockId, myStock, mainL);
		stockFrame.getContentPane().add(stockPanel);
		stockFrame.pack();
		stockFrame.setVisible(true);
	} //openStockPanel()
	
	public void removeStockPanel() {
		
		stockFrame.getContentPane().removeAll();
		stockFrame.revalidate();
		stockFrame.setVisible(false);	
		
	} //removeStockPanel()
	
	public void updateTable() {
		
		int stockId;
		
		for(int i=0; i<8; i++) { //테이블의 데이터 모두 삭제
			stockId = (currentPage * 8) + i;
			stockDTM.addRow(realTimeStock.getTableRow(stockId));
			stockDTM.removeRow(0);
		} //for
		
		stockTable.repaint(); //테이블 다시 그림
		
	} //updateTable()
	
	public void updateDate() {
		
		cal.add(Calendar.DAY_OF_YEAR, 1);
		currentDate = cal.getTime();
		datelbl.setText(sdf.format(currentDate));
		
	} //updateDate()
	
	public void mainExit() {
		rtThread.stop(); //창이 뜨는 동안 쓰레드 멈춤
		
		int result = JOptionPane.showOptionDialog(
				null, "데이터를 저장 후\n종료 하시겠습니까?", "종료 창", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, alrertMsg, "취소"
				);
		
		if(result == JOptionPane.YES_OPTION) {
			saveMain();
			System.exit(0);
		} else if(result == JOptionPane.NO_OPTION) {
			System.exit(0);
		} else if(result == JOptionPane.CANCEL_OPTION) {
			rtThread.reRun(); //취소 시 쓰레드 다시 시작
		} //if else
		
		
	} //mainExit()
	
	public void saveMain() { //갱신되는 값을 가지는 객체들과 데이터 저장
		eventStr = eventTextArea.getText();
		saveData = new SaveData(currentDate, dateId, eventStr);
		
		try {
			ObjectOutputStream outputStream;
			
			outputStream = new ObjectOutputStream(new FileOutputStream(savePath[0]));
			outputStream.writeObject(realTimeStock);
			outputStream.close();
			
			outputStream = new ObjectOutputStream(new FileOutputStream(savePath[1]));
			outputStream.writeObject(myStock);
			outputStream.close();
			
			outputStream = new ObjectOutputStream(new FileOutputStream(savePath[2]));
			outputStream.writeObject(saveData);
			outputStream.close();

		} catch(Exception e) {}
	} //saveMain()
	
	public void setSaveVal() {
		this.dateId = saveData.dateId;
		this.currentDate = saveData.currentDate;
		this.eventStr = saveData.eventStr;
		
		cal = Calendar.getInstance();
		cal.setTime(currentDate);
	} //setSaveVal()

} //MainPannel class

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
	protected static int dateId; //���α׷� ����� ������ ��¥ (0~1094)
	private Calendar cal;
	private SimpleDateFormat sdf;
	private SaveData saveData; //MainPanel�� �Ϻ� ���� �� ������ �ϱ� ���� ����
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
	protected int currentPage; //ȭ�鿡 ������ Table ������ (0~2)
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
	
	private final Object[] tableHeader = {"�����","���簡","���Ϻ�","�����"}; //���̺� ���
	private final String[] alrertMsg = {"��", "�ƴϿ�", "���"}; //�˸�â ��ư ǥ�� ���ڿ�
	private final String[] savePath = {"RTStock.ser", "MyStock.ser", "SData.ser"}; //������ ���� ���
	
	public MainPanel() {	
		cal = Calendar.getInstance(); //���� ��¥�� ������
		dateId = Calendar.DAY_OF_YEAR % 1095; //���α׷� ����� ������ ��¥
		currentDate = new Date();
		currentDate = cal.getTime(); //���� ��¥
		

		realTimeStock = new RealTimeStock();
		myStock = new MyStock();
				
		setMain();
	} //MainPanel()
	
	public MainPanel(RealTimeStock realTimeStock, MyStock myStock, SaveData saveData) {
		this.realTimeStock = realTimeStock;
		this.myStock = myStock;
		this.saveData = saveData;
		
		setSaveVal(); //����� ������ �Ҵ�
		
		setMain();
		
		myStockPanel.updateHoldStock(); //������ �ֽ��� ���̱� �ϱ� ���� MyStockPanel�� ����
		myStockPanel.updatePage();
		myStockPanel.updateTable();
		myStockPanel.updateButton();
		myStockPanel.updateMyVal();
	} //MainPanel()
	
	public void setMain() { //�� Ŭ���� ��ü ������ �ߺ��Ǵ� �κ�
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(600, 450));
		this.setLayout(null);
						
		backgroundImg = new ImageIcon("images/background.png");
		
		currentPage = 0; //���̺� �������� 0���� �ʱ�ȭ
		
		mainL = new MainListener(this);
		
		stockFrame = new JFrame("�ֽ� ������");
		stockFrame.setResizable(false);
		stockFrame.setSize(600, 480);
		stockFrame.setLocationRelativeTo(null);
			
		stockFrame.pack();
		stockFrame.setVisible(false);
		
		myStockFrame = new JFrame("�� ����");
		myStockFrame.setResizable(false);
		myStockFrame.setSize(600, 450);
		myStockFrame.setLocationRelativeTo(null);
		
		myStockPanel = new MyStockPanel(realTimeStock, myStock, mainL);
		myStockFrame.getContentPane().add(myStockPanel);
		
		myStockFrame.pack();
		myStockFrame.setVisible(false);
		
		eventTextArea = new JTextArea(eventStr);
		eventTextArea.setFont(new Font("����", Font.PLAIN, 11));
		eventTextArea.setEditable(false);

		eventScroll = new JScrollPane(eventTextArea);
		eventScroll.setBounds(0, 0, 380, 80);
		eventScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(eventScroll);
		
		//�ֽ� ���̺� ����
		tableData = new String[8][4];
		for(int i=(8*currentPage); i<(8*(currentPage+1)); i++){ //���̺� ������ ��������
			tableData[i] = realTimeStock.getTableRow(i);
		} //for
		
		stockDTM = new DefaultTableModel(tableData, tableHeader) { //���� �����Ϳ� ����� ���̺� �� ����
			public boolean isCellEditable(int row, int col) { //���̺� ���� �Ұ�
				return false;
			} //isCellEditable()
		};
		
		alignCenter = new DefaultTableCellRenderer();
		alignCenter.setHorizontalAlignment(SwingConstants.CENTER); //��� ���� ����
		alignRight = new DefaultTableCellRenderer();
		alignRight.setHorizontalAlignment(SwingConstants.RIGHT); //���� ���� ����
		
		stockTable = new JTable(stockDTM) { //������ ������ ������ ���� ������ ���̺� ����
			
		    public Component prepareRenderer(TableCellRenderer tableRenderer, int row, int col) { //���ڻ� ������ ���� override

		    	Component comp = super.prepareRenderer(tableRenderer, row, col);
	    		
		    	if(stockDTM.getRowCount() > 0) {
			        if (realTimeStock.getFluc((currentPage * 8) + row) >= 0) { //�� ������ �� ������
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.red);
			        	} //if
			        } else { //�� ������ �� �Ķ���
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.blue);
			        	} //if
			        } //if else
			        
			        if(col == 1) { //�ٸ� ���� ���
			        	comp.setForeground(Color.black);
			        } //if	
		    	} //if
		    	
		        return comp;
		    } //PrepareRenderer()
		};
		
		stockTable.getTableHeader().setReorderingAllowed(false); //�̵� �Ұ�
		stockTable.getTableHeader().setResizingAllowed(false); //ũ�� ���� �Ұ�
		stockTable.getTableHeader().setPreferredSize(new Dimension(450, 30)); //���� ����
		stockTable.getColumnModel().getColumn(0).setCellRenderer(alignCenter); //��� ����
		stockTable.getColumnModel().getColumn(0).setPreferredWidth(110); //�ʺ� ����
		stockTable.getColumnModel().getColumn(1).setCellRenderer(alignRight); //���� ����
		stockTable.getColumnModel().getColumn(2).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(3).setCellRenderer(alignRight);
		stockTable.setRowHeight(30); //�� ���� ����
		stockTable.addMouseListener(mainL.tableL);
		
		tableScroll = new JScrollPane(stockTable);
		tableScroll.setBounds(75, 90, 450, 272); //270�� ����. 2�� ����
		tableScroll.setBorder(BorderFactory.createLineBorder(Color.black));
		tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //��ũ�� ����
		tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.add(tableScroll);
			
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		datelbl = new JLabel(sdf.format(currentDate)); //��¥ ǥ��
		datelbl.setBackground(Color.blue);
		datelbl.setBounds(480, 10, 100, 30);
		this.add(datelbl);
		
		myHoldlbl = new JLabel("���� �ݾ� :");
		myHoldlbl.setBounds(20, 400, 70, 30);
		this.add(myHoldlbl);
		
		myHoldMoneylbl = new JLabel(myStock.getMoneyStr() + " ��");
		myHoldMoneylbl.setBounds(100, 400, 130, 30);
		myHoldMoneylbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(myHoldMoneylbl);
		
		backBt = new JButton(new ImageIcon("images/backArrow.png")); //���̺� ���� ��ư
		backBt.setBounds(370, 370, 20, 20);
		backBt.setBorderPainted(false);
		backBt.setContentAreaFilled(false);
		backBt.setFocusPainted(false);
		backBt.setVisible(false);
		backBt.addActionListener(mainL.btnL);
		this.add(backBt);
		
		nextBt = new JButton(new ImageIcon("images/nextArrow.png")); //���̺� ���� ��ư
		nextBt.setBounds(410, 370, 20, 20);
		nextBt.setBorderPainted(false);
		nextBt.setContentAreaFilled(false);
		nextBt.setFocusPainted(false);
		nextBt.addActionListener(mainL.btnL);
		this.add(nextBt);
		
		myInfoBt = new JButton("�� ����");
		myInfoBt.setBounds(250, 400, 100, 30);
		myInfoBt.addActionListener(mainL.btnL);
		this.add(myInfoBt);
		
		exitBt = new JButton("����");
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
	
	public void openStockPanel(RealTimeStock realTimeStock, int stockId, MyStock myStock) { //���̺� Ŭ�� �� �ֽ� â ������
		
		if(stockPanel != null) { //������ ������������ ����
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
		
		for(int i=0; i<8; i++) { //���̺��� ������ ��� ����
			stockId = (currentPage * 8) + i;
			stockDTM.addRow(realTimeStock.getTableRow(stockId));
			stockDTM.removeRow(0);
		} //for
		
		stockTable.repaint(); //���̺� �ٽ� �׸�
		
	} //updateTable()
	
	public void updateDate() {
		
		cal.add(Calendar.DAY_OF_YEAR, 1);
		currentDate = cal.getTime();
		datelbl.setText(sdf.format(currentDate));
		
	} //updateDate()
	
	public void mainExit() {
		rtThread.stop(); //â�� �ߴ� ���� ������ ����
		
		int result = JOptionPane.showOptionDialog(
				null, "�����͸� ���� ��\n���� �Ͻðڽ��ϱ�?", "���� â", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, alrertMsg, "���"
				);
		
		if(result == JOptionPane.YES_OPTION) {
			saveMain();
			System.exit(0);
		} else if(result == JOptionPane.NO_OPTION) {
			System.exit(0);
		} else if(result == JOptionPane.CANCEL_OPTION) {
			rtThread.reRun(); //��� �� ������ �ٽ� ����
		} //if else
		
		
	} //mainExit()
	
	public void saveMain() { //���ŵǴ� ���� ������ ��ü��� ������ ����
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

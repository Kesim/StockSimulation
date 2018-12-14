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
	protected int[] holdStockArr; //���̺� ����� ������ �ֽ��� �ε��� ��
	protected int totalHoldNum; //������ �ֽ� ���� ����
	protected int currentPage; //0~2�� ��
	protected JTable stockTable;
	protected DefaultTableModel stockDTM;
	
	private MainListener myStockL;
	
	private final Object[] tableHeader = {"�����","���簡","���Ϻ�","�����","������","���Ŵ��"};
	
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
		
		stockDTM = new DefaultTableModel(tableData ,tableHeader) { //�� �����Ϳ� ����� ������ ���̺� �� ����
			public boolean isCellEditable(int row, int col) { //���̺� ���� �Ұ�
				return false;
			} //isCellEditable()
		};
		
		alignCenter = new DefaultTableCellRenderer();
		alignCenter.setHorizontalAlignment(SwingConstants.CENTER); //��� ���� ����
		alignRight = new DefaultTableCellRenderer();
		alignRight.setHorizontalAlignment(SwingConstants.RIGHT); //���� ���� ����
		
		stockTable = new JTable (stockDTM) { //������ ������ ���̺� ���� ������ ���̺� ����
		    public Component prepareRenderer(TableCellRenderer tableRenderer, int row, int col) { //���ڻ� ������ ���� override
		    	Component comp = super.prepareRenderer(tableRenderer, row, col);
		    	
		    	if(totalHoldNum > 0) {
			    	int myStockId = (currentPage * 8) + row;
			    	int stockId = holdStockArr[myStockId];
			    	int startFluc = realTimeStock.getFlucFromStart(stockId, myStock.startValStock[stockId]);
		    		
			        if (realTimeStock.getFluc(stockId) >= 0) { //������ �� ������
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.red);
			        	} //if
			        } else { //������ �� �Ķ���
			        	if(col == 2 || col == 3) {
			        		comp.setForeground(Color.blue);
			        	} //if
			        } //if else
			        
			        if(col == 4) { //�⺻ �����ʹ� ������
			        	comp.setForeground(Color.black);
			        } //if
			        
			        if(startFluc >= 0) { //���� ���� ��
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
		
		stockTable.getTableHeader().setReorderingAllowed(false); //�̵� �Ұ�
		stockTable.getTableHeader().setResizingAllowed(false); //ũ�� ���� �Ұ�
		stockTable.getTableHeader().setPreferredSize(new Dimension(500, 30)); //��� ���� 30
		stockTable.getColumnModel().getColumn(0).setCellRenderer(alignCenter); //��� ����
		stockTable.getColumnModel().getColumn(0).setPreferredWidth(95); //�ʺ� 95
		stockTable.getColumnModel().getColumn(1).setCellRenderer(alignRight); //���� ����
		stockTable.getColumnModel().getColumn(1).setPreferredWidth(85); //�ʺ� 85
		stockTable.getColumnModel().getColumn(2).setCellRenderer(alignRight); //���� ����
		stockTable.getColumnModel().getColumn(3).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(4).setCellRenderer(alignRight);
		stockTable.getColumnModel().getColumn(5).setCellRenderer(alignRight);
		stockTable.setRowHeight(30); //�� ���� ����
		stockTable.addMouseListener(myStockL.tableL);
		
		stockScroll = new JScrollPane(stockTable);
		stockScroll.setBounds(50, 90, 500, 272); //270�� ����. 2�� ����
		stockScroll.setBorder(BorderFactory.createLineBorder(Color.black));
		stockScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //��ũ�ѹ� ��� ����
		stockScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.add(stockScroll);
		
		stockDTM.removeRow(0); //���� �� ������� ������ ����
		
		backBt = new JButton(new ImageIcon("images/backArrow.png")); //���̺� ���� ��ư
		backBt.setBounds(370, 370, 20, 20);
		backBt.setBorderPainted(false);
		backBt.setContentAreaFilled(false);
		backBt.setFocusPainted(false);
		backBt.setVisible(false);
		backBt.addActionListener(myStockL.sBtnL);
		this.add(backBt);
		
		nextBt = new JButton(new ImageIcon("images/nextArrow.png")); //���̺� ���� ��ư
		nextBt.setBounds(410, 370, 20, 20);
		nextBt.setBorderPainted(false);
		nextBt.setContentAreaFilled(false);
		nextBt.setFocusPainted(false);
		nextBt.setVisible(false);
		nextBt.addActionListener(myStockL.sBtnL);
		this.add(nextBt);
		
		exitBt = new JButton("�ڷ�"); //â �ݱ� ��ư
		exitBt.setBounds(500, 7, 80, 30);
		exitBt.setFont(new Font("����", Font.PLAIN, 8));
		exitBt.addActionListener(myStockL.sBtnL);
		this.add(exitBt);
		
		myMoneylbl = new JLabel("���� �ݾ� :");
		myMoneylbl.setBounds(20, 400, 70, 30);
		this.add(myMoneylbl);
		
		myMoneyVallbl = new JLabel("150,000 ��");
		myMoneyVallbl.setBounds(30, 400, 130, 30);
		myMoneyVallbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(myMoneyVallbl);
		
		totalMoneylbl = new JLabel("�� �����ڻ� :");
		totalMoneylbl.setBounds(220, 400, 100, 30);
		this.add(totalMoneylbl);
		
		totalMoneyVallbl = new JLabel("150,000 ��");
		totalMoneyVallbl.setBounds(250, 400, 130, 30);
		totalMoneyVallbl.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(totalMoneyVallbl);
		
	} //MyStockPanel()
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		page.drawImage(backgroundImg.getImage(), 0, 0, this);
	} //paintComponent()
	
	public void updateHoldStock() { //������ �ֽ��� id ����
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
		
		while(stockDTM.getRowCount() > 0) { //���� ���̺� �ִ� ��� ����
			stockDTM.removeRow(0);
		} //while
		
		if((currentPage + 1) * 8 <= totalHoldNum) { //���� �������� ���̺� ����� �ֽ� ���� ���
			maxIndex = 8;
		} else {
			maxIndex = totalHoldNum % 8;
		} //if else

		for(int i=0; i<maxIndex; i++) { //���ο� ���̺� �����͸� �޾� �߰�
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
	
	public String getStartFluc(int stockId) { //���������� ������ ���� ���ݰ��� ������ ��ȯ
		String str = realTimeStock.getFlucPerFromStart(stockId, myStock.startValStock[stockId]);

		return str;
	} //getStartFluc()
	
	public void updateButton() { //��ư ���� ����
		if(totalHoldNum > (currentPage + 1) * 8) { //���� ������ ������ �� ������ �� ��ư�� ����
			nextBt.setVisible(true);
		} else {
			nextBt.setVisible(false);
		} //if else
		
		if(currentPage > 0) { //0 �������� �ƴϸ� ��ư�� ����
			backBt.setVisible(true);
		} else { //0 �������� ���� �� ��ư�� �ʿ� ����
			backBt.setVisible(false);
		} //if else
	} //updateButton()
	
	public void updatePage() {
		if(totalHoldNum / 8 < currentPage) { //�ֽ� ���� ���ŵǾ� ���� �������� �ʿ� ���� ��
			currentPage = totalHoldNum / 8;
		} //if
	} //updatePage()
	
	public void updateMyVal() { //����� ���� �� ����
		int total = myStock.havingMoney;
		int stockId;
		int num;
		int price;
		
		myMoneyVallbl.setText(myStock.getMoneyStr() + " ��");
		
		for(int i=0;i<totalHoldNum; i++) { //������ �ֽ��� �� ��ġ ���
			stockId = holdStockArr[i];
			num = myStock.numHoldingStock[stockId];
			price = realTimeStock.stocks[stockId].getPrice()[MainPanel.dateId];
			
			total += num * price;
		} //for
		
		totalMoneyVallbl.setText(String.format("%,d", total) + " ��");
	} //updateMyVal()
	
} //MyStockPanel class

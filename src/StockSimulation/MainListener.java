package StockSimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainListener {

	private int num, price, total;
	private StartPanel startPanel;
	private MainPanel mainPanel;
	private MyStock myStock;
	protected StockPanel stockPanel;
	protected MyStockPanel myStockPanel;
	protected ButtonListener btnL;
	protected SubButtonListener sBtnL;
	protected textInputListener textL;
	protected TableListener tableL;
	
	public MainListener(StartPanel startPanel) { //StartPanel���� ����� ������
		this.startPanel = startPanel;
		
		btnL = new ButtonListener();
	} //MainPanelListener()
	
	public MainListener(MainPanel mainPanel) { //MainPanel�� �� ���� ���� ����� ������
		this.mainPanel = mainPanel;
		this.myStockPanel = mainPanel.myStockPanel;
		this.myStock = mainPanel.myStock;
		
		btnL = new ButtonListener();
		sBtnL = new SubButtonListener();
		textL = new textInputListener();
		tableL = new TableListener();
	} //MainPanelListener()
	
	
	public class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			
			Object obj = event.getSource();
			
			//StartPanel
			if(startPanel != null) {
				if(obj == startPanel.startBt) { //����
					startPanel.mainFrame.openMain();
					return;
				} else if(obj == startPanel.loadBt) { //�ҷ�����
					startPanel.mainFrame.loadMain();
					return;
				} else if(obj == startPanel.exitBt) { //���α׷� ����
					System.exit(0);
				} //if .. else if
			} //if
			
			
			//MainPanel
			if(obj == mainPanel.nextBt) { //���̺��� ���� ��ư ���� �� ���̺� ����
				mainPanel.currentPage += 1;
				
				mainPanel.backBt.setVisible(true);
				if(mainPanel.currentPage == 2) {
					mainPanel.nextBt.setVisible(false);
				} //if
				
				for(int i=0; i<8; i++) {
					mainPanel.stockDTM.removeRow(0);
				} //for
				
				for(int i=0; i<8; i++) {
					int j = (mainPanel.currentPage * 8) + i; //stock �迭�� �ε��� ��
					
					mainPanel.stockDTM.addRow(mainPanel.realTimeStock.getTableRow(j));
				} //for
				
			} else if(obj == mainPanel.backBt) { //���̺��� �ڷ� ��ư ���� �� ���̺� ����
				mainPanel.currentPage -= 1;
				
				mainPanel.nextBt.setVisible(true);
				if(mainPanel.currentPage == 0) {
					mainPanel.backBt.setVisible(false);
				} //if
				
				for(int i=0; i<8; i++) {
					mainPanel.stockDTM.removeRow(0);
				} //for
				
				for(int i=0; i<8; i++) {
					int j = (mainPanel.currentPage * 8) + i; //stock �迭�� �ε��� ��
					
					mainPanel.stockDTM.addRow(mainPanel.realTimeStock.getTableRow(j));
				} //for
				
			} else if(obj == mainPanel.myInfoBt) { //������ â
				mainPanel.myStockFrame.setVisible(true);
			} else if(obj == mainPanel.exitBt) { //���� â
				mainPanel.mainExit();
			} //if else.. if
			
			
		} //actionPerformed()
		
	} //ButtonListener class
	
	public class SubButtonListener implements ActionListener{ //StockPanel, MyStockPanel�� ��ư ������

		public void actionPerformed(ActionEvent event) {
			
			Object obj = event.getSource();
			
			//StockPanel
			if(stockPanel != null) {
				if(obj == stockPanel.buyBt || obj == stockPanel.buyfd) { //���� ��ư�̳� ���� �Է� ��
					num = Integer.parseInt(stockPanel.buyfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					total = price * num;
					if(myStock.havingMoney >= total) { //�� ���� ����� ���� ����
						myStock.havingMoney -= total;
						
						if(myStock.numHoldingStock[stockPanel.stockId] == 0) { //���� ó�� ���� ���� ������ ����
							myStock.startValStock[stockPanel.stockId] = price;
						} //if
						
						myStock.numHoldingStock[stockPanel.stockId] += num; //���� ���� ����
						
						mainPanel.myHoldMoneylbl.setText(String.format("%,d", myStock.havingMoney) + " ��"); //���� �ݾ� ��� ����
						stockPanel.updateMyVal(); //StockPanel ����
					} //if
					
					stockPanel.setEmpty();
					
					mainPanel.myStockPanel.updateHoldStock(); //MyStockPanel ����
					mainPanel.myStockPanel.updatePage();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
					mainPanel.myStockPanel.updateMyVal();
				} else if(obj == stockPanel.sellBt || obj == stockPanel.sellfd) { //�Ǹ� ��ư�̳� ���� �Է� ��
					num = Integer.parseInt(stockPanel.sellfd.getText());
					
					if(myStock.numHoldingStock[stockPanel.stockId] >= num) { //�ȸ�ŭ �ֽ��� �����ϰ� ���� ���� ����
						price = stockPanel.stock.getPrice()[MainPanel.dateId];
						total = price * num;
						myStock.havingMoney += total;
						myStock.numHoldingStock[stockPanel.stockId] -= num; //���� �ݾ� ����
						
						mainPanel.myHoldMoneylbl.setText(String.format("%,d", myStock.havingMoney) + " ��"); //���� �ݾ� ��� ����
						stockPanel.updateMyVal(); //StockPanel ����
					} //if
					
					stockPanel.setEmpty();
					
					mainPanel.myStockPanel.updateHoldStock(); //MyStockPanel ����
					mainPanel.myStockPanel.updatePage();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
					mainPanel.myStockPanel.updateMyVal();
				} else if(obj == stockPanel.backBt) { //StockPanel �ݱ�
						mainPanel.removeStockPanel();
				} else if(obj == stockPanel.dayBox) { //�׷��� ���� �޺��ڽ� Ŭ�� ��
					
					int time = stockPanel.dayBox.getSelectedIndex();
					
					if(time == 0) { //���� �Ϳ� ���� �׷����� ������ ����
						stockPanel.chartPanel.setDay = 1;
					} else if(time == 1) {
						stockPanel.chartPanel.setDay = 7;
					} else if(time == 2) {
						stockPanel.chartPanel.setDay = 30;
					} else if(time == 3) {
						stockPanel.chartPanel.setDay = 90;
					} //if else
					
					stockPanel.chartPanel.updateChart(); //�׷��� �ٽ� �׸�
					
				} //if else
			} //if
			
			
			//MyStockPanel
			if(myStockPanel != null) {
				if(obj == myStockPanel.exitBt) { //â �ݱ�
					mainPanel.myStockFrame.setVisible(false);
				} else if(obj == myStockPanel.nextBt) { //���̺� ���� ��ư
					myStockPanel.currentPage += 1;
					mainPanel.myStockPanel.updateHoldStock();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
				} else if(obj == myStockPanel.backBt) { //���̺� �ڷ� ��ư
					myStockPanel.currentPage -= 1;
					mainPanel.myStockPanel.updateHoldStock();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
				} //if else
			} //if
			
			
		} //actioinPerformed()
		
	} //SubButtonListener class
	
	public class textInputListener implements KeyListener { //���� �Ǵ� �Ǹ� �ؽ�Ʈ �ʵ忡 �Է��� ������ �� ���
		
		public void keyTyped(KeyEvent event) {}
		public void keyPressed(KeyEvent event) {}
		public void keyReleased(KeyEvent event) {
			Object obj = event.getSource();
			
			try {
				if(obj == stockPanel.buyfd) { //�� ����� ���� ���
					num = Integer.parseInt(stockPanel.buyfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					total = num * price;
					
					if(total > myStock.havingMoney) { //�� �� �ִ� �������� ������ �ִ밪���� �缳��
						num = myStock.havingMoney / price;
						stockPanel.buyfd.setText(num + "");
					} //if
					
					total = num * price;
					stockPanel.sumVallbl.setText(String.format("%,d", total) + " ��");
					stockPanel.sellfd.setText("");
				} else if(obj == stockPanel.sellfd) { //�� ����� ���� ���
					num = Integer.parseInt(stockPanel.sellfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					
					if(num > myStock.numHoldingStock[stockPanel.stockId]) { //�� �� �ִ� �������� ������ �ִ밪���� �缳��
						num = myStock.numHoldingStock[stockPanel.stockId];
						stockPanel.sellfd.setText(num + "");
					}
					
					total = num * price;
					stockPanel.sumVallbl.setText(String.format("%,d", total) + " ��");
					stockPanel.buyfd.setText("");
				}
			} catch (NumberFormatException e) { //�߸��� �Է��� �� ����
				stockPanel.setEmpty();
			} //try catch
		} //keyRealeased()
		
	} //textInputListener class
	
	public class TableListener implements MouseListener{ //���̺� ������ �� �ش� �ֽ� ���� â ����

		public void mouseClicked(MouseEvent event) {
			Object obj = event.getSource();
			
			if(obj == mainPanel.stockTable) { //MainPanel�� ���̺� Ŭ�� ��
				int stockId = (mainPanel.currentPage * 8) + mainPanel.stockTable.getSelectedRow();
				
				mainPanel.openStockPanel(mainPanel.realTimeStock, stockId, mainPanel.myStock);
				
			} else if(obj == myStockPanel.stockTable) { //MyStockPanel�� ���̺� Ŭ�� ��
				
				if(myStockPanel.totalHoldNum > 0) {
					int myStockId = (myStockPanel.currentPage * 8) + myStockPanel.stockTable.getSelectedRow();
					int stockId = myStockPanel.holdStockArr[myStockId];
					
					mainPanel.openStockPanel(mainPanel.realTimeStock, stockId, mainPanel.myStock);
				} //if
			} //if else
		} //mouseClicked()

		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	} //TableListener class
	
} //MainPanelListener class

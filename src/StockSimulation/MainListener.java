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
	
	public MainListener(StartPanel startPanel) { //StartPanel에서 사용할 리스너
		this.startPanel = startPanel;
		
		btnL = new ButtonListener();
	} //MainPanelListener()
	
	public MainListener(MainPanel mainPanel) { //MainPanel과 그 하위 에서 사용할 리스너
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
				if(obj == startPanel.startBt) { //시작
					startPanel.mainFrame.openMain();
					return;
				} else if(obj == startPanel.loadBt) { //불러오기
					startPanel.mainFrame.loadMain();
					return;
				} else if(obj == startPanel.exitBt) { //프로그램 종료
					System.exit(0);
				} //if .. else if
			} //if
			
			
			//MainPanel
			if(obj == mainPanel.nextBt) { //테이블의 다음 버튼 누를 때 테이블 갱신
				mainPanel.currentPage += 1;
				
				mainPanel.backBt.setVisible(true);
				if(mainPanel.currentPage == 2) {
					mainPanel.nextBt.setVisible(false);
				} //if
				
				for(int i=0; i<8; i++) {
					mainPanel.stockDTM.removeRow(0);
				} //for
				
				for(int i=0; i<8; i++) {
					int j = (mainPanel.currentPage * 8) + i; //stock 배열의 인덱스 값
					
					mainPanel.stockDTM.addRow(mainPanel.realTimeStock.getTableRow(j));
				} //for
				
			} else if(obj == mainPanel.backBt) { //테이블의 뒤로 버튼 누를 때 테이블 갱신
				mainPanel.currentPage -= 1;
				
				mainPanel.nextBt.setVisible(true);
				if(mainPanel.currentPage == 0) {
					mainPanel.backBt.setVisible(false);
				} //if
				
				for(int i=0; i<8; i++) {
					mainPanel.stockDTM.removeRow(0);
				} //for
				
				for(int i=0; i<8; i++) {
					int j = (mainPanel.currentPage * 8) + i; //stock 배열의 인덱스 값
					
					mainPanel.stockDTM.addRow(mainPanel.realTimeStock.getTableRow(j));
				} //for
				
			} else if(obj == mainPanel.myInfoBt) { //내정보 창
				mainPanel.myStockFrame.setVisible(true);
			} else if(obj == mainPanel.exitBt) { //종료 창
				mainPanel.mainExit();
			} //if else.. if
			
			
		} //actionPerformed()
		
	} //ButtonListener class
	
	public class SubButtonListener implements ActionListener{ //StockPanel, MyStockPanel의 버튼 리스너

		public void actionPerformed(ActionEvent event) {
			
			Object obj = event.getSource();
			
			//StockPanel
			if(stockPanel != null) {
				if(obj == stockPanel.buyBt || obj == stockPanel.buyfd) { //구매 버튼이나 엔터 입력 시
					num = Integer.parseInt(stockPanel.buyfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					total = price * num;
					if(myStock.havingMoney >= total) { //살 돈이 충분할 때만 실행
						myStock.havingMoney -= total;
						
						if(myStock.numHoldingStock[stockPanel.stockId] == 0) { //가장 처음 샀을 때의 가격을 저장
							myStock.startValStock[stockPanel.stockId] = price;
						} //if
						
						myStock.numHoldingStock[stockPanel.stockId] += num; //보유 개수 증가
						
						mainPanel.myHoldMoneylbl.setText(String.format("%,d", myStock.havingMoney) + " 원"); //보유 금액 출력 갱신
						stockPanel.updateMyVal(); //StockPanel 갱신
					} //if
					
					stockPanel.setEmpty();
					
					mainPanel.myStockPanel.updateHoldStock(); //MyStockPanel 갱신
					mainPanel.myStockPanel.updatePage();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
					mainPanel.myStockPanel.updateMyVal();
				} else if(obj == stockPanel.sellBt || obj == stockPanel.sellfd) { //판매 버튼이나 엔터 입력 시
					num = Integer.parseInt(stockPanel.sellfd.getText());
					
					if(myStock.numHoldingStock[stockPanel.stockId] >= num) { //팔만큼 주식을 보유하고 있을 때만 실행
						price = stockPanel.stock.getPrice()[MainPanel.dateId];
						total = price * num;
						myStock.havingMoney += total;
						myStock.numHoldingStock[stockPanel.stockId] -= num; //보유 금액 갱신
						
						mainPanel.myHoldMoneylbl.setText(String.format("%,d", myStock.havingMoney) + " 원"); //보유 금액 출력 갱신
						stockPanel.updateMyVal(); //StockPanel 갱신
					} //if
					
					stockPanel.setEmpty();
					
					mainPanel.myStockPanel.updateHoldStock(); //MyStockPanel 갱신
					mainPanel.myStockPanel.updatePage();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
					mainPanel.myStockPanel.updateMyVal();
				} else if(obj == stockPanel.backBt) { //StockPanel 닫기
						mainPanel.removeStockPanel();
				} else if(obj == stockPanel.dayBox) { //그래프 단위 콤보박스 클릭 시
					
					int time = stockPanel.dayBox.getSelectedIndex();
					
					if(time == 0) { //누른 것에 따라 그래프의 단위값 갱신
						stockPanel.chartPanel.setDay = 1;
					} else if(time == 1) {
						stockPanel.chartPanel.setDay = 7;
					} else if(time == 2) {
						stockPanel.chartPanel.setDay = 30;
					} else if(time == 3) {
						stockPanel.chartPanel.setDay = 90;
					} //if else
					
					stockPanel.chartPanel.updateChart(); //그래프 다시 그림
					
				} //if else
			} //if
			
			
			//MyStockPanel
			if(myStockPanel != null) {
				if(obj == myStockPanel.exitBt) { //창 닫기
					mainPanel.myStockFrame.setVisible(false);
				} else if(obj == myStockPanel.nextBt) { //테이블 다음 버튼
					myStockPanel.currentPage += 1;
					mainPanel.myStockPanel.updateHoldStock();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
				} else if(obj == myStockPanel.backBt) { //테이블 뒤로 버튼
					myStockPanel.currentPage -= 1;
					mainPanel.myStockPanel.updateHoldStock();
					mainPanel.myStockPanel.updateTable();
					mainPanel.myStockPanel.updateButton();
				} //if else
			} //if
			
			
		} //actioinPerformed()
		
	} //SubButtonListener class
	
	public class textInputListener implements KeyListener { //구매 또는 판매 텍스트 필드에 입력할 때마다 값 계산
		
		public void keyTyped(KeyEvent event) {}
		public void keyPressed(KeyEvent event) {}
		public void keyReleased(KeyEvent event) {
			Object obj = event.getSource();
			
			try {
				if(obj == stockPanel.buyfd) { //살 경우의 가격 계산
					num = Integer.parseInt(stockPanel.buyfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					total = num * price;
					
					if(total > myStock.havingMoney) { //살 수 있는 개수보다 많으면 최대값으로 재설정
						num = myStock.havingMoney / price;
						stockPanel.buyfd.setText(num + "");
					} //if
					
					total = num * price;
					stockPanel.sumVallbl.setText(String.format("%,d", total) + " 원");
					stockPanel.sellfd.setText("");
				} else if(obj == stockPanel.sellfd) { //팔 경우의 가격 계산
					num = Integer.parseInt(stockPanel.sellfd.getText());
					price = stockPanel.stock.getPrice()[MainPanel.dateId];
					
					if(num > myStock.numHoldingStock[stockPanel.stockId]) { //팔 수 있는 개수보다 많으면 최대값으로 재설정
						num = myStock.numHoldingStock[stockPanel.stockId];
						stockPanel.sellfd.setText(num + "");
					}
					
					total = num * price;
					stockPanel.sumVallbl.setText(String.format("%,d", total) + " 원");
					stockPanel.buyfd.setText("");
				}
			} catch (NumberFormatException e) { //잘못된 입력일 때 비우기
				stockPanel.setEmpty();
			} //try catch
		} //keyRealeased()
		
	} //textInputListener class
	
	public class TableListener implements MouseListener{ //테이블 눌렀을 때 해당 주식 정보 창 띄우기

		public void mouseClicked(MouseEvent event) {
			Object obj = event.getSource();
			
			if(obj == mainPanel.stockTable) { //MainPanel의 테이블 클릭 시
				int stockId = (mainPanel.currentPage * 8) + mainPanel.stockTable.getSelectedRow();
				
				mainPanel.openStockPanel(mainPanel.realTimeStock, stockId, mainPanel.myStock);
				
			} else if(obj == myStockPanel.stockTable) { //MyStockPanel의 테이블 클릭 시
				
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

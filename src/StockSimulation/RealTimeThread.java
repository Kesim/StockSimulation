package StockSimulation;

public class RealTimeThread implements Runnable{
	
	private int nDelayTime;
	private Thread myThread;
	private MainPanel mainPanel;
	private Boolean flag;
	
	public RealTimeThread(MainPanel mainPanel) {
		nDelayTime = 60 * 1000; //30초
		flag = false;
		
		this.mainPanel = mainPanel;
	} //RealTimeThread()
	
	public void start() { //쓰레드 시작
		
		if(myThread == null) {
			myThread = new Thread(this);
		} //if
		
		myThread.start();
		flag = true;
		
	} //start()
	
	public void stop() { //쓰레드 일시 중지
		flag = false;
		if(myThread != null) {
			myThread.suspend();
		} //if
	} //stop()

	@Override
	public void run() { //쓰레드 반복 실행
		try {
			while(true) {
				myThread.sleep(nDelayTime); //30초 멈춤
				
				MainPanel.updateDateId(); //MainPanel 갱신
				mainPanel.updateDate();
				mainPanel.realTimeStock.updateStocks();
				mainPanel.updateTable();
				
				if(mainPanel.stockPanel != null) { //StockPanel 갱신
					mainPanel.stockPanel.updateVal();
					mainPanel.stockPanel.updateMyVal();
					mainPanel.stockPanel.chartPanel.updateChart();
				} //if
				
				mainPanel.myStockPanel.updatePage(); //MyStockPanel 갱신
				mainPanel.myStockPanel.updateTable();
				mainPanel.myStockPanel.updateButton();
				mainPanel.myStockPanel.updateMyVal();
				
				int rand = (int)(Math.random() * 10);
				if(rand < 4) { //40퍼 확률로 주식 이벤트 생성
					mainPanel.eventText.makeNewEvent();
				} //if
			} //while
		} catch(Exception e) {}
	} //run()
	
	public void reRun() { //쓰레드 재 가동
		flag = true;
		myThread.resume();
	} //reRun()
} //RealTimeThread class

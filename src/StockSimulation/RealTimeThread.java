package StockSimulation;

public class RealTimeThread implements Runnable{
	
	private int nDelayTime;
	private Thread myThread;
	private MainPanel mainPanel;
	private Boolean flag;
	
	public RealTimeThread(MainPanel mainPanel) {
		nDelayTime = 60 * 1000; //30��
		flag = false;
		
		this.mainPanel = mainPanel;
	} //RealTimeThread()
	
	public void start() { //������ ����
		
		if(myThread == null) {
			myThread = new Thread(this);
		} //if
		
		myThread.start();
		flag = true;
		
	} //start()
	
	public void stop() { //������ �Ͻ� ����
		flag = false;
		if(myThread != null) {
			myThread.suspend();
		} //if
	} //stop()

	@Override
	public void run() { //������ �ݺ� ����
		try {
			while(true) {
				myThread.sleep(nDelayTime); //30�� ����
				
				MainPanel.updateDateId(); //MainPanel ����
				mainPanel.updateDate();
				mainPanel.realTimeStock.updateStocks();
				mainPanel.updateTable();
				
				if(mainPanel.stockPanel != null) { //StockPanel ����
					mainPanel.stockPanel.updateVal();
					mainPanel.stockPanel.updateMyVal();
					mainPanel.stockPanel.chartPanel.updateChart();
				} //if
				
				mainPanel.myStockPanel.updatePage(); //MyStockPanel ����
				mainPanel.myStockPanel.updateTable();
				mainPanel.myStockPanel.updateButton();
				mainPanel.myStockPanel.updateMyVal();
				
				int rand = (int)(Math.random() * 10);
				if(rand < 4) { //40�� Ȯ���� �ֽ� �̺�Ʈ ����
					mainPanel.eventText.makeNewEvent();
				} //if
			} //while
		} catch(Exception e) {}
	} //run()
	
	public void reRun() { //������ �� ����
		flag = true;
		myThread.resume();
	} //reRun()
} //RealTimeThread class

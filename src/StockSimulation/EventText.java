package StockSimulation;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventText {
	
	private int[] stockId;
	private EventData[] events;
	private MainPanel mainPanel;
	private RealTimeStock realTimeStock;
	private Calendar cal;
	private SimpleDateFormat sdf;
	
	public EventText(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel;
		
		stockId = new int[3];
		realTimeStock = mainPanel.realTimeStock;
		cal = Calendar.getInstance();
		cal.setTime(MainPanel.currentDate);
		sdf = new SimpleDateFormat("YYYY.MM.dd");
		
		//�̺�Ʈ�� ����
		events = new EventData[10];
		events[0] = new EventData(" ���� �ǹ��� ū ȭ�簡 �߻��߽��ϴ�. ", -10);
		events[1] = new EventData(" �翡�� �ҹ� ������ ���������ϴ�. ", -20);
		events[2] = new EventData(" �翡�� ���η� ���� ����ڰ� �߻��߽��ϴ�. ", -15);
		events[3] = new EventData(" �簡 ���η� ���� ǥâ�� �޾ҽ��ϴ�. ", 10);
		events[4] = new EventData(" �簡 �� ���� ������� �����Ǿ����ϴ�. ", 20);
		events[5] = new EventData(" �簡 ���� �ູ�� ������� �����Ǿ����ϴ�. ", 15);
		events[6] = new EventData(" ���� ������� �ټ� �����߽��ϴ�. ", 5);
		events[7] = new EventData(" ���� ������� �ſ� �����߽��ϴ�. ", 10);
		events[8] = new EventData(" ���� ������� �ټ� �����߽��ϴ�. ", -5);
		events[9] = new EventData(" ���� ������� �ſ� �����߽��ϴ�. ", -10);
		
	} //EventText()
	
	public void makeNewEvent() {
		
		String eventStr;
		Boolean flag = false;	
		int num = (int)(Math.random() * 3); //0~2
		int event;
		
		for(int i=0; i<num; i++) { //num�� ����ŭ�� �ֽ��� �̾� ������ �̺�Ʈ ����
			stockId[i] = (int)(Math.random() * 24);
			event = (int)(Math.random() * 10); //0~9 �̺�Ʈ �������� �̱�
			flag = false;
			
			while(flag != true) { //24���� �ֽ� �� �ߺ����� �ʰ� �̱�
				flag = true;
				for(int j=0; j<i; j++) {
					if(stockId[i] == stockId[j]) {
						stockId[i] = (int)(Math.random() * 24);
						flag = false;
						break;
					} else {
						flag = true;
					} //if else
				} //for
			} //while
			
			cal.setTime(MainPanel.currentDate); //���α׷��� ���� ��¥
			//����� ���ڿ� ����
			eventStr = realTimeStock.stocks[stockId[i]].getStockName() + events[event].event + sdf.format(cal.getTime()) + "\n";
			mainPanel.eventTextArea.insert(eventStr, 0); //MainPanel�� �̺�Ʈ textArea�� ù�ٿ� ����
			realTimeStock.stocks[stockId[i]].setEffectValue(events[event].effect); //�ֽ��� ����Ʈ�� ����

		} //for
		
	} //makeNewEvent()
	
} //EventText class

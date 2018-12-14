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
		
		//이벤트값 생성
		events = new EventData[10];
		events[0] = new EventData(" 사의 건물에 큰 화재가 발생했습니다. ", -10);
		events[1] = new EventData(" 사에서 불법 행적이 밝혀졌습니다. ", -20);
		events[2] = new EventData(" 사에서 과로로 인한 사망자가 발생했습니다. ", -15);
		events[3] = new EventData(" 사가 정부로 부터 표창을 받았습니다. ", 10);
		events[4] = new EventData(" 사가 이 달의 기업으로 선정되었습니다. ", 20);
		events[5] = new EventData(" 사가 가장 행복한 기업으로 선정되었습니다. ", 15);
		events[6] = new EventData(" 사의 매출액이 다소 증가했습니다. ", 5);
		events[7] = new EventData(" 사의 매출액이 매우 증가했습니다. ", 10);
		events[8] = new EventData(" 사의 매출액이 다소 감소했습니다. ", -5);
		events[9] = new EventData(" 사의 매출액이 매우 감소했습니다. ", -10);
		
	} //EventText()
	
	public void makeNewEvent() {
		
		String eventStr;
		Boolean flag = false;	
		int num = (int)(Math.random() * 3); //0~2
		int event;
		
		for(int i=0; i<num; i++) { //num의 값만큼의 주식을 뽑아 랜덤한 이벤트 생성
			stockId[i] = (int)(Math.random() * 24);
			event = (int)(Math.random() * 10); //0~9 이벤트 랜덤으로 뽑기
			flag = false;
			
			while(flag != true) { //24개의 주식 중 중복되지 않게 뽑기
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
			
			cal.setTime(MainPanel.currentDate); //프로그램상 현재 날짜
			//출력할 문자열 생성
			eventStr = realTimeStock.stocks[stockId[i]].getStockName() + events[event].event + sdf.format(cal.getTime()) + "\n";
			mainPanel.eventTextArea.insert(eventStr, 0); //MainPanel의 이벤트 textArea의 첫줄에 삽입
			realTimeStock.stocks[stockId[i]].setEffectValue(events[event].effect); //주식의 이펙트값 갱신

		} //for
		
	} //makeNewEvent()
	
} //EventText class

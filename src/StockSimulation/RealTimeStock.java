package StockSimulation;

import java.io.Serializable;

public class RealTimeStock implements Serializable {

	private static final long serialVersionUID = -3661441076277604708L;
	
	protected Stock[] stocks;
	
	public RealTimeStock() {
		stocks = new Stock[24];
		
		//24개 종목 초기화(종목명, 종류를 제외한 값은 랜덤)
		stocks[0] = new Stock("삼성 전자");
		stocks[1] = new Stock("LG 전자");
		stocks[2] = new Stock("경인 전자");
		stocks[3] = new Stock("대동 전자");
		stocks[4] = new Stock("주연 테크");
		
		stocks[5] = new Stock("동성 제약");
		stocks[6] = new Stock("환인 제약");
		stocks[7] = new Stock("하나 제약");
		stocks[8] = new Stock("삼진 제약");
		
		stocks[9] = new Stock("기아차");
		stocks[10] = new Stock("현대차");
		stocks[11] = new Stock("쌍용차");
		stocks[12] = new Stock("새론 오토모티브");
		stocks[13] = new Stock("금호 에이치티");
		stocks[14] = new Stock("현대 모비스");
		stocks[15] = new Stock("S&T 모티브");
		
		stocks[16] = new Stock("농심");
		stocks[17] = new Stock("오뚜기");
		stocks[18] = new Stock("롯데 제과");
		stocks[19] = new Stock("오리온");
		stocks[20] = new Stock("빙그레");
		stocks[21] = new Stock("크라운 제과");
		
		stocks[22] = new Stock("하이트진로");
		
		stocks[23] = new Stock("KR모터스");

	} //RealTimeStock()
	
	public String[] getTableRow(int stockId) { //테이블에 넣을 데이터로 반환

		String name = stocks[stockId].getStockName();
		String price = getPrice(stockId);
		String flucVal = getFlucVal(stockId);
		String flucPer = getFlucPer(stockId);
		
		String[] rowStr = {name, price, flucVal, flucPer};
		
		return rowStr;
		
	} //getTableRow()
	
	public void updateStocks() { //모든 주식값 갱신
		for(int i=0; i<24; i++) {
			stocks[i].updatePrice();
		}
	} //updateStocks()
	
	public String getPrice(int stockId) {
		
		int nowVal = stocks[stockId].getPrice()[MainPanel.dateId];
		
		String price = String.format("%,d", nowVal);
		
		return price;
	} //getPrice()
	
	public int getFluc(int stockId) {

		int beforeVal = stocks[stockId].getPrice()[MainPanel.getYesterday()];
		int nowVal = stocks[stockId].getPrice()[MainPanel.dateId];
		int fluc = nowVal - beforeVal;
		
		return fluc;
		
	} //getFluc
	
	public String getFlucVal(int stockId) { //이전 가격과의 차이 값 반환	
		String flucVal;
		
		int fluc = getFluc(stockId);
		
		if(fluc >= 0) flucVal = "▲" +  String.format("%,d", fluc);
		else flucVal = "▼" +  String.format("%,d", -fluc);
		
		return flucVal;
		
	} //getFlucVal
	
	public String getFlucPer(int stockId) { //이전 가격과의 차이 퍼센트 반환	
		String flucPer;
			
		int beforeVal = stocks[stockId].getPrice()[MainPanel.getYesterday()];
		int fluc = getFluc(stockId);
		
		double per = (double)fluc / beforeVal * 100;
		
		if(fluc >= 0) flucPer = "+" + String.format("%.2f", per) + "%";
		else flucPer = "-" + String.format("%.2f", -per) + "%";
		
		return flucPer;
	} //getFlucPer()
	
	public String getNumShare(int stockId) {
		int num = stocks[stockId].getNumShare();
		String share =  String.format("%,d", num);
		
		return share;
		
	} //getNumShare
	
	public String getFlucPerFromStart(int stockId, int startPrice) {
		int fluc = getFlucFromStart(stockId, startPrice);
		double per = (double)fluc / startPrice * 100;
		
		String flucPer;
		
		if(fluc >= 0) flucPer = "+" + String.format("%.2f", per) + "%";
		else flucPer = "-" + String.format("%.2f", -per) + "%";
		
		return flucPer;
	} //getFlucPerFromStart()
	
	public int getFlucFromStart(int stockId, int startPrice) {
		int nowVal = stocks[stockId].getPrice()[MainPanel.dateId];
		int fluc = nowVal - startPrice;
		
		return fluc;
	} //getFlucFromStart()
	
} //RealTimeStock class

package StockSimulation;

import java.io.Serializable;

public class Stock implements Serializable {

	private static final long serialVersionUID = -1625360316534376319L;
	
	private String stockName; //종목명
	private int[] price = new int[1095]; //가격
	private int numShare; //발행 주식수
	private double effectValue; //주식 성장 변수  실수형으로 -30~30값
	
	//get,set
	public String getStockName() {return this.stockName;}
	public int[] getPrice() {return this.price;}
	public int getNumShare() {return this.numShare;}
	public double getEffectValue() {return this.effectValue;}
	public void setEffectValue(int effect) {
		effectValue += effect;
		
		if(effectValue > 30) effectValue = 30;
		else if(effectValue < -30) effectValue = -30;
	} //setEffectValue()
	
	public Stock(String stockName) {
		
		MainPanel.dateId = (MainPanel.dateId - 90 + 1095) % 1095; //90일 전의 날짜 id값 계산
		
		this.stockName = stockName;
		price[MainPanel.dateId] = ((int)(Math.random() * 9501) + 500) * 10; //5,000 ~ 100,000
		numShare = ((int)(Math.random() * 9901) + 100) * 100; //10,000~1,000,000
		effectValue = 0.0;
		
		for(int i=0; i<90; i++) { //90일치의 값을 생성
			MainPanel.updateDateId();	
			updatePrice();
		} //for
		
		effectValue = (Math.random() * 61) - 30; // -30~30 초기값 갱신에 영향
		if(effectValue > 30.0) { //실수형이지만 최대값은 30.0
			effectValue = 30.0;
		} //if
		
	} //Stock()
	
	public void updatePrice() {
		
		int today = MainPanel.dateId;
		int yesterday = MainPanel.getYesterday();
		
		double fluctuation = (Math.random() * 41) - 20; //-20~20% 변동
		if(fluctuation > 20) {
			fluctuation = 20;
		} //if
		
		fluctuation += effectValue; //변동 값에 이펙트값 추가
		
		if(fluctuation > 30.0) { //최대 -30~30%로 제한
			fluctuation = 30.0;
		} else if(fluctuation < -30.0) {
			fluctuation = -30.0;
		} //if .. else if
		
		price[today] = (int) (price[yesterday] * (100.0 + fluctuation) / 100.0); //어제 가격에서 값 갱신
		
		price[today] += 50; //십의자리에서 반올림
		price[today] -= price[today] % 100;
		
		if(price[today] < 1000) price[today] = 1000; //가격 최저값
		
		updateEffect(); //이펙트값 갱신
	
	} //updatePrice()
	
	private void updateEffect() {
		
		if(effectValue > 20) { //이펙트 값에 따라 차등 갱신
			effectValue -= 6;
		} else if(effectValue > 10) {
			effectValue -= 4;
		} else if(effectValue > 0) {
			effectValue -= 2;
			if(effectValue < 0) effectValue = 0;
		} else if(effectValue > -10) {
			effectValue += 2;
			if(effectValue > 0) effectValue = 0;
		} else if(effectValue > -20) {
			effectValue += 4;
		} else if(effectValue > -30) {
			effectValue += 6;
		} //if .. else if
		
	} //updateEffect()
	
} //Stock class

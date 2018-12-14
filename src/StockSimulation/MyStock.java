package StockSimulation;

import java.io.Serializable;

public class MyStock implements Serializable{

	private static final long serialVersionUID = -5106823174879732250L;
	
	protected int[] numHoldingStock;
	protected int[] startValStock;
	protected int havingMoney;

	public MyStock() {
		
		numHoldingStock = new int[24];
		startValStock = new int[24];
		
		for(int i=0; i<24; i++) { //소유 주식 초기화
			numHoldingStock[i] = 0;
			startValStock[i] = 0;
		} //for
		
		havingMoney = 150000; //초기 자금
	} //MyStock()
	
	public String getMoneyStr() {
		String money = String.format("%,d", havingMoney);
		
		return money;
	} //getMoneyStr()
	
	public String getHoldStr(int stockId) {
		String hold = String.format("%,d", numHoldingStock[stockId]);
		
		return hold;
	} //getHoldStr()
	
} //MyStock class

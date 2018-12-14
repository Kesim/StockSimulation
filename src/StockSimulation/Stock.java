package StockSimulation;

import java.io.Serializable;

public class Stock implements Serializable {

	private static final long serialVersionUID = -1625360316534376319L;
	
	private String stockName; //�����
	private int[] price = new int[1095]; //����
	private int numShare; //���� �ֽļ�
	private double effectValue; //�ֽ� ���� ����  �Ǽ������� -30~30��
	
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
		
		MainPanel.dateId = (MainPanel.dateId - 90 + 1095) % 1095; //90�� ���� ��¥ id�� ���
		
		this.stockName = stockName;
		price[MainPanel.dateId] = ((int)(Math.random() * 9501) + 500) * 10; //5,000 ~ 100,000
		numShare = ((int)(Math.random() * 9901) + 100) * 100; //10,000~1,000,000
		effectValue = 0.0;
		
		for(int i=0; i<90; i++) { //90��ġ�� ���� ����
			MainPanel.updateDateId();	
			updatePrice();
		} //for
		
		effectValue = (Math.random() * 61) - 30; // -30~30 �ʱⰪ ���ſ� ����
		if(effectValue > 30.0) { //�Ǽ��������� �ִ밪�� 30.0
			effectValue = 30.0;
		} //if
		
	} //Stock()
	
	public void updatePrice() {
		
		int today = MainPanel.dateId;
		int yesterday = MainPanel.getYesterday();
		
		double fluctuation = (Math.random() * 41) - 20; //-20~20% ����
		if(fluctuation > 20) {
			fluctuation = 20;
		} //if
		
		fluctuation += effectValue; //���� ���� ����Ʈ�� �߰�
		
		if(fluctuation > 30.0) { //�ִ� -30~30%�� ����
			fluctuation = 30.0;
		} else if(fluctuation < -30.0) {
			fluctuation = -30.0;
		} //if .. else if
		
		price[today] = (int) (price[yesterday] * (100.0 + fluctuation) / 100.0); //���� ���ݿ��� �� ����
		
		price[today] += 50; //�����ڸ����� �ݿø�
		price[today] -= price[today] % 100;
		
		if(price[today] < 1000) price[today] = 1000; //���� ������
		
		updateEffect(); //����Ʈ�� ����
	
	} //updatePrice()
	
	private void updateEffect() {
		
		if(effectValue > 20) { //����Ʈ ���� ���� ���� ����
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

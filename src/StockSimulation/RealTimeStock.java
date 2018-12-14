package StockSimulation;

import java.io.Serializable;

public class RealTimeStock implements Serializable {

	private static final long serialVersionUID = -3661441076277604708L;
	
	protected Stock[] stocks;
	
	public RealTimeStock() {
		stocks = new Stock[24];
		
		//24�� ���� �ʱ�ȭ(�����, ������ ������ ���� ����)
		stocks[0] = new Stock("�Ｚ ����");
		stocks[1] = new Stock("LG ����");
		stocks[2] = new Stock("���� ����");
		stocks[3] = new Stock("�뵿 ����");
		stocks[4] = new Stock("�ֿ� ��ũ");
		
		stocks[5] = new Stock("���� ����");
		stocks[6] = new Stock("ȯ�� ����");
		stocks[7] = new Stock("�ϳ� ����");
		stocks[8] = new Stock("���� ����");
		
		stocks[9] = new Stock("�����");
		stocks[10] = new Stock("������");
		stocks[11] = new Stock("�ֿ���");
		stocks[12] = new Stock("���� �����Ƽ��");
		stocks[13] = new Stock("��ȣ ����ġƼ");
		stocks[14] = new Stock("���� ���");
		stocks[15] = new Stock("S&T ��Ƽ��");
		
		stocks[16] = new Stock("���");
		stocks[17] = new Stock("���ѱ�");
		stocks[18] = new Stock("�Ե� ����");
		stocks[19] = new Stock("������");
		stocks[20] = new Stock("���׷�");
		stocks[21] = new Stock("ũ��� ����");
		
		stocks[22] = new Stock("����Ʈ����");
		
		stocks[23] = new Stock("KR���ͽ�");

	} //RealTimeStock()
	
	public String[] getTableRow(int stockId) { //���̺� ���� �����ͷ� ��ȯ

		String name = stocks[stockId].getStockName();
		String price = getPrice(stockId);
		String flucVal = getFlucVal(stockId);
		String flucPer = getFlucPer(stockId);
		
		String[] rowStr = {name, price, flucVal, flucPer};
		
		return rowStr;
		
	} //getTableRow()
	
	public void updateStocks() { //��� �ֽİ� ����
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
	
	public String getFlucVal(int stockId) { //���� ���ݰ��� ���� �� ��ȯ	
		String flucVal;
		
		int fluc = getFluc(stockId);
		
		if(fluc >= 0) flucVal = "��" +  String.format("%,d", fluc);
		else flucVal = "��" +  String.format("%,d", -fluc);
		
		return flucVal;
		
	} //getFlucVal
	
	public String getFlucPer(int stockId) { //���� ���ݰ��� ���� �ۼ�Ʈ ��ȯ	
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

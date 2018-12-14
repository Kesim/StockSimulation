package StockSimulation;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame{
	
	protected StartPanel startPanel;
	protected MainPanel mainPanel;
	protected RealTimeStock realTimeStock;
	protected MyStock myStock;
	protected SaveData saveData;
	
	private final String[] savePath = {"RTStock.ser", "MyStock.ser", "SData.ser"}; //������ ���� ���
	
	public MainFrame() {
		
		startPanel = new StartPanel(this);
		
		this.setTitle("�ֽ� �ùķ��̼�");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
			
		this.getContentPane().add(startPanel);
		this.setSize(600,450);
		this.setLocationRelativeTo(null);
		
		this.pack();
		this.setVisible(true);

	} //SwitchPanel()
	
	public void openMain() { //�Ϲ����� ����
		mainPanel = new MainPanel();
		
		this.getContentPane().removeAll(); //StartPanel���� �� ����
		this.getContentPane().add(mainPanel);
		this.revalidate();
		this.repaint();
	} //openMain()
	
	public void loadMain() { //����� �����͸� �ҷ��ͼ� ����
		loadData();
	
		this.getContentPane().removeAll(); //StartPanel���� �� ����
		this.getContentPane().add(mainPanel);
		this.revalidate();
		this.repaint();
	} //openMain()
	
	public void loadData() { //����� ������ �ҷ�����
		
		try {
			ObjectInputStream inputStream;
			
			inputStream = new ObjectInputStream(new FileInputStream(savePath[0]));
			realTimeStock = (RealTimeStock)inputStream.readObject();
			inputStream.close();
			
			inputStream = new ObjectInputStream(new FileInputStream(savePath[1]));
			myStock = (MyStock)inputStream.readObject();
			inputStream.close();
			
			inputStream = new ObjectInputStream(new FileInputStream(savePath[2]));
			saveData = (SaveData)inputStream.readObject();
			inputStream.close();
			
			mainPanel = new MainPanel(realTimeStock, myStock, saveData); //�ҷ��� �����ͷ� ����
			
		} catch(Exception e) { //����� �����Ͱ� ���� ��� �Ϲ� ����
			JOptionPane.showMessageDialog(
					null, "����� �����Ͱ� �����ϴ�.\n������ ���� �����ϰڽ��ϴ�.", "�ҷ�����", JOptionPane.ERROR_MESSAGE
					);
			
			mainPanel = new MainPanel();
		}
		
	} //loadData()

} //SwitchPanel class

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
	
	private final String[] savePath = {"RTStock.ser", "MyStock.ser", "SData.ser"}; //저장할 파일 경로
	
	public MainFrame() {
		
		startPanel = new StartPanel(this);
		
		this.setTitle("주식 시뮬레이션");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
			
		this.getContentPane().add(startPanel);
		this.setSize(600,450);
		this.setLocationRelativeTo(null);
		
		this.pack();
		this.setVisible(true);

	} //SwitchPanel()
	
	public void openMain() { //일반적인 시작
		mainPanel = new MainPanel();
		
		this.getContentPane().removeAll(); //StartPanel삭제 후 갱신
		this.getContentPane().add(mainPanel);
		this.revalidate();
		this.repaint();
	} //openMain()
	
	public void loadMain() { //저장된 데이터를 불러와서 시작
		loadData();
	
		this.getContentPane().removeAll(); //StartPanel삭제 후 갱신
		this.getContentPane().add(mainPanel);
		this.revalidate();
		this.repaint();
	} //openMain()
	
	public void loadData() { //저장된 데이터 불러오기
		
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
			
			mainPanel = new MainPanel(realTimeStock, myStock, saveData); //불러온 데이터로 생성
			
		} catch(Exception e) { //저장된 데이터가 없을 경우 일반 시작
			JOptionPane.showMessageDialog(
					null, "저장된 데이터가 없습니다.\n데이터 없이 시작하겠습니다.", "불러오기", JOptionPane.ERROR_MESSAGE
					);
			
			mainPanel = new MainPanel();
		}
		
	} //loadData()

} //SwitchPanel class

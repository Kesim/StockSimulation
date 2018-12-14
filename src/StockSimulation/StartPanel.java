package StockSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPanel extends JPanel {

	private ImageIcon startImg;
	protected MainFrame mainFrame;
	protected JButton startBt, loadBt, exitBt;
	
	private MainListener mainL;
	
	public StartPanel(MainFrame mainFrame) {
		
		this.mainFrame = mainFrame;
		
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(600, 450));
		this.setLayout(null);
		
		mainL = new MainListener(this);
		
		startBt = new JButton("시 작");
		startBt.setBounds(410, 150, 120, 50);
		startBt.addActionListener(mainL.btnL);
		this.add(startBt);
		
		loadBt = new JButton("불러오기");
		loadBt.setBounds(410, 230, 120, 50);
		loadBt.addActionListener(mainL.btnL);
		this.add(loadBt);
		
		exitBt = new JButton("종 료");
		exitBt.setBounds(410, 310, 120, 50);
		exitBt.addActionListener(mainL.btnL);
		this.add(exitBt);
		
		startImg = new ImageIcon("images/startImage.png");
		
	} //StartPannel()
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		page.drawImage(startImg.getImage(), 0, 0, this);
	} //paintComponent()
	
} //StartPannel class

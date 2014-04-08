package home.s1.app;

import home.s1.alg.RecommendAlgorithm;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2569297494321956686L;
	private MainPanel myPanel;
	public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800, 600));
		myPanel = new MainPanel();
		this.add(myPanel);
		this.setVisible(true);
	}
	
	public RecommendAlgorithm.MLMethods getMethod() {
		return myPanel.getMethod();
	}
	public String getInputFilePath() {
		return myPanel.getInputFilePath();
	}
	
	public String getOutputFilePath() {
		return myPanel.getOutputFilePath();
	}
	
	public int getTrainingSize() {
		return myPanel.getTrainingSize();
	}
}

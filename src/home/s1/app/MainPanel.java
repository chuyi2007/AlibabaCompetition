package home.s1.app;

import home.s1.alg.RecommendAlgorithm;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4279736748888433985L;
	private JTextField txtInputFilePath;
	private JTextField txtOutputFilePath;
	private JTextField trainingSize;
	private JComboBox comboBox;
	private JTextArea textArea;
	
	public RecommendAlgorithm.MLMethods getMethod() {
		return (RecommendAlgorithm.MLMethods) comboBox.getSelectedItem();
	}
	
	public String getInputFilePath() {
		return txtInputFilePath.getText();
	}
	
	public String getOutputFilePath() {
		return txtOutputFilePath.getText();
	}
	
	public int getTrainingSize() {
		return Integer.parseInt(trainingSize.getText());
	}
	
	public MainPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{41, 232, 177, 84, 1, 0};
		gridBagLayout.rowHeights = new int[]{21, 21, 0, 0, 0, 16, 0, 16, 0, 39, 29, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		txtInputFilePath = new JTextField();
		//txtInputFilePath.setText("Input File Path ...");
		txtInputFilePath.setText("/Users/OwenShi/Dropbox/aliCompetition/data/t_alibaba_data_noch.csv");
		GridBagConstraints gbc_txtInputFilePath = new GridBagConstraints();
		gbc_txtInputFilePath.gridwidth = 3;
		gbc_txtInputFilePath.fill = GridBagConstraints.BOTH;
		gbc_txtInputFilePath.insets = new Insets(0, 0, 5, 5);
		gbc_txtInputFilePath.gridx = 0;
		gbc_txtInputFilePath.gridy = 0;
		add(txtInputFilePath, gbc_txtInputFilePath);
		
				JButton inputButton = new JButton("browse");
				inputButton.addActionListener(new OpenFile());
				GridBagConstraints gbc_inputButton = new GridBagConstraints();
				gbc_inputButton.anchor = GridBagConstraints.NORTH;
				gbc_inputButton.insets = new Insets(0, 0, 5, 5);
				gbc_inputButton.gridx = 3;
				gbc_inputButton.gridy = 0;
				add(inputButton, gbc_inputButton);
		
				txtOutputFilePath = new JTextField();
		txtOutputFilePath.setText("/Users/OwenShi/Dropbox/aliCompetition/data/shi_Alibaba_recommendation.txt");
		GridBagConstraints gbc_txtOutputFilePath = new GridBagConstraints();
		gbc_txtOutputFilePath.gridwidth = 3;
		gbc_txtOutputFilePath.fill = GridBagConstraints.BOTH;
		gbc_txtOutputFilePath.insets = new Insets(0, 0, 5, 5);
		gbc_txtOutputFilePath.gridx = 0;
		gbc_txtOutputFilePath.gridy = 1;
		add(txtOutputFilePath, gbc_txtOutputFilePath);
		
		
				JButton outputButton = new JButton("browse");
				outputButton.addActionListener(new SaveFile());
				GridBagConstraints gbc_outputButton = new GridBagConstraints();
				gbc_outputButton.insets = new Insets(0, 0, 5, 5);
				gbc_outputButton.gridx = 3;
				gbc_outputButton.gridy = 1;
				add(outputButton, gbc_outputButton);

		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.gridwidth = 3;
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 2;
		add(progressBar, gbc_progressBar);

		JLabel percentageLabel = new JLabel("0%");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 3;
		gbc_label_2.gridy = 2;
		add(percentageLabel, gbc_label_2);

		JLabel TrainingSetLabel = new JLabel("Training Months (inclusive upper bound)");
		GridBagConstraints gbc_lblInputFilePath = new GridBagConstraints();
		gbc_lblInputFilePath.anchor = GridBagConstraints.EAST;
		gbc_lblInputFilePath.insets = new Insets(0, 0, 5, 5);
		gbc_lblInputFilePath.gridx = 2;
		gbc_lblInputFilePath.gridy = 3;
		add(TrainingSetLabel, gbc_lblInputFilePath);

		trainingSize = new JTextField();
		trainingSize.setHorizontalAlignment(SwingConstants.CENTER);
		trainingSize.setText("6");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 3;
		add(trainingSize, gbc_textField);
		trainingSize.setColumns(10);
		AbstractDocument ad = (AbstractDocument) trainingSize.getDocument();
		ad.setDocumentFilter(new MyDocumentFilter());
		ad.setDocumentFilter(new MyDocumentFilter());
		
		comboBox = new JComboBox(RecommendAlgorithm.MLMethods.values());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 5;
		add(comboBox, gbc_comboBox);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 2;
		gbc_textArea.gridheight = 4;
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 7;
		add(textArea, gbc_textArea);

		JLabel lblMethod = new JLabel("Method: ");
		GridBagConstraints gbc_lblMethod = new GridBagConstraints();
		gbc_lblMethod.anchor = GridBagConstraints.EAST;
		gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblMethod.gridx = 2;
		gbc_lblMethod.gridy = 5;
		add(lblMethod, gbc_lblMethod);

		JButton btnRun = new JButton("Run");
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.gridwidth = 2;
		gbc_btnRun.fill = GridBagConstraints.BOTH;
		gbc_btnRun.gridheight = 4;
		gbc_btnRun.insets = new Insets(0, 0, 0, 5);
		gbc_btnRun.gridx = 2;
		gbc_btnRun.gridy = 7;
		add(btnRun, gbc_btnRun);
		btnRun.addActionListener(new RunAlgorithm());

		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridx = 4;
		gbc_label.gridy = 10;
		add(label, gbc_label);

		JLabel label_1 = new JLabel("");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.gridx = 4;
		gbc_label_1.gridy = 10;
		add(label_1, gbc_label_1);

	}
	
	class MyDocumentFilter extends DocumentFilter {
		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset,
				int length, String text, AttributeSet attrs)
						throws BadLocationException {
			int documentLength = fb.getDocument().getLength();
			if (isNumeric(text) && documentLength - length + text.length() <= 2)
				super.replace(fb, offset, length, text.toUpperCase(), attrs);
			else 
				showError();
		}

		private void showError() {
			JOptionPane.showMessageDialog(null, "Enter Integer between 0 and 100");
		}
		@Override
		public void insertString(FilterBypass fb, int offs,
				String str, AttributeSet a)
						throws BadLocationException {
			
			if (isNumeric(str))
				super.insertString(fb, offs, str, a);
			else
				Toolkit.getDefaultToolkit().beep();
			
			
		}
		
		private boolean isNumeric(String str)  
		{  
		  try  
		  {  
		    Integer d = Integer.parseInt(str);

			if (d <= 0 || d >= 100)
				return false;
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }
		  return true;  
		}
	}
	
	class OpenFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
		      // Demonstrate "Open" dialog:
		      int rVal = c.showOpenDialog(MainPanel.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		        txtInputFilePath.setText(c.getCurrentDirectory() 
		        		+ "/" + c.getSelectedFile().getName());
		      }
		}
	}
	
	class SaveFile implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	      JFileChooser c = new JFileChooser();
	      // Demonstrate "Save" dialog:
	      int rVal = c.showSaveDialog(MainPanel.this);
	      if (rVal == JFileChooser.APPROVE_OPTION) {
	        txtOutputFilePath.setText(c.getCurrentDirectory() 
	        		+ "/" + c.getSelectedFile().getName());
	      }
	    }
	  }
	
	class RunAlgorithm implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			MainController mc = new MainController(getInputFilePath(),
					getOutputFilePath(), getTrainingSize(), getMethod());
			String result = mc.run();
			textArea.setText(result);
		}
	}
}

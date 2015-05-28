package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DataField extends JPanel {
	JTextField tfData;
	JLabel lbName;
	public DataField(String label) {
		setLayout(new BorderLayout());
		
		lbName = new JLabel("  " + label + "  ");
		add(lbName, BorderLayout.WEST);
		tfData = new JTextField();
		add(tfData, BorderLayout.CENTER);
	}
	
	public String getData(){
		return tfData.getText();
	}
}

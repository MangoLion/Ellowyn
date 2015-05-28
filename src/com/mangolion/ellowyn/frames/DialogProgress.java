package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class DialogProgress extends JDialog{

	private final JPanel contentPanel = new JPanel();

	JLabel lbMessage;
	JProgressBar progressBar;
	JFrame desktopPane;
	
	private Component verticalStrut;
	
	/**
	 * Create the dialog.
	 * @param desktopPane 
	 */
	public DialogProgress(JFrame desktopPane) {
		super(desktopPane);
		//setModalityType(ModalityType.MODELESS);
		this.desktopPane = desktopPane;
		setBounds(100, 100, 450, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		lbMessage = new JLabel("Operations in Progress");
		contentPanel.add(lbMessage);
		
		verticalStrut = Box.createVerticalStrut(5);
		contentPanel.add(verticalStrut);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		contentPanel.add(progressBar);
		//pack();
		setLocationRelativeTo(desktopPane);
		//setVisible(true);
	}

	public void setProgress(int progress, String msg){
		setVisible(true);
		//lbMessage.setText(msg);
		setTitle(msg);
		progressBar.setValue(progress);
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(false);
		//pack();
	}
}

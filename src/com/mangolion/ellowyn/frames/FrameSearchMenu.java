package com.mangolion.ellowyn.frames;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;

import java.awt.Component;
import java.beans.PropertyVetoException;

import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLabel;

public class FrameSearchMenu extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameSearchMenu frame = new FrameSearchMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameSearchMenu() {
		setClosable(true);
		setResizable(true);
		try {
			setMaximum(true);
			//setIcon(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMaximizable(true);
		setIconifiable(true);
		setTitle("Advance Search");
		setBounds(100, 100, 609, 428);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.5);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);
		
		JPanel panel_2 = new JPanel();
		splitPane_1.setLeftComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollProject = new JScrollPane();
		panel_2.add(scrollProject, BorderLayout.CENTER);
		
		JCheckBox chckbxProjectList = new JCheckBox("Project list");
		scrollProject.setColumnHeaderView(chckbxProjectList);
		
		JPanel panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollExtension = new JScrollPane();
		panel_3.add(scrollExtension, BorderLayout.CENTER);
		
		JCheckBox chckbxExtensionList = new JCheckBox("Extension List");
		scrollExtension.setColumnHeaderView(chckbxExtensionList);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_4.add(scrollPane);
		
		JTextPane paneSearch = new JTextPane();
		scrollPane.setViewportView(paneSearch);
		
		JPanel panelReplace = new JPanel();
		panel.add(panelReplace);
		panelReplace.setLayout(new BorderLayout(0, 0));
		
		JCheckBox cbReplace = new JCheckBox("Replace");
		panelReplace.add(cbReplace, BorderLayout.NORTH);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panelReplace.add(scrollPane_1, BorderLayout.CENTER);
		
		JTextPane paneReplace = new JTextPane();
		scrollPane_1.setViewportView(paneReplace);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setResizeWeight(0.5);
		panel.add(splitPane_2);
		
		JPanel panel_5 = new JPanel();
		splitPane_2.setLeftComponent(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		
		JCheckBox cbObject = new JCheckBox("Object");
		panel_5.add(cbObject);
		
		JCheckBox cbAttr = new JCheckBox("Attribute");
		panel_5.add(cbAttr);
		
		JCheckBox cbStatement = new JCheckBox("Statement");
		panel_5.add(cbStatement);
		
		JPanel panel_6 = new JPanel();
		splitPane_2.setRightComponent(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		
		JCheckBox cbContains = new JCheckBox("Contains");
		panel_6.add(cbContains);
		
		JCheckBox cbFileName = new JCheckBox("File Name");
		panel_6.add(cbFileName);
		
		JCheckBox cbDualScreen = new JCheckBox("Use Dual Screen");
		panel_6.add(cbDualScreen);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btSearch = new JButton("Search");
		panel_1.add(btSearch);
		
		JButton btCancel = new JButton("Cancel");
		panel_1.add(btCancel);

	}

}

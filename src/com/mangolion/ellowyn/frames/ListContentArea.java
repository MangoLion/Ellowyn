package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ListContentArea extends JPanel {
	public JList<String> list;
	public DefaultListModel<String> model;
	public JButton btAdd, btRemove;
	public JPanel panelButtons;
	
	public ListContentArea() {
		setLayout(new BorderLayout());
		model = new DefaultListModel<>();
		list = new JList<String>(model);
		JScrollPane panelScroll = new JScrollPane(list);
		add(panelScroll, BorderLayout.CENTER);
		
		panelButtons = new JPanel(new FlowLayout());
		//panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.LINE_AXIS));
		btAdd = new JButton("Add");
		btRemove = new JButton("Remove");
		panelButtons.add(btAdd);
		panelButtons.add(btRemove);
		
		btRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i: list.getSelectedIndices())
					model.remove(i);
			}
		});
		
		add(panelButtons, BorderLayout.SOUTH);
	}
	
	public void showButtonsPanel(boolean show){
		panelButtons.setVisible(show);
	}
}

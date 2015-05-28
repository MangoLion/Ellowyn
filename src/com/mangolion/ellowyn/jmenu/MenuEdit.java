package com.mangolion.ellowyn.jmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.Projects;

public class MenuEdit extends JMenu {
	public MenuEdit() {
		super("Edit");

		JMenuItem mUndo = new JMenuItem("Undo");
		JMenuItem MCopy = new JMenuItem("Copy");
		JMenuItem mCut = new JMenuItem("Cut");
		JMenuItem mPaste = new JMenuItem("Paste");
		JMenuItem mVis = new JMenuItem("Visualize");

		add(mUndo);
		add(MCopy);
		add(mCut);
		add(mPaste);
		add(mVis);
		
		mVis.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EllowynProject project = Projects.projects.getFirst();
				
			}
		});
	}
}

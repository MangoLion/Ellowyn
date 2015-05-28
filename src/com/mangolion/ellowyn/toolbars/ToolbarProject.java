package com.mangolion.ellowyn.toolbars;

import images.Icons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.frames.FrameProjectTypeList;
import com.mangolion.ellowyn.main.EllowMain;

public class ToolbarProject extends JToolBar {
	public ToolbarProject(final FrameDesktop frameDesktop) {
		JButton btNewProj = new JButton();
		btNewProj.setIcon(Icons.newProjIcon);
		btNewProj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//EllowMain.createNewProject();
				String project = (new FrameProjectTypeList(EllowMain.getMainDesktop().getDesktopPane())).showDialog();
				if (project != null)
					EllowMain.createNewProject(project);
			}
		});
		add(btNewProj);

		JButton btOpen = new JButton();
		btOpen.setIcon(Icons.openIcon);
		add(btOpen);

		JButton btSave = new JButton();
		btSave.setIcon(Icons.saveIcon);
		add(btSave);
		btSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameDesktop.saveNote();
			}
		});

		JButton btSaveALL = new JButton();
		btSaveALL.setIcon(Icons.saveAllIcon);
		add(btSaveALL);
		btSaveALL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameDesktop.saveAllNote();
			}
		});
	}
}

package com.mangolion.ellowyn.jmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.utility.Utility;

public class MenuFile extends JMenu {
	public MenuFile(final FrameDesktop frameDesktop) {
		super("File");
		JMenu menuNewFile = new JMenu("New");
		add(menuNewFile);

		JMenuItem mSave = new JMenuItem("Save");
		add(mSave);
		mSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameDesktop.saveNote();
			}
		});

		JMenuItem mSaveAll = new JMenuItem("SaveAll");
		add(mSaveAll);
		mSaveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frameDesktop.saveAllNote();
			}
		});
		/*
		 * Class<?> clazz = Class.forName("com.foo.MyClass"); Constructor<?>
		 * constructor = clazz.getConstructor(String.class, Integer.class);
		 * Object instance = constructor.newInstance("stringparam", 42);
		 */
		for (Class<? extends EllowynProject> cl : Projects.projectTypes) {
			final String type = Utility.getInstance(cl).PROJECT_TYPE;
			JMenuItem mNewProject;
				mNewProject = new JMenuItem(type);

				mNewProject.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						EllowMain.createNewProject(type);
					}
				});
				menuNewFile.add(mNewProject);
		}

		JMenuItem mSwitchWS = new JMenuItem("Switch Workspace");
		mSwitchWS.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EllowMain.switchWorkspace("");
			}
		});
		add(mSwitchWS);

		JMenuItem mRefresh = new JMenuItem("Refresh Workspace");
		mRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EllowMain.getMainDesktop().tree.refreshWorkspace(false);
			}
		});
		add(mRefresh);

	}
}

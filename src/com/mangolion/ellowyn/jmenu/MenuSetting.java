package com.mangolion.ellowyn.jmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.frames.FramePluginList;
import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.theme.Themes;

public class MenuSetting extends JMenu {
	public MenuSetting(FrameDesktop desktop) {
		super("Setting");
		JMenu mTheme = new JMenu("Themes");
		JMenu mLight = new JMenu("Light");
		JMenu mDark = new JMenu("Dark");
		JMenuItem mPlugins = new JMenuItem("Plugins");
		JMenuItem mDual = new JMenuItem("Enable/Disable DualScreen");

		for (final String str : Themes.getLightThemeList()) {
			JMenuItem item = new JMenuItem(str);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Themes.setTheme(str);
				}
			});
			mLight.add(item);
		}

		for (final String str : Themes.getDarkThemeList()) {
			JMenuItem item = new JMenuItem(str);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Themes.setTheme(str);
				}
			});
			mDark.add(item);
		}

		mTheme.add(mLight);
		mTheme.add(mDark);
		add(mPlugins);
		add(mTheme);
		mPlugins.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FramePluginList list = new FramePluginList();
				EllowMain.getMainDesktop().addFrame(list, null);
			}
		});
		
		add(mDual);
		mDual.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EllowMain.toggleDualScreen();
			}
		});
	}
}

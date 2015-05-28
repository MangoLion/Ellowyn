package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import plugin.Function;
import plugin.Plugin;
import plugin.PluginInterFace;
import plugin.Plugins;

public class FramePluginList extends JInternalFrame {
	
	JTextArea tfDesc;
	JPanel pane;
	LinkedList<PluginInterFace> plugins;
	JList<String> listPlugin;
	JList<String> listFunc;
	DefaultListModel<String> modelFunc;
	LinkedList<Class<? extends Function>> functions;
	JTextArea tfFuncDesc;
	JPanel paneFunc;
	public FramePluginList() {
		super("Plugins", true, true, true, true);
		setSize(500, 350);
		pane = new JPanel(new BorderLayout());
		//pane.setDividerLocation(200);
		setContentPane(pane);
		listPlugin = new JList<String>();
		listPlugin.setPreferredSize(new Dimension(200, 350));
		DefaultListModel<String> modelPlugins = new DefaultListModel<String>();
		plugins = Plugins.getplugins();
		for (PluginInterFace plugin: plugins){
			modelPlugins.addElement(plugin.getName());
		}
		listPlugin.setModel(modelPlugins);
		pane.add(new JScrollPane(listPlugin), BorderLayout.WEST);
		tfDesc = new JTextArea();
		pane.add(tfDesc, BorderLayout.CENTER);
		tfDesc.setLineWrap(true);
		tfDesc.setPreferredSize(tfDesc.getPreferredSize());
		//JPanel rPane = new JPanel(new BorderLayout());
		
		listPlugin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PluginInterFace interFace = plugins.get(listPlugin.getSelectedIndex());
				tfDesc.setText(interFace.getDesc());
				functions = new LinkedList<Class<? extends Function>>();
				functions = interFace.getFunctions();
				
				paneFunc.setVisible(false);
				modelFunc.clear();
				for (Class<? extends Function> func : functions){
					modelFunc.addElement(Plugin.getInstance(func).getName());
					paneFunc.setVisible(true);
				}
				super.mouseClicked(e);
			}
		});
		
		paneFunc = new JPanel(new BorderLayout());
		paneFunc.setVisible(false);
		paneFunc.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		pane.add(paneFunc, BorderLayout.SOUTH);
		tfFuncDesc = new JTextArea();
		listFunc = new JList<String>();
		JScrollPane scrListFunc = new JScrollPane(listFunc);
		scrListFunc.setPreferredSize(new Dimension(150, 100));
		paneFunc.add(scrListFunc, BorderLayout.WEST);
		paneFunc.add(tfFuncDesc, BorderLayout.CENTER);
		tfFuncDesc.setPreferredSize(tfFuncDesc.getPreferredSize());
		tfFuncDesc.setLineWrap(true);
		modelFunc = new DefaultListModel<String>();
		listFunc.setModel(modelFunc);
		listFunc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String desc = Plugin.getInstance(functions.get(listFunc.getSelectedIndex())).getDesc();
				tfFuncDesc.setText(desc);
				super.mouseClicked(e);
			}
		});
		setVisible(true);
	}
}

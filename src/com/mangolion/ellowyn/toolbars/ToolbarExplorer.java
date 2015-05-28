package com.mangolion.ellowyn.toolbars;

import java.awt.BorderLayout;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.tree.EllowTree;

public class ToolbarExplorer extends JPanel {
	public EllowTree tree;
	public ToolbarExplorer(FrameDesktop desktopPane) {
		tree = new EllowTree(desktopPane);
		JScrollPane scrTree = new JScrollPane(tree);
		setLayout(new BorderLayout());
		add(scrTree);
		
		add(tree.cbCat, BorderLayout.NORTH);
	}
}

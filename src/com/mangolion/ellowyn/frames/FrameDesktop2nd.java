package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.panes.PaneView;
import com.mangolion.ellowyn.reference.ReferenceItem;

public class FrameDesktop2nd extends JFrame{

	LinkedList<JDesktopPane> desktopPanes;
	JTabbedPane tabbedPane;

	public FrameDesktop2nd() {
		setLayout(new BorderLayout());
		setSize(800, 600);
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		desktopPanes = new LinkedList<>();
		add(tabbedPane);
		addTab();
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if ((e.getSource() instanceof JTabbedPane)
						&& tabbedPane.getSelectedIndex() == tabbedPane
								.getTabCount() - 1)
					addTab();
			}
		});
	}

	public void addTab() {
		String name = JOptionPane.showInputDialog(tabbedPane,
				"Input new perspective name");
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPanes.add(desktopPane);

		if (tabbedPane.getTabCount() > 0) {
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
			tabbedPane.remove(tabbedPane.getTabCount() - 1);
		}
		tabbedPane.addTab(name, desktopPane);
		tabbedPane.addTab("+", null);
	}
	
	public void addComp(JComponent component){
		getDesktopPane().add(component);
	}

	public JDesktopPane getDesktopPane() {
		return (tabbedPane.getTabCount() > 0) ? desktopPanes.get(tabbedPane
				.getSelectedIndex()) : null;
	}

	public void addComp(ReferenceItem referenceItem) {
		for (Component component: referenceItem.component.getSubComponents()){
			JInternalFrame frame = new JInternalFrame();
			frame.setTitle(referenceItem.component.name);
			//frame.add((new PaneView(project, file))
		}
	}
}

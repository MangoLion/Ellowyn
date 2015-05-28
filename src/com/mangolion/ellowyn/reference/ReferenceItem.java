package com.mangolion.ellowyn.reference;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.panes.PaneView;

public class ReferenceItem {
	public Component component;
	public String type;
	public ReferenecItemListener listener;
	
	public ReferenceItem(Component component_) {
		this.component = component_;
		listener = new ReferenecItemListener() {

			@Override
			public void onItemTooltip(JFrame frameToolTip) {
				//System.out.println("tooltip component "+ component.name);
				JPanel tooltipPane = new JPanel(new BorderLayout());
				frameToolTip.setContentPane(tooltipPane);
				PaneView textPane = new PaneView();
				textPane.refresh(component.getPad(new LinkedList<Pad>()));
				JScrollPane pane = new JScrollPane(textPane);
				tooltipPane.add(pane);
				frameToolTip.pack();
			}
		};
	}
	
	public ReferenceItem(Component component, ReferenecItemListener listener_) {
		this.component = component;
		listener = listener_;
	}
	
	@Override
	public String toString() {
		return component.getReference();
	}
}

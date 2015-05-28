package com.mangolion.ellowyn.note;

import java.util.LinkedList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mangolion.ellowyn.frames.FrameNote;

public class NoteTabManager {
	private static LinkedList<Tab> tabs = new LinkedList<Tab>();
	
	public static void addTab(Tab tab){
		tabs.add(tab);
	}
	
	public static LinkedList<Tab> getTabs(){
		return tabs;
	}
	
	public static ChangeListener getListener(final FrameNote note){
		ChangeListener listener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane pane = (JTabbedPane) e.getSource();
				for (Tab tab: tabs){
					if (pane.getSelectedComponent() == tab.component)
						tab.listener.onTabSelected(note);
				}
			}
		};
		return listener;
	}

	public static void removeTab(Tab tab) {
		tabs.remove(tab);
	}
}

package com.mangolion.ellowyn.toolbars;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ToolbarMemory extends JToolBar {
	
	private JProgressBar pbMemory;
	public ToolbarMemory() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		pbMemory = new JProgressBar();
		pbMemory.setStringPainted(true);
		//pbMemory.setPreferredSize(new Dimension(250, 20));
		add(pbMemory);
		JButton btClean = new JButton("GC");
		add(btClean);
		btClean.setToolTipText("Call the Garbage Collector");
		btClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.gc();
				refresh();
			}
		});
		setToolTipText("JVM Memory Used");
		scheduledRefresh();
	}
	Timer timer;
	public void scheduledRefresh(){
	timer  = new Timer(100, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			refresh();
			timer.stop();
			scheduledRefresh();
		}
	});
	timer.start();
	}
	
	public void refresh(){
		float memFree =  Runtime.getRuntime().freeMemory();
	    float memTotal = Runtime.getRuntime().totalMemory();
	    pbMemory.setValue((int)((memTotal - memFree)/memTotal*100));
	    pbMemory.setString(String.valueOf((int)((memTotal - memFree)/1048576)) +"/" + String.valueOf((int)(memTotal/1048576)) + "M");
	}
}

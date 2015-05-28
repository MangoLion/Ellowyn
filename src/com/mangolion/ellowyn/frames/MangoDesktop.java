package com.mangolion.ellowyn.frames;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.Timer;

import com.mangolion.dynamicbackground.thread.DynamicThread;

public class MangoDesktop extends JDesktopPane{
	
	public DynamicThread thread;
	
	public MangoDesktop() {
		thread = new DynamicThread(this);
		thread.start();
		refreshFrames();
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		thread.draw(g);
	}
	
	public void refreshFrames(){
		Timer timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JInternalFrame frame: getAllFrames())
					if (frame instanceof FrameNote){
					FrameNote note = (FrameNote) frame;
					if (note.thread.settingDBF == null && !note.isOpaque())
						note.thread.setOpaque(thread.isOpaque);
					}
			}
		});
		timer.start();
	}
}

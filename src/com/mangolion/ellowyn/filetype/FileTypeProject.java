package com.mangolion.ellowyn.filetype;

import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameProjectEdit;
import com.mangolion.ellowyn.frames.FrameToolTip;
import com.mangolion.ellowyn.main.EllowMain;

import images.Icons;

public class FileTypeProject extends FileType {
	public FileTypeProject() {
		super("Project", false, false, false, Icons.txtIcon, new FileTypeListener() {
			
			@Override
			public void onToolTipHover(EllowFile file, FrameToolTip frameToolTip) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuggestion(EllowFile file) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onOpenFile(EllowFile file) {
				FrameProjectEdit edit = new FrameProjectEdit(file.parentProject);
				EllowMain.getMainDesktop().getDesktopPane().add(edit);
				edit.setVisible(true);
				System.out.println("called");
			}
			
			@Override
			public void onNewFile() {
				// TODO Auto-generated method stub
				
			}
		},  "project");
	}
}

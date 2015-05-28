package com.mangolion.ellowyn.project;

import com.mangolion.ellowyn.filetype.FileTypeNote;
import com.mangolion.ellowyn.progresssetter.ProgressSetter;

public class ProjectDefault extends EllowynProject{
	public static final String EXT_NOTE = "ellowyn";
	
	public ProjectDefault() {
		init();
	}
	
	public ProjectDefault(String name, ProgressSetter setter) {
		super(name, setter);
		init();
	}
	
	public void init(){
		PROJECT_TYPE = "Normal Project";
		//addExtensionforCompile(EXT_NOTE, Icons.noteIcon);
		addFileType(new FileTypeNote());
	}

}

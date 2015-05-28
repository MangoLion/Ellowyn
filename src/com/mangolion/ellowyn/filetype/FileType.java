package com.mangolion.ellowyn.filetype;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.file.EllowFile;

public class FileType {
	public boolean isCompiled, isWatched, userCreatable;
	public String type;
	public LinkedList<String> extensions = new LinkedList<String>();
	public ImageIcon icon;
	private  LinkedList<EllowFile> files = new LinkedList<EllowFile>();
	private LinkedList<EllowObject> objects = new LinkedList<EllowObject>();
	public FileTypeListener fileTypeListener;
	
	public FileType() {
		userCreatable = false;
		isCompiled = false;
		isWatched = false;
		type = "";
		extensions = null;
		icon = null;
		fileTypeListener = null;
	}
	
	public FileType( String type_,  boolean isCompiled_,boolean isWatched_, boolean userCreatable_,ImageIcon icon_, FileTypeListener fileTypeListener_, String ... ext) {
		isCompiled = isCompiled_;
		isWatched = isWatched_;
		userCreatable = userCreatable_;
		type = type_;
		icon = icon_;
		fileTypeListener = fileTypeListener_;
		
		if (ext != null){
			extensions.addAll(Arrays.asList(ext));
		}
	}

	public LinkedList<EllowFile> getFiles() {
		return files;
	}

	public void setFiles(LinkedList<EllowFile> files) {
		this.files = files;
	}

	public LinkedList<EllowObject> getObjects() {
		return objects;
	}

	public void setObjects(LinkedList<EllowObject> objects) {
		this.objects = objects;
	}
	
	public boolean checkExtension(String argExt){
		for (String ext: extensions){
			if (argExt.equals(ext))
				return true;
		}
		return false;
	}
}

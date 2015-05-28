package com.mangolion.ellowyn.extension;

import java.util.LinkedList;

import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.filetype.FileType;
import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.main.EllowMain;

public class ExtensionManager {
	/*private static HashMap<String[], FileTypeListener> map = new HashMap<String[], FileTypeListener>();
	public static void registerExtension(FileTypeListener listener, String ... exts ){
		map.put(exts, listener);
	}*/
	public static LinkedList<FileType> fileTypesGlobal = new LinkedList<FileType>();
	public LinkedList<FileType> fileTypes = new LinkedList<FileType>();
	
	public static void triggerToolTip(EllowFile file, FrameDesktop desktop) {
		String ext = file.getExtension();
		FileType type = getGlobalFileTypeforExt(ext);
		if (type != null)
			type.fileTypeListener.onToolTipHover(file, desktop.getToolTip());
		ExtensionManager manager = file.parentProject.extensionManager;
		//if (manager.getFileTypeforExt(ext).fileTypeListener != null)
			manager.getFileTypeforExt(ext).fileTypeListener.onToolTipHover(file, desktop.getToolTip());
	}
	
	public static void triggerOpenFile(EllowFile file) {
		String ext = file.getExtension();
		FileType type = getGlobalFileTypeforExt(ext);
		if (type != null)
			type.fileTypeListener.onOpenFile(file);
		if (file.parentProject == null){
			System.out.println("file " + file.getName() + " has no parent project");
			return;
		}
		ExtensionManager manager = file.parentProject.extensionManager;
		type = manager.getFileTypeforExt(ext);
		if (type != null && type.fileTypeListener != null)
			type.fileTypeListener.onOpenFile(file);
	}
	
	public static FileType getGlobalFileTypeforExt(String ext) {
		for (FileType type:  fileTypesGlobal)
			if (type.checkExtension(ext))
				return type;
		    return null;
	}
	
	public FileType getFileTypeforExt(String ext) {
		for (FileType type:  fileTypes)
			if (type.checkExtension(ext)){
				return type;
			}
		    return getGlobalFileTypeforExt(ext);
	}

	public static void addGlobalExtension(FileType fileTypeAudio) {
		fileTypesGlobal.add(fileTypeAudio);
	}
}

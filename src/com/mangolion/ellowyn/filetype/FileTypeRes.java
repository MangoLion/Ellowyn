package com.mangolion.ellowyn.filetype;

import images.Icons;

import com.mangolion.ellowyn.project.ProjectDefault;

public class FileTypeRes  extends FileType {
	public FileTypeRes() {
		super("Res File", true, false, false, Icons.txtIcon, null, ProjectDefault.EXT_RES);
	}
}

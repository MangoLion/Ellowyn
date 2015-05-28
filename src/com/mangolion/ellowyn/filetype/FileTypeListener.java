package com.mangolion.ellowyn.filetype;

import java.awt.event.MouseAdapter;
import java.io.File;

import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameToolTip;

public abstract class FileTypeListener {
	public void onToolTipHover(EllowFile file, FrameToolTip frameToolTip){};
	public void onSuggestion(EllowFile file){};
	public void onOpenFile(EllowFile file){};
	public void onNewFile(){};
	public void onPopupMenu(){};
}

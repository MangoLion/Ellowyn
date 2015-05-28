package com.mangolion.ellowyn.progresssetter;

public interface ProgressSetter {
	public void setDialogIndeterminate(boolean ind, String text);
	public void setDialogProgress (int progress, String msg);
	public void hideDialogProgress();
	public void setToolbarProgress(final int progress, final String text);
	public void setToobearIndterminate(boolean ind, String text);
	public void refreshToolbarMemory();
}

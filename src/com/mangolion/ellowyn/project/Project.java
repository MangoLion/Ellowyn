package com.mangolion.ellowyn.project;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.progresssetter.ProgressSetter;

public class Project {
	private String name;
	public LinkedList<EllowObject> objectsDefault = new LinkedList<EllowObject>();
	public LinkedList<Pad> padsDefault = new LinkedList<Pad>();
	public EllowFile resFile;
	public EllowFile projectFile;
	public String getProjectDir() {
		return projectDir;
	}

	private String projectDir = "";
	ProgressSetter progressSetter;
	
	public Project(String name, ProgressSetter setter) {
		this.progressSetter = setter;
		this.name = name;
		projectDir = Projects.workspace + "\\\\" + name;
		resFile = new EllowFile(Projects.workspace + "\\\\" + name + "\\\\"
				+ "R" + "\\\\" + "res.gen");
		projectFile = new EllowFile(Projects.workspace + "\\\\" + name + "\\\\"
				+ name + ".ellowyn");
	}

	EllowObject r;

	/**
	 * Rebuild the project by reloading from all of the .ellowyn files in it
	 * 
	 * @param project
	 *            the project to be rebuilded
	 */
	public void rebuild() {
		padsDefault.clear();
		objectsDefault.clear();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				Thread thread = new Thread(new Runnable() {
					float totalFiles = 1, currentFiles = 0;

					private void getProgress(File ellowFile) {
						totalFiles++;
						if (ellowFile.isDirectory())
							for (File file : ellowFile.listFiles()) {
								getProgress(file);
							}
					}

					@Override 
					public void run() {
						r = new EllowObject("R");
						EllowFile pfile = new EllowFile(Projects.workspace
								+ "\\" + name);
						getProgress(new File(Projects.workspace + "\\" + name));

						scanFiles(pfile);
						progressSetter.hideDialogProgress();
						String files = Writer.write(r);
						try {
							FileUtils.write(resFile, files);
							padsDefault.addAll(Reader.readString(FileUtils
									.readFileToString(resFile)));
							objectsDefault.addAll(Reader.read(padsDefault));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						progressSetter.setToolbarProgress(0, "Idling");
					}

					public void scanFiles(File pfile) {
						currentFiles++;
						progressSetter.setToolbarProgress((int) (currentFiles / totalFiles * 100), "Rebuilding " + name);


						if (pfile.listFiles() == null)
							return;
						try {
							for (File orgfile : pfile.listFiles()) {
								EllowFile file = new EllowFile(orgfile);

								if (file.getExtension().equals("ellowyn")) {
									String note = "";
									note = FileUtils.readFileToString(file);
									padsDefault.addAll(Reader.readString(note));
								} else {
									if (!file.getExtension().equals("project")
											&& !file.isDirectory()
											&& !file.getAbsolutePath().equals(
													resFile.getAbsolutePath())) {

										String fileName = file
												.getAbsolutePath()
												// NO IDEA why "\\" instead of
												// "\\\\"
												.replace(
														Projects.workspace
																+ "\\" + name,
														"")
												.replaceFirst("\\\\", "");
										r.attributes.add(new Attribute(r,
												fileName));
									}
								}

								if (file.isDirectory())
									scanFiles(file);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				);
				// TODO Auto-generated method stub
				thread.start();
				progressSetter.refreshToolbarMemory();
			}
			
		});
	
	}

}

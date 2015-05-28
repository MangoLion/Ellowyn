package com.mangolion.ellowyn.project;

import images.Icons;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.components.Statement;
import com.mangolion.ellowyn.extension.ExtensionManager;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.filetype.FileType;
import com.mangolion.ellowyn.filetype.FileTypeProject;
import com.mangolion.ellowyn.filetype.FileTypeRes;
import com.mangolion.ellowyn.filetype.FileTypeTXT;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.progresssetter.ProgressSetter;
import com.mangolion.ellowyn.utility.Utility;
import com.mangolion.ellowyn.values.Values;

public class EllowynProject {
	public static String EXT_PROJECT = "project", EXT_RES = "gen";

	public LinkedList<EllowynProject> referenceProjects = new LinkedList<EllowynProject>();
	public String name, category;

	public String PROJECT_TYPE = "Project";
	public EllowFile resFile;
	public EllowFile projectFile;
	public LinkedList<EllowObject> objectsR = new LinkedList<EllowObject>();
	public ExtensionManager extensionManager = new ExtensionManager();
	private String projectDir = "";
	ProgressSetter progressSetter;

	public EllowynProject() {
		// TODO Auto-generated constructor stub
	}

	public LinkedList<FileType> getFileTypes() {
		return extensionManager.fileTypes;
	}

	public EllowynProject(String name, ProgressSetter setter) {
		this.progressSetter = setter;
		this.name = name;
		projectDir = Projects.workspace + "\\\\" + name;
		resFile = new EllowFile(Projects.workspace + "\\\\" + name + "\\\\"
				+ "R" + "\\\\" + "res." + EXT_RES);
		projectFile = new EllowFile(Projects.workspace + "\\\\" + name + "\\\\"
				+ name + "." + EXT_PROJECT);
		addFileType(new FileTypeRes());
		addFileType(new FileTypeProject());
		addFileType(new FileTypeTXT());
	}

	public FileType getFileTypeforExt(String ext) {
		for (FileType type : getFileTypes())
			if (type.checkExtension(ext))
				return type;
		FileType typeGlobal = ExtensionManager.getGlobalFileTypeforExt(ext);
		if (typeGlobal != null && typeGlobal.checkExtension(ext))
			return typeGlobal;
		return new FileType();
	}

	public void addFileType(FileType type) {
		extensionManager.fileTypes.add(type);
	}

	public ImageIcon getIconforExt(String ext) {
		FileType type = getFileTypeforExt(ext);
		if (type != null)
			return type.icon;
		else
			return Icons.fileIcon;
	}

	public LinkedList<FileType> getCompileTypes() {
		LinkedList<FileType> results = new LinkedList<FileType>();

		for (FileType type : getFileTypes())
			if (type.isCompiled)
				results.add(type);
		return results;
	}

	public LinkedList<FileType> getWatchTypes() {
		LinkedList<FileType> results = new LinkedList<FileType>();

		for (FileType type : getFileTypes())
			if (type.isCompiled)
				results.add(type);
		return results;
	}

	public Collection<EllowObject> getObjectsfromExtension(String ext) {
		Collection<EllowObject> results = new LinkedList<EllowObject>();
		results.addAll(getFileTypeforExt(EllowynProject.EXT_RES).getObjects());
		results.addAll(getFileTypeforExt(ext).getObjects());
		return results;
	}

	public LinkedList<EllowFile> getFilesfromExtension(String ext) {
		LinkedList<EllowFile> results = new LinkedList<EllowFile>();
		results.addAll(getFileTypeforExt(EllowynProject.EXT_RES).getFiles());
		results.addAll(getFileTypeforExt(ext).getFiles());
		return results;
	}

	/**
	 * Rebuild the project by reloading from all of the .ellowyn files in it
	 * 
	 * @param project
	 *            the project to be rebuilded
	 */
	public void rebuild() {
	/*	EllowObject objectRef = new EllowObject("References");
		for (EllowynProject project: referenceProjects){
			Attribute attribute = new Attribute(objectRef, project.name);
			objectRef.addAttribute(attribute);
		}*/
		
	updateReferences();
		
		objectsR.clear();
		for (FileType type : getFileTypes()) {
			type.getFiles().clear();
			type.getObjects().clear();
			for (EllowynProject project: referenceProjects)
				for (String ext: type.extensions)
					type.getObjects().addAll(project.getObjectsfromExtension(ext));
		}
		
		
		SwingUtilities.invokeLater(new Runnable() {
			EllowObject r;

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

							LinkedList<Pad> padsTemp = new LinkedList<Pad>();
							// read from the R file
							padsTemp.addAll(Reader.readString(FileUtils
									.readFileToString(resFile)));
							// convert pads to objects
							objectsR.addAll(Reader.read(padsTemp));
						} catch (IOException e) {
							e.printStackTrace();
						}
						progressSetter.setToolbarProgress(0, "Idling");
					}

					public void scanFiles(File pfile) {
						currentFiles++;
						progressSetter.setToolbarProgress((int) (currentFiles
								/ totalFiles * 100), "Rebuilding " + name);

						if (pfile.listFiles() == null)
							return;
						try {
							for (File orgfile : pfile.listFiles()) {
								EllowFile file = new EllowFile(orgfile);
								if (file.isDirectory())// !
									scanFiles(file);

								boolean isUsed = false;
								for (FileType type : getFileTypes()) {
									if (type.checkExtension(file.getExtension())) {
										LinkedList<Pad> padsTemp = new LinkedList<Pad>();
										// JOptionPane.showMessageDialog(null,
										// "added" + file.getName());
										String note = "";
										note = FileUtils.readFileToString(file);
										padsTemp.addAll(Reader.readString(note));

										if (type.isCompiled)
											type.getObjects().addAll(
													Reader.read(padsTemp));
										if (type.isWatched)
											type.getFiles().add(file);
										isUsed = true;
									}
								}

								if (isUsed)
									continue;

								// only add to R file if no other extensions are
								// recognized
								if (!file.getExtension().equals(EXT_PROJECT)
										&& !file.getExtension().equals(EXT_RES)
										&& !file.isDirectory()) {

									String fileName = file
											.getAbsolutePath()
											// NO IDEA why "\\" instead
											// of
											// "\\\\"
											.replace(
													Projects.workspace + "\\"
															+ name, "")
											.replaceFirst("\\\\", "");
									r.attributes
											.add(new Attribute(r, fileName));
								}
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
	
	public void readReferences() {
		try {
			String read = FileUtils.readFileToString(projectFile);
			LinkedList<EllowObject> objects = Reader.read(Reader.readString(read));
			
			EllowObject objProject = objects.getFirst();
			EllowObject objectRef = objProject.getSubObject("References");
			if (objectRef != null){
				for (Attribute attribute: objectRef.attributes)
					Utility.updateList(referenceProjects, Projects.getProject(attribute.name));
			}
		}catch (Exception e){
			
		}
	}

public void updateReferences() {
		/*try {
			String read = FileUtils.readFileToString(projectFile);
			LinkedList<EllowObject> objects = Reader.read(Reader.readString(read));
			
			EllowObject objProject = objects.getFirst();
			EllowObject objectRef = objProject.getSubObject("References");
			if (objectRef == null){
				objectRef = new EllowObject("References");
				objProject.addChild(objectRef);
			}else{
				objectRef.attributes.clear();
			}
			for (EllowynProject project: referenceProjects){
				Attribute attribute = new Attribute(objectRef, project.name);
				objectRef.addAttribute(attribute);
			}
			
			String write = Writer.write(objects);
			FileUtils.write(projectFile, write);
		}catch (Exception e){
			
		}*/
	}

	
	public String getProjectDir() {
		return projectDir;
	}

	public LinkedList<Pad> getAllText() {
		// TODO Auto-generated method stub
		return null;
	}
}

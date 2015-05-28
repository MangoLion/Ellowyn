package com.mangolion.ellowyn.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.progresssetter.ProgressSetter;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.ProjectDefault;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.utility.Utility;

public class EllowFile extends File{	


	public EllowynProject parentProject;
	public boolean isProjectFile = false;

	public EllowFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public EllowFile(File file) {
		super(file,"");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}

	public String getExtension(){
		String extension = "";
		String fileName = getName();
		
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}
	
	public static String getExtension(File file){
		String extension = "";
		String fileName = file.getName();
		
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}


	public void deleteAll() {
		try {
			if (isDirectory())
				FileUtils.deleteDirectory(this);
			else
				delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void scanProjectInfo(ProgressSetter setter) {
		for (File file: listFiles()){
			if (getExtension(file).equals("project")){
				isProjectFile = true;
				String read;
				try {
					read = FileUtils.readFileToString(file);
					LinkedList<EllowObject> objects = Reader.read(Reader.readString(read));		
					EllowObject objProject = objects.getFirst();
					Attribute projectType = objProject.getAttribute("Project Type");
					String type = null;
					if (projectType != null)
						type = projectType.getStatement().trim();
					else{
						JOptionPane.showMessageDialog(null, "Project type of " + file.getParentFile().getName() + " not found: " + type + " using default project type.");
							type = "Normal Project";
					}
					Class<? extends EllowynProject> project = Projects.getProjectType(type);
					if (project == null){
						JOptionPane.showMessageDialog(null, "Project type of " + file.getParentFile().getName() + " not found: " + type + " using default project type.");
						type = "Normal Project";
						project = Projects.getProjectType(type);
					}
					
					Attribute attrName = objProject.getAttribute("Name");
					Attribute attrCategory = objProject.getAttribute("Category");
					String name = (attrName != null) ? attrName.getStatement().trim():getName().replace("." + getExtension(), "");
					String category = (attrCategory != null) ? attrCategory.getStatement().trim():"Normal Project";
					
					parentProject = Utility.getInstance(project, name, setter);
					parentProject.category = category;				
					
					Projects.addProject(parentProject);
					isProjectFile = true;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Icon getIconfromproject() {
		return parentProject.getIconforExt(getExtension());
	}
}

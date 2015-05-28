package com.mangolion.ellowyn.project;

import java.util.Collection;
import java.util.LinkedList;

import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.tree.EllowTree;
import com.mangolion.ellowyn.utility.Utility;

public class Projects {
	public static LinkedList<Class<? extends EllowynProject>> projectTypes = new LinkedList<>();
	/**
	 * The list of all projects in the workspace
	 */
	public static LinkedList<EllowynProject> projects = new LinkedList<EllowynProject>();
	
	/**
	 * The string for the path of Ellowyn's workspace
	 */
	public static String workspace = ""; 
	
	/**
	 * Rebuild all projects
	 */
	public static void rebuildAll(){
		EllowTree tree = EllowMain.getMainDesktop().tree;
		for (EllowynProject project : projects){
			if (!tree.categoryFilter.equals("All") && !tree.categoryFilter.equals(project.category)){
				System.out.println(tree.categoryFilter + "|" + project.category);
				continue;
			}
			project.readReferences();
			project.rebuild();
		}
	}
	
	public static EllowynProject getProject(String name){
		for (EllowynProject project: projects)
			if (project.name.equals(name))
				return project;
		return null;
	}
	
	public static void addProjectType(Class<? extends EllowynProject> project){
		projectTypes.add(project);
	}

	public static Class<? extends EllowynProject> getProjectType(String type) {
		for (Class<? extends EllowynProject> cl : Projects.projectTypes) {
			String ptype = Utility.getInstance(cl).PROJECT_TYPE;
			if (ptype.equals(type))
				return cl;
		}

		return null;
	}
	
	public static Collection<String> getAllProjectNames(){
		LinkedList<String> result = new LinkedList<String>();
		for (EllowynProject project: projects) {
			result.add(project.name);
		}
		return result;
}
	
	public static Collection<String> getAllProjectTypeNames(){
			LinkedList<String> result = new LinkedList<String>();
			for (Class<? extends EllowynProject> cl : Projects.projectTypes) {
				String ptype = Utility.getInstance(cl).PROJECT_TYPE;
				result.add(ptype);
			}
			return result;
	}

	public static void addProject(EllowynProject parentProject) {
		projects.add(parentProject);
	}
}

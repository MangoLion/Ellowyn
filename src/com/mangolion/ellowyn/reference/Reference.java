package com.mangolion.ellowyn.reference;

import java.util.Collection;
import java.util.LinkedList;

import plugin.Plugins;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.project.EllowynProject;

public class Reference {
	public static LinkedList<ReferenceItem> findSimilarRef(String find,
			Collection<EllowObject> objects) {
		LinkedList<ReferenceItem> results = new LinkedList<>();
		for (EllowObject obj : objects) {
			if (checkString(obj.name, find))
				results.add(new ReferenceItem(obj));
			for (Attribute attribute : obj.attributes) {
				if (checkString(attribute.name, find))
					results.add(new ReferenceItem(attribute)); 
				
			//	results.addAll(findSimilarRef(find, obj.childObjs));
			}
		}
		for (String str: Plugins.getAllFunctionNames())
			if (checkString(str, find))
				results.add(new ReferenceItem(new Component(str)));
		return results;
	}

	public static boolean checkString(String str, String str2) {
		return str.toLowerCase().trim().contains(str2.toLowerCase().trim());
	}

	public static Component findReferenceComponent(String string,
			EllowynProject argProject, String extension) {
		String str[] = string.split("'");
		int type = str.length;
		for (EllowObject obj : argProject.getObjectsfromExtension(extension)) {
			if (type == 1) {			
				if (obj.name.trim().equals(str[0].trim())){		//space after name bug detected!
					
					return obj;
				}
				else
					continue;
			}
			for (Attribute attribute : obj.attributes){
				if (type == 2) {
					if (attribute.name.trim().equals(str[1].trim()))//space after name bug detected!
						return attribute;
					else
						continue;
				}
			}
		}
		
		//search the project's references
		for (EllowynProject project: argProject.referenceProjects)
			return findReferenceComponent(string, project, extension);

		return null;
	}
}

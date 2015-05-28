package com.mangolion.ellowyn.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.project.EllowynProject;

public class FrameProjectEdit  extends IDEForm{
	EllowynProject project;
	String name, date, category, type;
	
	public FrameProjectEdit(EllowynProject project_) {
		super(project_.name + "'s info");
		project = project_;
		readProjectInfo(project);
		
		System.out.println(name);
		setDataField("Name", name);
		addDataField(new DataField("Date Created"), date);
		addDataField(new DataField("Project Type"), type);
		addDataField(new DataField("Category"), category);
		pack();
		
		btSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveProjectInfo(project);
				dispose();
			}
		});
	}

	public void saveProjectInfo(EllowynProject project) {
		name = getDataField("Name");
		type = getDataField("Project Type");
		date = getDataField("Date Created");
		category = getDataField("Category");
		
		EllowObject obj = new EllowObject("Project");
		
		obj.addAttribute("Name", name);
		obj.addAttribute("Project Type", type);
		obj.addAttribute("Date Created",date);
		obj.addAttribute("Category",category);

		String projectInfo = Writer.write(obj);
		try {
			FileUtils.write(project.projectFile, projectInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readProjectInfo(EllowynProject project) {
		String read = "";
		try {
			read = FileUtils.readFileToString( project.projectFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LinkedList<EllowObject> objects = Reader.read(Reader.readString(read));
		
		EllowObject objProject = objects.getFirst();
		name = objProject.getAttributeStr("Name");
		date = objProject.getAttributeStr("Date Created");
		category = objProject.getAttributeStr("Category");
		type = objProject.getAttributeStr("Project Type");
	}

}


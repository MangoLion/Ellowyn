package com.mangolion.ellowyn.main;

import images.Icons;
import iniwriter.IniWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.apache.commons.io.FileUtils;
import org.ini4j.Wini;

import plugin.PluginInterFace;
import plugin.Plugins;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameChooseWorkspace;
import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.frames.FrameDesktop2nd;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.project.ProjectDefault;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.theme.Themes;

public class EllowMain {
	public static String iniFile = "ellowyn.ini";
	public static EllowFile setting = new EllowFile(
			System.getProperty("user.dir") + "\\\\" + iniFile);
	
	public static EllowFile windowsFile = new EllowFile(
			System.getProperty("user.dir") + "\\\\" + "openwindows.txt");

	/**
	 * The frame that hold the Jdesktop, it is the main frame that this program
	 * will run on
	 */
	private static FrameDesktop desktop;
	private static FrameDesktop2nd desktop2nd;
	
	public static FrameDesktop2nd getSecondDesktop() {
		return desktop2nd;
	}
	
	public static FrameDesktop getMainDesktop() {
		return desktop;
	}

	public static void setDesktop(FrameDesktop desktop) {
		EllowMain.desktop = desktop;
	}
	static FrameChooseWorkspace frameWorkspace;

	public static void main(String[] args) {
		 // Create user dictionary in the current working directory of your application
        SpellChecker.setUserDictionaryProvider( new FileUserDictionary() );
        
        // Load the configuration from the file dictionaries.cnf and 
        // use the current locale or the first language as default
        // You can download the dictionary files from http://sourceforge.net/projects/jortho/files/Dictionaries/
        try {
			SpellChecker.registerDictionaries( new File(System.getProperty("user.dir") + "\\\\" + "dictionaries").toURI().toURL(), null );
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}



		
		Projects.addProjectType(ProjectDefault.class);
		
		// initialize and load icons
		new Icons();
		IniWriter.init();
		// apply the substance theme
		String theme = IniWriter.getTheme();
				Themes.setTheme(theme);
		// only start the workspace picker after the theme has been loaded
		SwingUtilities.invokeLater(new Runnable() {
			Timer timer;
			@Override
			public void run() {				
				switchWorkspace("");
				timer = new Timer(100, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						timer.stop();
					}
				});
				timer.start();

			}
		});
		
	}
	public static void initPlugin() {
		PluginManager  manager = PluginManagerFactory.createPluginManager();
		manager.addPluginsFrom(new EllowFile(System.getProperty("user.dir")+ "\\\\" + "mods.plugin").toURI());
		Collection<PluginInterFace> plugins =  new PluginManagerUtil(manager).getPlugins(PluginInterFace.class);
		Plugins.getplugins().addAll(plugins);
	}

	public static void switchWorkspace(String path) {
		if (desktop != null)
			desktop.dispose();
		if (frameWorkspace == null)
			frameWorkspace = new FrameChooseWorkspace(path);
		frameWorkspace.setVisible(true);
	}

	/**
	 * Create and show the desktop pane
	 */
	public static void showDesktop() {
		desktop = new FrameDesktop();
		desktop.setVisible(true);
	}

	/**
	 * Create a new Ellowyn Project, making a folder and a project file, then
	 * add it to the list of projects
	 * 
	 * @return whether or not the project was created successfully
	 */
	public static boolean createNewProject(String projectType) {
		String name = JOptionPane
				.showInputDialog("Please Enter New Project Name:");

		EllowFile file = new EllowFile(Projects.workspace + "\\\\" + name);
		if (file.exists() || name.equals(""))
			return false;

		file.mkdirs();
		file = new EllowFile(file.getAbsolutePath() + "\\\\" + name
				+ ".project");
		try {
			file.createNewFile();
			EllowObject obj = new EllowObject("Project");
			
			obj.addAttribute("Name", name);
			obj.addAttribute("Project Type", projectType);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			obj.addAttribute("Date Created", dateFormat.format(date));

			String projectInfo = Writer.write(obj);
			FileUtils.write(file, projectInfo);

			EllowFile resFile = new EllowFile(Projects.workspace + "\\\\"
					+ name + "\\\\" + "R");

			if (!resFile.exists()) {
				try {
					resFile.mkdirs();
					resFile = new EllowFile(Projects.workspace + "\\\\" + name
							+ "\\\\" + "R" + "\\\\" + "res.gen");
					resFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		desktop.tree.refreshWorkspace(false);
		return true;
	}

	public static void toggleDualScreen() {
		if (desktop2nd == null){
			desktop2nd = new FrameDesktop2nd();
			desktop2nd.setVisible(true);
		}else 
			desktop2nd.dispose();
	}

}

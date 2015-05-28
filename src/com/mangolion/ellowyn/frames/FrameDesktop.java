package com.mangolion.ellowyn.frames;

import images.Icons;
import iniwriter.IniWriter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.mangolion.dynamicbackground.setting.DefaultSettingDBF;
import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.extension.ExtensionManager;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.jmenu.MenuEdit;
import com.mangolion.ellowyn.jmenu.MenuFile;
import com.mangolion.ellowyn.jmenu.MenuSetting;
import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.progresssetter.ProgressSetter;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.toolbars.ToolbarExplorer;
import com.mangolion.ellowyn.toolbars.ToolbarMemory;
import com.mangolion.ellowyn.toolbars.ToolbarProject;
import com.mangolion.ellowyn.tree.EllowTree;
import com.mangolion.ellowyn.values.Values;

@SuppressWarnings("serial")
public class FrameDesktop extends JFrame implements ProgressSetter {
	public EllowTree tree;
	private MangoDesktop desktopPane;
	public MangoDesktop getDesktopPane() {
		return desktopPane;
	}

	public void setDesktopPane(MangoDesktop desktopPane) {
		this.desktopPane = desktopPane;
	}

	private FrameToolTip toolTip;
	public FrameToolTip getToolTip() {
		return toolTip;
	}

	public void setToolTip(FrameToolTip toolTip) {
		this.toolTip = toolTip;
	}

	private int winPaddling = 0;
	private DialogProgress dialogProgress;
	private JProgressBar pbToolbar = new JProgressBar();
	private ToolbarMemory tbMemory;

	/**
	 * The code suggestion frame
	 */
	public FrameSuggestion frameSuggestion;
	public FrameDesktop() {
		dialogProgress = new DialogProgress(this);
		dialogProgress.setProgress(0, "Loading GUI");
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						init();
						//desktopPane.add(new TestFrame());
						}
				});
	}
	public MenuSetting mSetting;
	Timer timer, timer2;
	
	void init(){
		// TODO Auto-generated method stub
		dialogProgress.setProgress(40, "Loading GUI");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Ellowyn");
		setSize(new Dimension(800, 600));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setIconImage(Icons.orbIcon.getImage());

		// the outer borderlayout, i used border layout to put toolbars
		// in
		JPanel contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);

		// the split pane used to hold the desktop pane and the explorer
		// (Tree)
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.1);
		contentPane.add(splitPane);

		desktopPane = new MangoDesktop();
		JScrollPane scrDesktop = new JScrollPane(desktopPane);
		splitPane.setRightComponent(scrDesktop);

		ToolbarProject toolBar = new ToolbarProject(FrameDesktop.this);
		contentPane.add(toolBar, BorderLayout.NORTH);

		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tbMemory = new ToolbarMemory();
		bottom.add(tbMemory);
		JToolBar tbProgress = new JToolBar();
		bottom.add(tbProgress);
		tbProgress.add(pbToolbar);
		pbToolbar.setToolTipText("Operations in Progress");
		tbProgress.setSize(new Dimension(250, 50));
		pbToolbar.setStringPainted(true);
		contentPane.add(bottom, BorderLayout.SOUTH);

		frameSuggestion = new FrameSuggestion();
		toolTip = new FrameToolTip(FrameDesktop.this);

		/*tree = new EllowTree(FrameDesktop.this);
		JScrollPane scrTree = new JScrollPane(tree);*/
		ToolbarExplorer tbExplorer = new ToolbarExplorer(this);
		tree = tbExplorer.tree;
		splitPane.setLeftComponent(tbExplorer);

		dialogProgress.setProgress(50, "Loading GUI");
		// create the menu bar and menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		MenuFile menuFile = new MenuFile(FrameDesktop.this);
		menuBar.add(menuFile);

		MenuEdit mnEdit = new MenuEdit();
		menuBar.add(mnEdit);

		mSetting = new MenuSetting(FrameDesktop.this);
		mSetting.add(desktopPane.thread.getMenu());
		menuBar.add(mSetting);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				IniWriter.setDynamicBackground( String.valueOf(desktopPane.thread.settingDBF));
				LinkedList<String> openWindows = new LinkedList<String>();
				for (JInternalFrame iframe: desktopPane.getAllFrames())
					if (iframe instanceof FrameNote)
						openWindows.add(((FrameNote)iframe).getFile().getAbsolutePath());
				IniWriter.saveOpenWindows(openWindows);
				
				for (JInternalFrame frame : desktopPane.getAllFrames()) {
					//System.out.println("closing");
					if (frame instanceof FrameNote
							&& ((FrameNote) frame).isUnsaved()) {
						if (JOptionPane
								.showConfirmDialog(desktopPane,
										"Are you sure? There are unsaved notes!") == JOptionPane.YES_OPTION)
							System.exit(0);
						else
							return;
					}
				}
				System.exit(0);
				super.windowClosing(e);
			}
		});
		
		/*
		 * StandalonePlayer player = StandalonePlayer.start();
		 * desktopPane.add(player); player.revalidate();
		 */
		dialogProgress.setProgress(0, "Loading Plugins");
		EllowMain.initPlugin();
		
		desktopPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)){
					JPopupMenu menu = new JPopupMenu();
					JMenuItem mCloseAll = new JMenuItem("Close All Windows");
					mCloseAll.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							for (JInternalFrame frame: desktopPane.getAllFrames())
								frame.dispose();
						}
					});
					menu.add(mCloseAll);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
				super.mouseClicked(e);
			}
		});
		
	/*	timer2 = new Timer(0, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setDialogIndeterminate(true, "Finalizing");
			}
		});
		
		timer2.start();
		
		timer = new Timer(3500, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogProgress.setVisible(false);
				String dbf =IniWriter.getDynamicBackground();
				if (!dbf.equals("null")){
					desktopPane.thread.setSettingDbf(DefaultSettingDBF.getTheme(dbf));
				}
				timer.stop();
				timer2.stop();
			}
		});
		timer.start();*/
		
	}

	public void saveAllNote() {
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (!(frame instanceof FrameNote))
				continue;
			FrameNote note = (FrameNote) frame;
			note.saveNote();
		}
		Projects.rebuildAll();
	}

	public void saveNote() {
		FrameNote note = (FrameNote) desktopPane.getSelectedFrame();
		if (note == null)
			return;
		note.saveNote();
		note.getFile().parentProject.rebuild();
	}

	public void openFile(EllowFile file) {
		if (!file.isFile())
			return;
		ExtensionManager.triggerOpenFile(file);
		String ext = file.getExtension();
		if (Values.isReadable(ext)) {
			for (JInternalFrame frame : desktopPane.getAllFrames()) {
				if (!(frame instanceof FrameNote))
					continue;
				FrameNote note = (FrameNote) frame;
				if (note.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
					frame.toFront();
					note.paneEdit.requestFocus();
					return;
				}
			}
			createFrameNote(file);
		}
	}

	public void openFileAsNote(EllowFile file) {
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (!(frame instanceof FrameNote))
				continue;
			FrameNote note = (FrameNote) frame;
			if (note.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
				frame.toFront();
				note.paneEdit.requestFocus();
				return;
			}
		}
		createFrameNote(file);
	}
	
	public void openFileAsPlainText(EllowFile file) {
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (!(frame instanceof FrameNote))
				continue;
			FrameNote note = (FrameNote) frame;
			if (note.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
				frame.toFront();
				note.paneEdit.requestFocus();
				return;
			}
		}
		FrameNote frameNote = createFrameNote(file);
		frameNote.tabPane.setSelectedComponent(frameNote.scrPlainText);
	}
	
	public FrameNote createFrameNote(EllowFile file) {
		FrameNote frameNote = new FrameNote(file, this);
		desktopPane.add(frameNote);
		frameNote.setVisible(true);
		frameNote.updateUI();
		frameNote.setLocation(winPaddling, winPaddling);
		frameNote.setSize(FrameNote.DEFWIDTH,
				(int) (desktopPane.getHeight() * 0.7));
		winPaddling += 25;
		return frameNote;
	}
	
	/**
	 * Finds all frames which' files' directory belong to the para file and close them
	 * @param file
	 */
	public void closeFrames(File file) {
		if (file.isDirectory())
			for (File subfile : file.listFiles())
				closeFrames(subfile);
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (!(frame instanceof FrameNote))
				continue;
			FrameNote note = (FrameNote) frame;
			if (note.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				note.dispose();
			if (note.getFile().isDirectory())
				for (File subfile : file.listFiles())
					closeFrames(subfile);

		}
	}

	public void showProjectInfo(EllowynProject project) {
		FrameProjectEdit frame = new FrameProjectEdit(project);
		desktopPane.add(frame);
		frame.setVisible(true);
		frame.setLocation(winPaddling, winPaddling);
		winPaddling += 25;
	}
	
	public void showProjectReferences(EllowynProject project) {
		FrameProjectReference frame = new FrameProjectReference(project);
		desktopPane.add(frame);
		frame.setVisible(true);
		frame.setLocation(winPaddling, winPaddling);
		winPaddling += 25;
	}

	public void showToolTip(int x, int y, Component component, EllowynProject project, EllowFile file) {
		if (toolTip.getComponent() != component){
			toolTip.setText(component, project, file);
			toolTip.setLocation(x, y);
		}
		toolTip.setVisible(true);
	}

	public void hideAllPopup() {
		frameSuggestion.setVisible(false);
		toolTip.setVisible(false);
	}

	public void findFocus(java.awt.Component parent) {
		for (java.awt.Component component : ((Container) parent)
				.getComponents()) {
			//if (component.isFocusOwner())
				//System.out.println(String.valueOf(component));
			findFocus(component);
		}
	}


	@Override
	public void setDialogProgress(final int progress, final String msg) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (!dialogProgress.isVisible())
					dialogProgress.setVisible(true);
				dialogProgress.setProgress(progress, msg);
				// dialogProgress.revalidate();
			}
		});
	}
	
	public void setDialogProgress(final int progress, final String msg, String msg2) {
		setDialogProgress(progress, msg);
		dialogProgress.lbMessage.setText(msg2);
	}

	@Override
	public void hideDialogProgress() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (dialogProgress != null)
					dialogProgress.setVisible(false);
			}
		});

	}
	
	public void hideToolTip(){
		toolTip.setVisible(false);
	}

	@Override
	public void setToolbarProgress(final int progress, final String string) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				pbToolbar.setValue(progress);
				pbToolbar.setString(string);
				pbToolbar.setIndeterminate(false);
			}
		});

	}

	/*public void showImageFile(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			showImage(image, file.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public void setDialogIndeterminate(boolean ind, String text) {
		if (!dialogProgress.isVisible())
			dialogProgress.setVisible(true);
		dialogProgress.progressBar.setIndeterminate(ind);
		dialogProgress.progressBar.setString(text);
		dialogProgress.lbMessage.setText(text);
		// dialogProgress.repaint();
	}

	@Override
	public void setToobearIndterminate(boolean ind, String text) {
		pbToolbar.setIndeterminate(ind);
		pbToolbar.setString(text);
	}

	@Override
	public void refreshToolbarMemory() {
		tbMemory.refresh();
	}
	
	public void addFrame(JInternalFrame frame, JInternalFrame parent){
		desktopPane.add(frame);
		frame.setVisible(true);
		if (parent != null)
			frame.setLocation(parent.getX(), parent.getY());
		frame.toFront();
	}
}

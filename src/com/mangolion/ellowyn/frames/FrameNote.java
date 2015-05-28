package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.commons.io.FileUtils;

import com.inet.jortho.SpellChecker;
import com.inet.jortho.SpellCheckerOptions;
import com.mangolion.dynamicbackground.component.ListDBF;
import com.mangolion.dynamicbackground.component.TransparentPane;
import com.mangolion.dynamicbackground.thread.DynamicThread;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.note.NoteTabManager;
import com.mangolion.ellowyn.note.Tab;
import com.mangolion.ellowyn.panes.DDPanel;
import com.mangolion.ellowyn.panes.PaneEdit;
import com.mangolion.ellowyn.panes.PanePlainText;
import com.mangolion.ellowyn.panes.PaneView;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.reference.Reference;
import com.mangolion.ellowyn.reference.ReferenceItem;

@SuppressWarnings("serial")
public class FrameNote extends JInternalFrame {
	/**
	 * The jtextpane used for editing the code
	 */
	public PaneEdit paneEdit;
	/**
	 * The jtextpane used for viewing the code
	 */
	public PaneView paneView;
	
	/**
	 * the jtextpane used for viewing the note's file's text
	 */
	public PanePlainText panePlainText;
	
	/**
	 * The list of pads that separate the words from the keywords
	 */
	public LinkedList<Pad> pads = new LinkedList<>();
	/**
	 * The list object for this note
	 */
	private LinkedList<EllowObject> objects = new LinkedList<>();
	/**
	 * The default width and height of the frame
	 */
	public static int DEFWIDTH = 600, DEFHEIGHT = 450;
	
	private FrameNote self = this;

	private boolean unsaved;

	/**
	 * The file for this note for saving and loading
	 */
	private EllowFile file;
	
	/**
	 * The content of the note's file, <b>it is only updated when the note is saved/opened.</b>, 
	 */
	private String text;

	public FrameSuggestion frameSuggestion;

	public FrameDesktop desktop;
	
	public void createPaneEdit(boolean spellCheck){
		// create the edit jtextpane
		paneEdit = new PaneEdit(this);
		
		// read from the text file
				if (file != null) {
					try {
						text = FileUtils.readFileToString(file);
						paneEdit.setText(text);
						refresh();
						setIsUnsaved(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
        // enable the spell checking on the text component with all features
		if (spellCheck){
			SpellChecker.register( paneEdit );
        	SpellCheckerOptions options = SpellChecker.getOptions();
        	options.setIgnoreCapitalization(true);
		}
		System.out.println(paneEdit);
	}

	public FrameNote(EllowFile file, final FrameDesktop desktop) {
		super("Note Name", true, true, true, true);
		getInputMap().clear();
		this.desktop = desktop;
		this.frameSuggestion = desktop.frameSuggestion;
		this.file = file;
		init();

		// there is a bug that makes the max and min button not work for the
		// first click, fix it by manually setting it to no current minimized
		// and no maximized mode
		try {
			setMaximum(false);
			setIcon(false);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameIconified(InternalFrameEvent e) {
				desktop.hideAllPopup();
				super.internalFrameIconified(e);
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				if (unsaved) {
					int returnVal = JOptionPane.showConfirmDialog(
							self,
							"This note is unsaved, save changes?");
					if (returnVal == JOptionPane.YES_OPTION) {
						saveNote();
					} else if (returnVal == JOptionPane.CANCEL_OPTION)
						return;
					dispose();
				} else
					dispose();

				desktop.hideAllPopup();
			}
		});
	}

	public String getText() {
		if (file != null) {
			try {
				text = FileUtils.readFileToString(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
	
	public JPanel paneSetting;

	/**
	 * Save the plain text into the file. this method is called only in the plain text editor
	 * @param text
	 */
	public void saveNote(String text) {
		try {
			FileUtils.write(file, text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	JScrollPane scrEdit, scrView, scrPlainText;
	JTabbedPane tabPane;
	public TransparentPane contentPane;
	public DynamicThread thread;
	ListDBF dbf;

	public void init() {
		thread = new DynamicThread(this);
		thread.settingDBF = null;
		thread.start();



		// create the view jtextpane
		paneView = new PaneView();
		scrView = new JScrollPane(paneView);
		
		createPaneEdit(true);
		scrEdit = new JScrollPane(paneEdit);
		
		// create the plain text jtextpane
		panePlainText = new PanePlainText(this);
		scrPlainText = new JScrollPane(panePlainText);

		paneSetting = new JPanel();
		paneSetting.setLayout(new FlowLayout());
		dbf = new ListDBF(thread);
		paneSetting.add(dbf);
		final JCheckBox cbSpellCheck = new JCheckBox("Spell Checking");
		cbSpellCheck.setSelected(true);
		cbSpellCheck.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				createPaneEdit(cbSpellCheck.isSelected());
				scrEdit.setViewportView(paneEdit);
			}
		});
		paneSetting.add(cbSpellCheck);

		tabPane = new JTabbedPane();
		tabPane.addTab("Edit", scrEdit);
		tabPane.addTab("View", scrView);
		tabPane.addTab("Plain Text", scrPlainText);
		final DDPanel ddPanel = new DDPanel(this);
		tabPane.addTab("Block View", ddPanel);
		tabPane.addTab("Setting", new JScrollPane(paneSetting));
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);

		contentPane = new TransparentPane(0.65F, getBackground(),
				new BorderLayout());
		contentPane.add(tabPane);
		setContentPane(contentPane);
		tabPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane pane = (JTabbedPane) e.getSource();
				if (e.getSource() instanceof JTabbedPane) {
					if (pane.getSelectedComponent() == ddPanel)
						ddPanel.requestFocus();// paneView.refresh(pads);
					else if(pane.getSelectedComponent() == scrView){
						System.out.println("display");
						paneView.refresh(pads);
					}
				}
			
		}});
		
		for (Tab tab : NoteTabManager.getTabs()){
			tabPane.addTab(tab.name, tab.component);
		}
		
		tabPane.addChangeListener(NoteTabManager.getListener(this));

		setSize(new Dimension(DEFWIDTH, DEFHEIGHT));

		

		thread.setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		thread.draw(g);
	}

	/**
	 * @return the height of a normal text line
	 */
	public int getLineHeight() {
		Graphics graphics = getGraphics();
		return graphics.getFontMetrics().getHeight();
	}

	/**
	 * refresh the text of both panes
	 */
	public void refresh() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				desktop.setToobearIndterminate(true, "Loading Note");
				paneEdit.refresh();
				paneView.refresh(pads);
				desktop.setToolbarProgress(0, "Idling");
			}
		});
		thread.start();

	}

	/**
	 * Save the text from the textpane to the file, also rebuild the current
	 * note's project
	 */
	public void saveNote() {
		refresh();
		setIsUnsaved(false);
		try {
			FileUtils.write(file, paneEdit.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param unsaved
	 *            set the saved or unsaved status, which means the text on this
	 *            pane is either changed or unchanged since the last save
	 */
	public void setIsUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
		if (unsaved)
			setTitle(file.getName().replaceAll(file.getExtension(), "") + "*");
		else
			setTitle(file.getName().replaceAll(file.getExtension(), ""));
	}

	public boolean isUnsaved() {
		return unsaved;
	}

	/**
	 * find and show the code suggestions
	 * 
	 * @param x
	 *            coord of the suggestion frame (on the screen)
	 * @param y
	 *            coord of the suggestion frame (on the screen)
	 * @param suggestion
	 *            the text to be used for suggestion
	 */
	public void showSuggestion(int x, int y, String suggestion, FrameNote note) {
		if (suggestion.equals("")) {
			hideSuggestion();
			return;
		}

	
		Point pt = getLocationOnScreen();
		int yy = pt.y + y ;
		if (note.file == null)
			System.out.println("uh oh");
		frameSuggestion.setLocation(pt.x + x, yy+ note.getLineHeight()
				+ 40);
		
		LinkedList<ReferenceItem> results = Reference.findSimilarRef(
				suggestion, note.file.parentProject.getObjectsfromExtension(file.getExtension()));
		
	/*	System.out.println(note.file.parentProject.name);
		for (EllowynProject project : note.file.parentProject.referenceProjects){
			results.addAll(Reference.findSimilarRef(suggestion, project.getObjectsfromExtension(file.getExtension())));
			System.out.println(project.name);
		}*/
		frameSuggestion.setSuggestions(note, results);
	}

	public void hideSuggestion() {
		frameSuggestion.setVisible(false);
	}

	public FrameDesktop getDesktop() {
		// TODO Auto-generated method stub
		return desktop;
	}
	
	public EllowFile getFile() {
		return file;
	}

	public void setFile(EllowFile file) {
		this.file = file;
	}

	public LinkedList<EllowObject> getObjects() {
		return objects;
	}

	public void setObjects(LinkedList<EllowObject> objects) {
		this.objects = objects;
	}

}

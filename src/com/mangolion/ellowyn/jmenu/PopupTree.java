package com.mangolion.ellowyn.jmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.filetype.FileType;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.tree.EllowTree;

@SuppressWarnings("serial")
public class PopupTree extends JPopupMenu {
	public static int width = 100;
	EllowTree ellowTree;
	
	public PopupTree(final EllowTree ellowTree) {
		this.ellowTree = ellowTree;
		createMenuNew();
		createFileMenu();
		JMenuItem mProjectInfo = new JMenuItem("Project Info");
		add(mProjectInfo);
		mProjectInfo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.showProjectInfo();
			}
		});
		//setPopupSize(width, getPreferredSize().height);
		
		JMenuItem mOpenasNote = new JMenuItem("Open as Note");
		mOpenasNote.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ellowTree.openAsNote();
			}
		});
		add(mOpenasNote);
		
		JMenuItem mOpenasPlainText = new JMenuItem("Open as Plain Text");
		mOpenasPlainText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ellowTree.openAsPlainText();
			}
		});
		add(mOpenasPlainText);
		
		JMenuItem mProjectRef = new JMenuItem("Project References");
		add(mProjectRef);
		mProjectRef.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.showProjectReferences();
			}
		});
		
		EllowFile file = ellowTree.getSelectedFile();
		
	}

	private void createMenuNew() {
		JMenu menuNew = new JMenu("New");
		JMenuItem mGroup = new JMenuItem("Group");
		mGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.createGroup();
			}
		});
		menuNew.add(mGroup);
		
		EllowFile file = (EllowFile) ((DefaultMutableTreeNode) ellowTree.getLastSelectedPathComponent()).getUserObject();
		EllowynProject project =file.parentProject;
		
		if (project != null)
		for (final FileType type: project. getFileTypes()){
		if (!type.userCreatable)
			continue;
		
		JMenuItem mNote = new JMenuItem(type.type);
		mNote.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ellowTree.createFile(type.extensions.getFirst());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuNew.add(mNote);
		}

		
		menuNew.setSize(width, getPreferredSize().height);
		add(menuNew);

	}
	
	public void createFileMenu(){
		/*JMenuItem mCopy = new JMenuItem("Copy");
		JMenuItem mPaste = new JMenuItem("Paste");
		JMenuItem mCut = new JMenuItem("Cut");*/
		JMenuItem mRename = new JMenuItem("Rename");
		JMenuItem mDelete = new JMenuItem("Delete");
		JMenuItem mInport = new JMenuItem("Import");
		
		/*add(mCopy);
		add(mPaste);
		add(mCut);*/
		add(mRename);
		add(mDelete);
		add(mInport);
		
		mRename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.renameFile();
			}
		});
		
		mDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.deleteFile();
			}
		});
		
		mInport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ellowTree.importFile();
			}
		});
	}
	
}

package com.mangolion.ellowyn.tree;

import iniwriter.IniWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FileUtils;

import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.jmenu.PopupTree;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.ProjectDefault;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.utility.Utility;

@SuppressWarnings("serial")
public class EllowTree extends JTree implements MouseListener {
	/**
	 * The model of the tree
	 */
	public DefaultTreeModel model;

	/**
	 * The root note of the tree
	 */
	DefaultMutableTreeNode root;

	/**
	 * The parent desktop pane
	 */
	FrameDesktop desktopPane;

	/**
	 * the combobox model that holds project catgories
	 */
	public DefaultComboBoxModel<String> cbModel = new DefaultComboBoxModel<>();

	/**
	 * the combobox that holds project categories
	 */
	public JComboBox<String> cbCat = new JComboBox<String>(cbModel);

	/**
	 * The current project category that is filtered
	 */
	public String categoryFilter;
	
	public boolean firstRefresh = true;
	LinkedList<String> openWindows = new LinkedList<>(IniWriter.getOpenWindows());

	public EllowTree(FrameDesktop desktopPane) {
		categoryFilter = IniWriter.getCatFilter();
		this.desktopPane = desktopPane;
		model = (DefaultTreeModel) getModel();
		root = (DefaultMutableTreeNode) getModel().getRoot();
		setRootVisible(false);
		refreshWorkspace(true);
		addMouseListener(this);
		setCellRenderer(new TreeRenderer(this));
		cbCat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object selected = cbCat.getSelectedItem();
				categoryFilter = (selected != null)? selected.toString():"All";
				refreshWorkspace(false);
				IniWriter.setCatFilter(categoryFilter);
			}
		});
		cbModel.setSelectedItem(categoryFilter);
		// setDragEnabled(true);
		// setDropMode(DropMode.USE_SELECTION);
	}

	/**
	 * clear the current list of projects and nodes and reload all files in the
	 * current workspace, also rebuild all of the projects
	 */
	public void refreshWorkspace(final boolean refreshCategories) {
		Projects.projects.clear();
		root.removeAllChildren();
		model.reload();

		if (refreshCategories) {
			cbModel.removeAllElements();
			cbModel.addElement("All");
		}
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				getProgress(new EllowFile(Projects.workspace));
				addFiletoNode(root, new EllowFile(Projects.workspace));
				Projects.rebuildAll();
				System.gc();
				desktopPane.hideDialogProgress();
				model.reload();
				firstRefresh = false;
			}

			float totalFiles = 1, currentFiles = 0;

			private void getProgress(File ellowFile) {
				totalFiles++;
				desktopPane.setDialogIndeterminate(true,
						"Scanned " + String.valueOf((int) (totalFiles))
								+ " files");
				if (ellowFile.isDirectory())
					for (File file : ellowFile.listFiles()) {
						getProgress(file);
					}
			}

			EllowFile tempProjectFile;

			/**
			 * A recursive func designed to find and add a file and all of its
			 * children into the tree
			 * 
			 * @param pnode
			 *            the parent node for adding the children file
			 * @param pfile
			 *            the parent file for adding all of its children to the
			 *            pnode
			 */
			public void addFiletoNode(DefaultMutableTreeNode pnode,
					EllowFile pfile) {
				currentFiles++;
				desktopPane.setDialogProgress(
						(int) (currentFiles / totalFiles * 100),
						"Refreshing Workspace", "Scanning Projects");
	
				if (pfile.isDirectory()){
					for (File file : pfile.listFiles())
						if (file.isDirectory())
							scanFile(file,pfile,  pnode);
					for (File file : pfile.listFiles())
						if (!file.isDirectory())
							scanFile(file,pfile,  pnode);
				}
			}
			
			public void scanFile(File file, EllowFile pfile, DefaultMutableTreeNode pnode){


				final EllowFile efile = new EllowFile(file);
				if (pnode == root) {
					if (!file.isDirectory())
						return;

					// EllowynProject project = new
					// EllowynProject(file.getName(), desktopPane);
					// Projects.projects.add(project);
					// efile.parentProject = project;
					// efile.isProjectFile = true;
					efile.scanProjectInfo(desktopPane);
					if (efile.parentProject != null) {

						String category = efile.parentProject.category;
						// add to category list
						if (refreshCategories) {
							boolean found = false;
							for (int i = 0; i < cbModel.getSize(); i++)
								if (cbModel.getElementAt(i).equals(
										category))
									found = true;
							if (!found && !category.equals(""))
								cbModel.addElement(category);
						}
						// filter category
						if (!categoryFilter.equals("All")
								&& !categoryFilter.equals(category))
							return;
					}

				}
				// pass the project name to this file
				if (pfile.parentProject != null)
					efile.parentProject = pfile.parentProject;

				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						efile);
				pnode.add(node);
				addFiletoNode(node, efile);
				if (firstRefresh){
					for (String str: openWindows){
						if (file.getAbsolutePath().trim().equals(str.trim()))
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									desktopPane.openFile(efile);
								}
							});
						//System.out.println(file.getAbsolutePath() + " | " + str);
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// handle right click, show the popup menu
		if (SwingUtilities.isRightMouseButton(e)) {

			/*
			 * int row = getClosestRowForLocation(e.getX(), e.getY());
			 * setSelectionRow(row);
			 */

			PopupTree menu = new PopupTree(this);
			menu.show(getParent(), e.getX(), e.getY());
		} else if (e.getClickCount() == 2) {
			// handle double click, open the selected file
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
			desktopPane.openFile((EllowFile) node.getUserObject());
		}
	}

	/**
	 * create a new note and add it to the project and tree
	 */
	public void createFile(String ext) throws IOException {
		String name = JOptionPane
				.showInputDialog("Please Enter a new file name:");
		if (name == null)
			return;

		// get the current selected node and the file attached to it
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile pfile = (EllowFile) node.getUserObject();
		EllowFile file;

		if (pfile.isDirectory()) {
			file = new EllowFile(pfile.getAbsolutePath() + "\\\\" + name + "."
					+ ext);
			file.createNewFile();
			file.parentProject = pfile.parentProject;
			// use insert node instead of add to update the tree without
			// reloading it, which cause the tree to be reset
			model.insertNodeInto(new DefaultMutableTreeNode(file), node,
					node.getChildCount());
		} else {
			file = new EllowFile(pfile.getParent() + "\\\\" + name + "." + ext);
			file.createNewFile();
			file.parentProject = pfile.parentProject;
			node = ((DefaultMutableTreeNode) node.getParent());
			model.insertNodeInto(new DefaultMutableTreeNode(file), node,
					node.getChildCount());
		}
		// save the project to the file, and the file to the project, for later
		// reference
		pfile.parentProject.rebuild();
		model.nodeChanged(root);

		/*
		 * if (ext.equals(ProjectDefault.EXT_NOTE))
		 * desktopPane.createFrameNote(file);
		 */
		desktopPane.openFile(file);
	}

	public void renameFile() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile file = (EllowFile) node.getUserObject();
		String newName = JOptionPane.showInputDialog("Please enter new name",
				file.getName());
		if (newName == null)
			return;
		newName = file.getParent() + "\\\\" + newName;
		EllowynProject project = file.parentProject;
		try {
			Utility.renameFile(file.getAbsolutePath(), newName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!file.isProjectFile)
			project.rebuild(); // don't rebuild the project if the project
								// itself is deleted
		desktopPane.closeFrames(file);
		EllowFile newFile = new EllowFile(newName);
		newFile.isProjectFile = file.isProjectFile;
		newFile.parentProject = file.parentProject;
		node.setUserObject(newFile);
	}

	/**
	 * Delete the file for the selected node, and all of its children if its a
	 * directory
	 * 
	 * <p>
	 * Close any FrameNote opened of those files
	 */
	public void deleteFile() {
		if (JOptionPane.showConfirmDialog(desktopPane,
				"Are you sure? this action is permanent!") != JOptionPane.YES_OPTION)
			return;

		EllowFile file = null;
		TreePath paths[] = getSelectionModel().getSelectionPaths();
		if (paths == null)
			return;
		for (TreePath path : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			file = (EllowFile) node.getUserObject();
			EllowynProject project = file.parentProject;
			file.deleteAll();
			if (!file.isProjectFile && project != null)
				project.rebuild(); // don't rebuild the project if the project
									// itself is deleted
			desktopPane.closeFrames(file);
			model.removeNodeFromParent(node);
		}
	}

	/**
	 * Create a folder in the selected node as a group
	 */
	public void createGroup() {
		String name = JOptionPane
				.showInputDialog("Please Enter a new group name:");
		if (name.equals(""))
			return;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile pfile = (EllowFile) node.getUserObject(), file;
		if (pfile.isDirectory()) {
			file = new EllowFile(pfile.getAbsolutePath() + "\\\\" + name);
			file.mkdirs();
			model.insertNodeInto(new DefaultMutableTreeNode(file), node,
					node.getChildCount());
		} else {
			file = new EllowFile(pfile.getParent() + "\\\\" + name);
			file.mkdirs();
			node = ((DefaultMutableTreeNode) node.getParent());
			model.insertNodeInto(new DefaultMutableTreeNode(file), node,

			node.getChildCount());
		}
		file.parentProject = pfile.parentProject;
		pfile.parentProject.rebuild();
		model.nodeChanged(root);
	}

	public void showProjectInfo() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile file = (EllowFile) node.getUserObject();
		desktopPane.showProjectInfo(file.parentProject);

	}

	public void showProjectReferences() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile file = (EllowFile) node.getUserObject();
		desktopPane.showProjectReferences(file.parentProject);
	}
	
	public EllowFile getSelectedFile(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		return (EllowFile) node.getUserObject();
	}
	
	public void importFile() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile pfile = (EllowFile) node.getUserObject();

		JFileChooser chooser = new JFileChooser();
		int input = chooser.showOpenDialog(this);

		if (input == JFileChooser.APPROVE_OPTION) {
			EllowFile srcFile = new EllowFile(chooser.getSelectedFile());
			EllowFile destFile;
			try {
				if (pfile.isDirectory()) {
					destFile = new EllowFile(pfile.getAbsolutePath() + "////"
							+ srcFile.getName());
					FileUtils.copyFile(srcFile, destFile);
				} else {
					destFile = new EllowFile(pfile.getParentFile() + "////"
							+ srcFile.getName());
					FileUtils.copyFile(srcFile, destFile);
					node = node.getPreviousNode();
				}
				destFile.parentProject = pfile.parentProject;
				model.insertNodeInto(new DefaultMutableTreeNode(destFile),
						node, node.getChildCount());
				pfile.parentProject.rebuild();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void openAsNote() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile pfile = (EllowFile) node.getUserObject();
		desktopPane.openFileAsNote(pfile);
	}

	public void openAsPlainText() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
		EllowFile pfile = (EllowFile) node.getUserObject();
		desktopPane.openFileAsPlainText(pfile);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) getPathForLocation(
				e.getX(), e.getY()).getLastPathComponent();
		EllowFile currentFile = (EllowFile) currentNode.getUserObject();

		TreePath[] paths = getSelectionPaths();

		for (TreePath path : paths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			EllowFile selFile = (EllowFile) node.getUserObject();
			// System.out.println(selFile);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

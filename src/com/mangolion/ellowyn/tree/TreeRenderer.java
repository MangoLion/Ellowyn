package com.mangolion.ellowyn.tree;

import images.Icons;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.mangolion.ellowyn.file.EllowFile;

public class TreeRenderer extends DefaultTreeCellRenderer {
	EllowTree ellowTree;

	public TreeRenderer(EllowTree ellowTree) {
		this.ellowTree = ellowTree;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		// since root node doesn't have a file, don't set icon for it
		if (node == ellowTree.root)
			return this;
		if (node.getUserObject() instanceof EllowFile) {
			EllowFile file = (EllowFile) node.getUserObject();
			if (!file.isDirectory()){
				if(file.parentProject != null)
					setIcon(file.parentProject.getIconforExt(file.getExtension()));
				else
					setIcon(Icons.fileIcon);
			}
			else if (file.isProjectFile)
				setIcon(Icons.projectIcon);
			else
				setIcon(Icons.folderIcon);
		}
		return this;
	}

}
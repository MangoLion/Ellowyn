package com.mangolion.ellowyn.panes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import plugin.Function;
import plugin.Plugins;

import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.frames.FrameNote;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.reference.Reference;
import com.mangolion.ellowyn.utility.VisibleCaretListener;
import com.mangolion.ellowyn.values.Values;

public class PanePlainText extends JTextPane implements FocusListener, MouseListener {
	FrameNote parent;

	public PanePlainText(FrameNote frameMain) {
		this.parent = frameMain;
		setStyle();
		addFocusListener(this);
		addCaretListener(new VisibleCaretListener());
		setUpBindings();
		refresh();
	}
	
	public void refresh(){
		setText(parent.getText());
	}

	private void setUpBindings() {
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK,
						true), "save");
		getActionMap().put("save", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.saveNote(getText());
			}
		});
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK,
						true), "undo");
		getActionMap().put("undo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	void setStyle() {
		// setBackground(Values.colFramebg);
		setCaretColor(Values.colCaret);
		//setForeground(Values.);
	}


	@Override
	public void focusGained(FocusEvent e) {
		refresh();
		String path = parent.getFile().getAbsolutePath();
		parent.getDesktop().setTitle(
				"Ellowyn - " + path.substring(Projects.workspace.length() + 1));
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		parent.getDesktop().hideAllPopup();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
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

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}

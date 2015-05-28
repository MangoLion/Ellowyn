package com.mangolion.ellowyn.panes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.frames.FrameNote;
import com.mangolion.ellowyn.values.Values;

public class PaneViewBackUp extends JTextPane implements KeyListener,
		MouseMotionListener, FocusListener {
	FrameNote frameMain;
	public LinkedList<Pad> pads = new LinkedList<Pad>();
	public LinkedList<EllowObject> objects = new LinkedList<EllowObject>();
	public int lastCaretPos = 0;
	private char lastChar;

	public PaneViewBackUp(FrameNote frameMain) {
		this.frameMain = frameMain;
		setStyle();
		/*
		 * addKeyListener(this); addMouseMotionListener(this);
		 * addCaretListener(new VisibleCaretListener());
		 */
		addFocusListener(this);
	}

	void setStyle() {
		setBackground(Values.colFramebg);
		setCaretColor(Values.colCaret);
		setForeground(Color.white);
	}

	public void refresh() {
		lastCaretPos = getCaretPosition();
		objects.clear();
		pads.clear();
		for (Pad pad : frameMain.pads)
			pads.add((Pad) pad.clone());
		displayPads();

		if (lastCaretPos < getText().length())
			setCaretPosition(lastCaretPos);
		else if (getText().length() > 0)
			setCaretPosition(getText().length());

	}

	public int getnextLineNumber() {
		int s = 0;
		for (int a = 0; a < getText().length(); a++)
			if (getText().charAt(a) == '\n')
				s++;
		return s;
	}

	Style style;

	public void displayPads() {
		setText("");

		for (Pad pad : pads) {
			transformPad(pad);
			pad.positionInText = getText().toString().length() - 1;
			setText(getText().toString() + pad.paddling + pad.string);
		}

		StyledDocument doc = getStyledDocument();
		Style style = addStyle("Style Pad", null);
		for (Pad pad : pads) {
			StyleConstants.setForeground(style, pad.color);
			doc.setCharacterAttributes(pad.positionInText + 1,
					+pad.paddling.length() + pad.string.length() + 1, style,
					false);
		}
	}

	public void transformPad(Pad pad) {
		pad.refreshType();
		if (Values.isKeyword(pad.string)) {
			if (pad.string.equals("[") || pad.string.equals("]")
					|| pad.string.equals("#")) {
				pad.string = "";
			} else if (pad.string.equals(";")) {
				if (pads.indexOf(pad) - 1 > 0
						&& pads.get(pads.indexOf(pad)).isReference)
					pad.string = "'";
				else
					pad.string = ".";
			} else if (pad.string.equals("#"))
				pad.string = "'";
		} else if (pad.isReference) {
			String str[] = pad.string.split("'");
			pad.string = str[str.length - 1];
		}
	}

	public static String trimLeft(String s) {
		return s.replaceAll("^\\s+", "");
	}

	public int getLineStartDis(int pos) {
		String input = getText().toString();
		int index = pos;
		char c = '*';
		String str = "";

		while (index > -1 && c != '\n') {
			c = input.charAt(index);
			str += c;
			index--;
		}

		// because chars varies by width, i need to get the width in terms of
		// number of empty string " "
		int dis = Math.round((float) (getTextWidth(str))
				/ (float) (getTextWidth(" ")));

		return dis;
	}

	public Rectangle getXYatPos(int pos) {
		try {
			return modelToView(pos);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public int getPositionAtXY(int x, int y) {
		Point pt = new Point(x, y);
		return viewToModel(pt);
	}

	public Pad getPadatPos(int pos) {
		for (Pad pad : pads) {
			if (pos >= pad.positionInText
					&& pos <= pad.positionInText + pad.string.length()
							+ pad.paddling.length()) {
				return pad;
			}
		}
		return null;
	}

	public float getTextWidth(String str) {
		Graphics graphics = getGraphics();
		return graphics.getFontMetrics().stringWidth(str);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		lastChar = arg0.getKeyChar();
		refresh();

		repaint();
	}

	public String inserCharAt(String str, int index, char c) {
		if (str == null) {
			return str;
		} else if (index < 0 || index >= str.length()) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		sb.insert(index, c);
		return sb.toString();
	}

	public String deletecharAt(String str, int index) {
		if (str == null) {
			return str;
		} else if (index < 0 || index >= str.length()) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		if (sb.charAt(index) == '\n')
			sb.deleteCharAt(index);
		return sb.toString();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Pad pad = getPadatPos(getPositionAtXY(e.getX(), e.getY()));
		if (pad != null) {
			pad.refreshType();
			setToolTipText(pad.string + " " + pad.type);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		showSuggestion();
		int code = arg0.getKeyCode();
		switch (code) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			if (frameMain.frameSuggestion.isVisible())
				frameMain.frameSuggestion.jList.requestFocus();
			break;
		default:

			break;
		}
	}

	public void showSuggestion() {
		lastCaretPos = getCaretPosition();
		Pad currentPad = getPadatPos(getCaretPosition() - 2);
		if (currentPad != null && currentPad.parentStatement != null) {
			Rectangle rec = getXYatPos(getCaretPosition());
			frameMain.showSuggestion(rec.x, rec.y,
					getWordAt(getCaretPosition() - 1), frameMain);
		} else
			frameMain.hideSuggestion();
	}

	public void replaceWord(int pos, String replace) {
		StringBuffer builder = new StringBuffer(getText().toString());
		char c = builder.charAt(pos);
		while (c != ' ' && c != '\n' && c != '\t' && c != '\0'
				&& !Values.isKeyword(String.valueOf(c))) {
			builder.deleteCharAt(pos);
			pos--;
			c = builder.charAt(pos);
		}
		builder.insert(pos + 1, replace);
		setText(builder.toString());
		refresh();
	}

	public String getWordAt(int pos) {
		String input = getText().toString();
		StringBuffer builder = new StringBuffer();
		if (pos > input.length() - 1)
			pos = input.length() - 1;
		char c = input.charAt(pos);
		while (c != ' ' && c != '\n' && c != '\t' && c != '\0'
				&& !Values.isKeyword(String.valueOf(c))) {
			builder.insert(0, c);
			if (pos > 0)
				pos--;
			else
				break;
			c = input.charAt(pos);
		}
		System.out.println(builder.toString());
		return builder.toString();
	}

	public void setUpDownKey(boolean enabled) {
		InputMap im = getInputMap(WHEN_FOCUSED);
		ActionMap am = getActionMap();

		am.get("caret-down").setEnabled(enabled);
		am.get("caret-up").setEnabled(enabled);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void focusLost(FocusEvent e) {
		setEditable(true);

	}

	@Override
	public void focusGained(FocusEvent e) {
		setEditable(false);
		refresh();
	}
}

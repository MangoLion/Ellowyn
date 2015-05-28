package com.mangolion.ellowyn.panes;

import images.Icons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameDesktop;
import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.reference.Reference;
import com.mangolion.ellowyn.values.Values;

public class PaneView extends JTextPane implements
		MouseMotionListener, FocusListener{
	public LinkedList<Pad> pads = new LinkedList<Pad>();
	public int lastCaretPos = 0;
	private char lastChar;
	public EllowFile file;

	public PaneView() {
		setStyle();
		/*
		 * addKeyListener(this); addMouseMotionListener(this);
		 * addCaretListener(new VisibleCaretListener());
		 */
		addMouseMotionListener(this);
	}

	void setStyle() {
		//setBackground(Values.colFramebg);
		setCaretColor(Values.colCaret);
		setForeground(Color.white);
	}
	
	public void refresh(String code) {
		refresh(Reader.readString(code));

	}

	public void refresh( LinkedList<Pad> pads) {
		lastCaretPos = getCaretPosition();
		this.pads.clear();
		for (Pad pad : pads)
			this.pads.add((Pad) pad.clone());
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
		requestFocus();
		String text = "";
		for (Pad pad : pads) {
			transformPad(pad);
			pad.positionInText = text.length() - 1;
			if (!pad.isReference)
				text += pad.paddling + pad.string;
			else{
				String str[] = pad.string.split("'");
				text += pad.paddling + str[str.length - 1];
			}
		}
		setText(text);

		StyledDocument doc = getStyledDocument();
		Style style = addStyle("Style Pad", null);
		Style styleIcon = addStyle("Icon", null);
		for (Pad pad : pads) {
			StyleConstants.setForeground(style, pad.color);
			
			if (!pad.isReference)
				doc.setCharacterAttributes(pad.positionInText + 1,
					+pad.paddling.length() + pad.string.length() + 1, style,
					false);
			else{
				String str[] = pad.string.split("'");
				doc.setCharacterAttributes(pad.positionInText + 1,
						+pad.paddling.length() + str[str.length-1].length() + 1, style,
						false);
			}
			
			if (pad.isReference){
				StyleConstants.setIcon(styleIcon, Icons.fileIcon);
				try {
					doc.insertString(doc.getLength(), "", styleIcon);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/*try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
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
		}
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
		if (!isFocusOwner() || file == null)
			return;
		Pad pad = getPadatPos(getPositionAtXY(e.getX(), e.getY()));
		/*if (pad != null && pad.isReference) {
			pad.refreshType();
			Point pt = getLocationOnScreen();
			EllowMain.getMainDesktop().showToolTip(pt.x + e.getX(),pt.y + e.getY() + 30, Reference.findReferenceComponent(pad.string, project, file.getExtension()), project, file);
		}*/
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		Timer timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EllowMain.getMainDesktop().hideToolTip();
			}
		});
		
		timer.start();

	}
	
	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}


}

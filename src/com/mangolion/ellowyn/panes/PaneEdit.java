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
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.undo.UndoManager;

import plugin.Function;
import plugin.Plugins;

import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.frames.FrameNote;
import com.mangolion.ellowyn.parser.Reader;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.reference.Reference;
import com.mangolion.ellowyn.utility.CompoundUndoManager;
import com.mangolion.ellowyn.utility.VisibleCaretListener;
import com.mangolion.ellowyn.values.Values;

public class PaneEdit extends JTextPane implements KeyListener,
		MouseMotionListener, FocusListener, MouseListener {
	FrameNote parent;
	public LinkedList<Pad> pads;
	public LinkedList<EllowObject> objects;
	public int lastCaretPos = 0;
	private char lastChar;
	boolean isControlPressed = false, isEnterPressed = false;
	boolean ctrlPressed = false;
	
	UndoManager undo = new CompoundUndoManager();
	public PaneEdit(FrameNote frameMain) {
        getStyledDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                if (evt.getEdit().isSignificant()) {
                    undo.addEdit(evt.getEdit());
               
                }
            }
        });
		this.parent = frameMain;
		this.pads = frameMain.pads;
		this.objects = frameMain.getObjects();
		setStyle();
		addKeyListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
		addCaretListener(new VisibleCaretListener());
		addMouseListener(this);
		setUpBindings();
	}

	private void setUpBindings() {
		getInputMap().clear();
		
		//save
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK,
						true), "save");
		getActionMap().put("save", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.saveNote();
			}
		});

		//ctrl+space
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK,
						true), "suggestion");
		getActionMap().put("suggestion", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("called");
				requestFocus();
				showSuggestion();
			}
		});
		
		//format
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_MASK,
						true), "format");
		getActionMap().put("format", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				formatPads();
			}
		});
		
		//undo
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK,
						true), "undo");
		getActionMap().put("undo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (undo.canUndo())
					undo.undo();
			}
		});
		
		//redo
		getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK,
						true), "redo");
		getActionMap().put("redo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (undo.canRedo())
					undo.redo();
			}
		});
	}

	void setStyle() {
		// setBackground(Values.colFramebg);
		setCaretColor(Values.colCaret);
		setForeground(Color.white);
		StyleContext sc = StyleContext.getDefaultStyleContext();
		TabStop[] tabStop = new TabStop[15];
		for (int i = 1; i < 16; i ++)
			tabStop[i-1] = new TabStop(30*i);
		TabSet tabs = new TabSet(tabStop);
		AttributeSet paraSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabs);
		setParagraphAttributes(paraSet, false);
	}

	public void refresh() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				lastCaretPos = getCaretPosition();
				objects.clear();
				pads.clear();
				pads.addAll(Reader.readString(getText().toString()));
				objects.addAll(Reader.read(pads));
				executeFunctions();
				displayPads();

				if (lastCaretPos < getText().length())
					setCaretPosition(lastCaretPos);
				else if (getText().length() > 0)
					setCaretPosition(getText().length());
			}
		});

	}

	private void executeFunctions() {
		LinkedList<Pad> refPads = new LinkedList<Pad>();
		for (Pad pad : pads) {
			if (pad.isReference && isPadCompleteReference(pad)) {
				Function function = Plugins.findFunction(pad.string);
				if (function != null) {
					pad.string = "";
					System.out.println(parent.getFile().getName());
					function.execute(parent, pad);
					refPads.add(pad);
				}
			}
		}
		for (Pad pad : refPads) {
			removePadReference(pad);
		}
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
		String text = "";
		for (float i = 0; i < pads.size(); i++) {
			Pad pad = pads.get((int) i);
			pad.positionInText = text.length() - 1;
			text += pad.paddling + pad.string;
		}
		setText(text);

	StyledDocument doc = getStyledDocument();
		Style style = addStyle("Style Pad", null);
		for (Pad pad : pads) {
			pad.refreshType();
			StyleConstants.setForeground(style, pad.color);
			doc.setCharacterAttributes(pad.positionInText + 1,
					+pad.paddling.length() + pad.string.length() + 1, style,
					false);

		}

	}
	
	public void formatPads(){
		setText(Writer.write(Reader.read(Reader.readString(getText()))));
		refresh();
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
		//if (!isControlPressed)
		//	replaceSelection("");
		lastChar = arg0.getKeyChar();
		if (!isControlPressed && ! isEnterPressed)
			refresh();
		parent.setIsUnsaved(true);
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
		if (!isFocusOwner())
			return;

		Pad pad = getPadatPos(getPositionAtXY(e.getX(), e.getY()));
		if (pad != null && pad.isReference) {
			pad.refreshType();
			Point pt = getLocationOnScreen();
			EllowFile file = new EllowFile(pad.string);
			file.parentProject = parent.getFile().parentProject;
			parent.getDesktop().showToolTip(
					pt.x + e.getX(),
					pt.y + e.getY() + 30,
					Reference.findReferenceComponent(pad.string, parent
							.getFile().parentProject, parent.getFile()
							.getExtension()), parent.getFile().parentProject, file);
		}
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_CONTROL:
			isControlPressed = true;
			break;
		/*case KeyEvent.VK_C:
		case KeyEvent.VK_V:
		case KeyEvent.VK_X:
			if (isControlPressed)
				arg0.consume();
			break;*/
		case KeyEvent.VK_SPACE:
			System.out.println("space");
			arg0.consume();
			requestFocusInWindow();
			break;
		case KeyEvent.VK_ENTER:
				isEnterPressed = true;
			try {
				System.out.println("enter");
				//if (isControlPressed)
					arg0.consume();
				String tabs = "";
				for (int i = getCaretPosition(); i >= 0; i --){
					if (i > getText().length() - 1)
						i--;
					char c = getText().charAt(i);
					if (c == '\t')
						tabs += "\t";
					if (c == '\n' && i != getCaretPosition())
						break;
					
				}
				
				getStyledDocument().insertString(getCaretPosition(), "\n" + tabs, null);
				//setCaretPosition(getCaretPosition());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		lastCaretPos = getCaretPosition();
		if (parent.frameSuggestion.isVisible())
			showSuggestion();
		int code = arg0.getKeyCode();
		switch (code) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			if (parent.frameSuggestion.isVisible())
				parent.frameSuggestion.jList.requestFocus();
			break;
		case KeyEvent.VK_ENTER:
				isEnterPressed = false;
			break;
		case KeyEvent.VK_ESCAPE:
				parent.hideSuggestion();
			break;
		case KeyEvent.VK_CONTROL:
			isControlPressed = false;
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
			parent.showSuggestion(rec.x, rec.y,
					getWordAt(getCaretPosition() - 1), parent);
		} else
			parent.hideSuggestion();
	}

	public void replaceWord(int pos, String replace) {
		StringBuffer builder = new StringBuffer(getText().toString());
		char c = builder.charAt(pos);
		while (c != ' ' && c != '\n' && c != '\t' && c != '\0'
				&& !Values.isKeyword(String.valueOf(c)) && pos > -1) {
			builder.deleteCharAt(pos);
			pos--;
			if (pos > -1)
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
		return builder.toString();
	}

	public void setUpDownKey(boolean enabled) {
		InputMap im = getInputMap(WHEN_FOCUSED);
		ActionMap am = getActionMap();

		am.get("caret-down").setEnabled(enabled);
		am.get("caret-up").setEnabled(enabled);
	}

	@Override
	public void focusGained(FocusEvent e) {
		String path = parent.getFile().getAbsolutePath();
		parent.getDesktop().setTitle(
				"Ellowyn - " + path.substring(Projects.workspace.length() + 1));
	}

	@Override
	public void focusLost(FocusEvent e) {
		parent.desktop.hideToolTip();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		parent.getDesktop().hideAllPopup();
	}

	public boolean isPadCompleteReference(Pad pad) {
		int index = pads.indexOf(pad);
		Pad prevPad = (index - 1 > 0) ? pads.get(index - 1) : null;
		Pad nextPad = (index + 1 < pads.size() - 1) ? pads.get(index + 1)
				: null;
		if (prevPad != null && prevPad.string.equals("#"))
			if (nextPad != null && nextPad.string.equals("#"))
				return true;
		return false;
	}

	public void removePadReference(Pad pad) {
		int index = pads.indexOf(pad);
		Pad prevPad = (index - 1 > 0) ? pads.get(index - 1) : null;
		Pad nextPad = (index + 1 < pads.size() - 1) ? pads.get(index + 1)
				: null;
		if (prevPad != null && prevPad.string.equals("#"))
			pads.remove(prevPad);
		if (nextPad != null && nextPad.string.equals("#"))
			pads.remove(nextPad);
	}

	@Override
	public void mouseDragged(MouseEvent e) {

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

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

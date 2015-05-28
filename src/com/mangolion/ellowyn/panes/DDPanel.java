package com.mangolion.ellowyn.panes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.components.Statement;
import com.mangolion.ellowyn.frames.FrameNote;
import com.mangolion.ellowyn.parser.Writer;
import com.mangolion.ellowyn.values.Values;

public class DDPanel extends JSplitPane implements FocusListener,
		ActionListener {

	FrameNote note;
	private LinkedList<com.mangolion.ellowyn.components.Component> components = new LinkedList<>();
	public DDContentPane contentPane;
	public JPanel dragPane;
	private JScrollPane scrBlocks;

	JButton btObject = new JButton("Object"), btAttribute = new JButton(
			"Attribute"), btStatement = new JButton("Statement");

	public DDPanel(FrameNote note) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		contentPane = new DDContentPane();
		contentPane.setLayout(null);

		dragPane = new JPanel();
		dragPane.setLayout(new BoxLayout(dragPane, BoxLayout.Y_AXIS));
		dragPane.add(btObject);
		dragPane.add(btAttribute);
		dragPane.add(btStatement);
		scrBlocks = new JScrollPane(contentPane);
		setLeftComponent(scrBlocks);
		setRightComponent(dragPane);

		addFocusListener(this);
		setResizeWeight(1.0);
		this.note = note;

		btObject.addActionListener(this);
		btAttribute.addActionListener(this);
		btStatement.addActionListener(this);

		btObject.addMouseListener(mousyButtonAdapter);
		btAttribute.addMouseListener(mousyButtonAdapter);
		btStatement.addMouseListener(mousyButtonAdapter);

	}
	
	 private int offset = 100;
	    @Override
	    public int getDividerLocation() {
	        return getWidth()-offset ;
	    }
	    @Override
	    public int getLastDividerLocation() {
	        return getWidth()-offset;
	    }

	public void refresh() {
		displayPads(getPads());
	}

	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				LinkedList<Pad> transformedPads = new LinkedList<>();
				transformPads(transformedPads);
				displayPads(transformedPads);
			}
		});

	}

	public void savetoCode() {
		String code = "";
		code = Writer.write(getEllowComponents());
		note.paneEdit.setText(code);
		note.paneEdit.refresh();
	}

	public void savetoCode(
			LinkedList<com.mangolion.ellowyn.components.Component> components) {
		String code = "";
		code = Writer.write(components);
		note.paneEdit.setText(code);
		note.paneEdit.refresh();
	}

	private void transformPads(LinkedList<Pad> transformedPads) {

		transformedPads.clear();
		for (Pad pad : note.pads) {
			Pad prevPad = (pad != note.pads.getFirst()) ? note.pads
					.get(note.pads.indexOf(pad) - 1) : null;
			Pad nextPad = (pad != note.pads.getLast()) ? note.pads
					.get(note.pads.indexOf(pad) + 1) : null;

			pad.string.trim();
			if (Values.isKeyword(pad.string) || pad.string.equals("")) {
				if (pad.string.equals("#")) {
					if (prevPad != null && prevPad.isReference)
						prevPad.string += "#";
					else if (nextPad != null && nextPad.isReference)
						nextPad.string = "#" + nextPad.string;
				}
				if (!(pad.string.contains("#") && !pad.string.trim()
						.equals("#")) && !pad.string.contains(Values.objectDef))
					continue;
			}
			transformedPads.add(pad);
		}
	}

	private int getAlignFieldX(Field afield) {
		Pad apad = afield.getPad();
		for (Component component : contentPane.getComponents()) {
			if (component instanceof Field) {
				Field field = (Field) component;
				Pad pad = field.getPad();
				if (apad != pad)
					if ((apad.obj != null && apad.obj.parentObj == null)
							|| (apad.obj != null && pad.obj != null && apad.obj.parentObj == pad.obj.parentObj)
							|| (apad.attribute != null && pad.attribute != null && apad.attribute.parentObj == pad.attribute.parentObj)
							|| (apad.obj != null && pad.attribute != null && apad.obj.parentObj == pad.attribute.parentObj)
							|| (apad.parentStatement != null
									&& pad.parentStatement != null && apad.parentStatement.parentAttribute == pad.parentStatement.parentAttribute)) {
						return field.getX();
					}
			}
		}
		return 0;
	}

	private void displayPads(LinkedList<Pad> pads) {
		// requestFocusInWindow();
		contentPane.removeAll();
		int cx = 0, cy = 0, maxW = 0, maxH = 0;
		Field prevField = null;
		for (Pad pad : pads) {
			pad.refreshType();
			if (pad.parentStatement != null) {
				Statement pStatement = pad.parentStatement;
				int index = pads.indexOf(pad) + 1;
				while (index < pads.size()) {
					Pad npad = pads.get(index);
					if ((npad.parentStatement != null && npad.parentStatement.parentAttribute == pStatement.parentAttribute)|| npad.string.trim().equals("#")) {
						npad.parentStatement.pads.remove(npad);
						pad.string += npad.string;
						//pads.remove(npad);
						npad.string = "";
						index ++;
						System.out.println(pStatement.parentAttribute + "|" + npad.parentStatement.parentAttribute);
					} else
						break;
				}
			}
			
			if (pad.string.equals(""))
				continue;
			
			Field field = new Field(this, pad);
			contentPane.add(field);
			int alignX = getAlignFieldX(field);
			if (pad != pads.getFirst())
				if (alignX > 0
						|| (pad.parentStatement != null && pad.parentStatement.parentAttribute == null)
						|| (pad.obj != null && pad.obj.parentObj == null)) {
					cx = alignX;
					cy += prevField.getHeight();

				}
			field.setLocation(cx, cy);
			
			cx += field.getWidth();
			if (cx > maxW)
				maxW = cx;
			if (cy > maxH + field.getHeight())
				maxH = cy;
			prevField = field;
		}
		contentPane.maxWidth = maxW;
		contentPane.maxHeight = maxH;
		contentPane.revalidate();
		repaint();
	}

	public int getLineHeight() {
		Graphics graphics = getGraphics();
		return graphics.getFontMetrics().getHeight();
	}

	public int getTextWidth(String str) {
		Graphics graphics = getGraphics();
		return (int) (graphics.getFontMetrics().stringWidth(str) + 3);
	}

	@Override
	public void focusGained(FocusEvent e) {
		reload();
	}

	@Override
	public void focusLost(FocusEvent e) {
		// savetoCode();
	}

	public LinkedList<com.mangolion.ellowyn.components.Component> getEllowComponents() {
		components.clear();
		Statement parenState = new Statement("", null);
		for (Component component : contentPane.getComponents()) {
			if (component instanceof Field) {
				Field field = (Field) component;
				Pad pad = field.getPad();
				pad.refreshContent();
				pad.string = pad.string.trim();
				pad.paddling = "";
				if (pad.component != null
						&& ((pad.parentStatement == null) || (pad.parentStatement != null && pad.parentStatement != parenState))) {
					components.add(pad.component);
					if (pad.parentStatement != null)
						parenState = pad.parentStatement;
				}
			}
		}
		components.add(new com.mangolion.ellowyn.components.Component());
		return components;
	}

	public LinkedList<com.mangolion.ellowyn.components.Component> getEllowComponents(
			LinkedList<Pad> ePads) {
		components.clear();
		Statement parenState = new Statement("", null);
		for (Pad pad : ePads) {
			pad.string = pad.string.trim();
			pad.paddling = "";
			if (pad.component != null
					&& ((pad.parentStatement == null) || (pad.parentStatement != null && pad.parentStatement != parenState))) {
				components.add(pad.component);
				if (pad.parentStatement != null)
					parenState = pad.parentStatement;
			}
		}
		components.add(new com.mangolion.ellowyn.components.Component());
		return components;
	}

	public LinkedList<Pad> getPads() {
		LinkedList<Pad> pads = new LinkedList<>();
		for (Component component : contentPane.getComponents()) {
			if (!(component instanceof Field))
				continue;
			Field field = (Field) component;
			field.getPad().refreshContent();
			pads.add(field.getPad());
		}

		return pads;
	}

	public void setEllowComonents(
			LinkedList<com.mangolion.ellowyn.components.Component> pads) {
		this.components = pads;
	}

	public void setPadFocus(Pad fpad, int focus) {
		for (Pad pad : getPads())
			pad.lastFocused = -1;
		fpad.lastFocused = focus;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
		
		if (e.getSource() == btObject) {
			insertObject();
		} else if (e.getSource() == btAttribute) {
			System.out.println("called");
			insertAttribute();
		} else if (e.getSource() == btStatement) {
			insertStatement();
		}
	}

	/**
	 * Inserting a new {@link EllowObject} next to the currently focused pane.
	 * If there are none focused, put it as the last element
	 */
	public void insertObject() {
		LinkedList<Pad> dPads = getPads();
		for (Component component : contentPane.getComponents()) {
			if (component instanceof Field) {
				Field field = (Field) component;
				Pad pad = field.getPad();
				if (pad.lastFocused != -1 || pad == dPads.getLast()) {
					Pad objPad = new Pad("Object"
							+ String.valueOf(Character.toChars(176)), "");
					EllowObject obj = new EllowObject("Object");
					objPad.obj = obj;
					objPad.refreshContent();
					objPad.wasSelected = true;
					setPadFocus(objPad, 0);
					if (pad.obj != null) {
						if (pad.obj.parentObj != null)
							pad.obj.parentObj.addChild(obj);
					} else if (pad.attribute != null)
						pad.attribute.parentObj.addChild(obj);
					else if (pad.parentStatement != null
							&& pad.parentStatement.parentAttribute != null)
						pad.parentStatement.parentAttribute.parentObj
								.addChild(obj);

					int index = dPads.indexOf(pad) + 1;
					dPads.add(index, objPad);

					savetoCode(getEllowComponents(dPads));
					reload();
					return;
				}
			}
		}
		Pad objPad = new Pad("Object", "");
		EllowObject obj = new EllowObject("Object");
		objPad.obj = obj;
		objPad.wasSelected = true;
		setPadFocus(objPad, 0);
		dPads.add(objPad);
		savetoCode(getEllowComponents(dPads));
		reload();
	}

	public void insertAttribute() {
		LinkedList<Pad> dPads = getPads();
		for (Component component : contentPane.getComponents()) {
			if (component instanceof Field) {
				Field field = (Field) component;
				Pad pad = field.getPad();
				if (pad.lastFocused != -1) {
					Pad attrPad = new Pad("Attribute"
							+ String.valueOf(Character.toChars(176)), "");
					Attribute attribute = new Attribute(null, "Attribute");
					attrPad.attribute = attribute;
					attrPad.refreshContent();
					attrPad.wasSelected = true;
					setPadFocus(attrPad, 0);

					if (pad.obj != null)
						pad.obj.addAttribute(attribute);
					if (pad.attribute != null)
						pad.attribute.parentObj.addAttribute(attribute);
					else if (pad.parentStatement != null
							&& pad.parentStatement.parentAttribute != null)
						pad.parentStatement.parentAttribute.parentObj
								.addAttribute(attribute);

					int index = dPads.indexOf(pad) + 1;
					dPads.add(index, attrPad);
				}
			}
		}
		savetoCode(getEllowComponents(dPads));
		reload();
	}

	public void insertStatement() {
		LinkedList<Pad> dPads = getPads();
		for (Component component : contentPane.getComponents()) {
			if (component instanceof Field) {
				Field field = (Field) component;
				Pad pad = field.getPad();
				int index = dPads.indexOf(pad) + 1;

				Pad nextPad = (pad != dPads.getLast()) ? dPads.get(index)
						: null;

				if (pad.lastFocused != -1) {
					if (pad.obj != null || pad.parentStatement != null) {
						return;// do nothing
					}
					if (nextPad != null && nextPad.parentStatement != null) {
						nextPad.string += String
								.valueOf(Character.toChars(176));
						nextPad.refreshContent();
						break;
					}

					Pad stateMentPad = new Pad("Statement"
							+ String.valueOf(Character.toChars(176)), "");
					Statement statement = new Statement("Statement", null,
							stateMentPad);
					stateMentPad.parentStatement = statement;
					stateMentPad.refreshContent();
					stateMentPad.wasSelected = true;
					setPadFocus(stateMentPad, 0);
					if (pad.attribute != null)
						pad.attribute.addStatement(statement);
					else if (pad.parentStatement != null
							&& pad.parentStatement.parentAttribute != null)
						pad.parentStatement.parentAttribute
								.addStatement(statement);

					dPads.add(index, stateMentPad);
				}
			}
		}
		savetoCode(getEllowComponents(dPads));
		reload();
	}

	public static Pad draggedPad;

	public void dragPadto(Field field, Field fieldto, boolean before) {
		int newPos = getPads().indexOf(fieldto.getPad());
		if (!before && newPos < getPads().size() - 1)
			newPos++;
		movePad(field.getPad(), newPos);
	}

	public void insertPadto(Pad pad, Field fieldto, boolean before) {
		int newPos = getPads().indexOf(fieldto.getPad());
		if (!before && newPos < getPads().size() - 1)
			newPos++;
		insertPad(pad, newPos);
	}

	public void movePad(Pad pad, int newPos) {
		LinkedList<Pad> dPad = getPads();
		int index = dPad.indexOf(pad);
		Pad prevPad;
		if (newPos > 0)
			prevPad = (index < newPos) ? dPad.get(newPos) : dPad
					.get(newPos - 1);
		else
			return;
		if (prevPad != null && pad.attribute != null) {
			if (prevPad.attribute != null)
				pad.attribute.changeParent(prevPad.attribute.parentObj);
			else if (prevPad.obj != null)
				pad.attribute.changeParent(prevPad.obj);
			else if (prevPad.parentStatement != null
					&& prevPad.parentStatement.parentAttribute != null)
				pad.attribute
						.changeParent(prevPad.parentStatement.parentAttribute.parentObj);
			else {
				return;
			}
		} else if (prevPad != null && pad.parentStatement != null) {
			if (prevPad.parentStatement != null) {
				pad.parentStatement
						.changeParent(prevPad.parentStatement.parentAttribute);
				pad.refreshContent();
			} else if (prevPad.obj != null)
				return; // do nothing
			else if (prevPad.attribute != null)
				pad.parentStatement.changeParent(prevPad.attribute);
			else
				return;
		} else if (prevPad != null && pad.obj != null) {
			if (prevPad.obj != null) {
				pad.obj.changeParent(prevPad.obj);
				pad.refreshContent();
			} else if (prevPad.attribute != null)
				pad.obj.changeParent(prevPad.attribute.parentObj);
			else if (prevPad.parentStatement != null) {
				Attribute pAttr = prevPad.parentStatement.parentAttribute;
				if (pAttr != null)
					pad.obj.changeParent(pAttr.parentObj);
			}

		} else if (prevPad == null && pad.obj.parentObj != null)
			pad.obj.parentObj.childObjs.remove(pad.obj);
		dPad.remove(pad);
		dPad.add(newPos, pad);
		savetoCode(getEllowComponents(dPad));
		reload();

	}

	public void insertPad(Pad pad, int pos) {
		LinkedList<Pad> dPad = getPads();
		Pad prevPad = (pad != dPad.getFirst()) ? dPad
				.get(dPad.indexOf(pad) - 1) : null;

		if (pad.attribute != null) {
			if (prevPad.attribute != null)
				pad.attribute.changeParent(prevPad.attribute.parentObj);
			else if (prevPad.obj != null)
				pad.attribute.changeParent(prevPad.obj);
			else if (prevPad.parentStatement != null
					&& prevPad.parentStatement.parentAttribute != null)
				pad.attribute
						.changeParent(prevPad.parentStatement.parentAttribute.parentObj);
		}

		dPad.add(pos, pad);
		displayPads(dPad);
		savetoCode();
	}

	MouseAdapter mousyButtonAdapter = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent e) {
			String source = ((JButton) e.getSource()).getText();

			int rx = e.getX() + ((Component) e.getSource()).getParent().getX(), ry = e
					.getY() + ((Component) e.getSource()).getParent().getY();
			Component component = contentPane.getComponentAt(rx, ry);
			setEnabled(true);
			boolean before;
			if (component instanceof Field) {
				if (rx < component.getWidth() / 2 + component.getX())
					before = true;
				else
					before = false;

				Pad pad = new Pad(source, "");
				if (source.equals(btObject.getText())) {
					EllowObject obj = new EllowObject(source);
					pad.obj = obj;
					insertPadto(pad, (Field) component, before);
				}
			}
			super.mouseReleased(e);
		}
	};
	
	public class DDContentPane extends JPanel{
		public int maxWidth = 0, maxHeight = 0;
		
		@Override
		public Dimension getPreferredSize() {
			// TODO Auto-generated method stub
			return new Dimension(maxWidth, maxHeight);
		}
	}
}

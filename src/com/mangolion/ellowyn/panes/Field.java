package com.mangolion.ellowyn.panes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.mangolion.ellowyn.components.Pad;

public class Field extends JTextPane implements FocusListener {

	DDPanel parent;
	private Pad pad;
	static Border border;
	public static final int MAXWIDTH = 450;

	public Field(final DDPanel parent, final Pad pad) {
		this.parent = parent;
		this.setPad(pad);
		setText(pad.string);		//setHorizontalAlignment(CENTER);
		
		if (border == null)
			border = new JTextField().getBorder();
		setBorder(border);
		
		if (pad.parentStatement == null || pad.isReference)
		setBackground(pad.color);
		
		setOpaque(parent.isOpaque());
		
		if (pad.isReference)
			setText((getText().replace("#", "")));
		
		if (getPreferredSize().width<MAXWIDTH)
			setSize(new Dimension((int) (getPreferredSize().width * 1.01), getPreferredSize().height));
		else{
			setSize(new Dimension(MAXWIDTH, getPreferredSize().height));
			setSize(new Dimension(MAXWIDTH, getPreferredSize().height));//setting size the second time, so that the getPreferedSize() return the right height
		}
		addFocusListener(this);
		
		if (pad.string.contains(String.valueOf(Character.toChars(176)))){
			pad.lastFocused = 0;
			pad.wasSelected = true;
		}
		
		if (pad.lastFocused != -1){
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					pad.string = pad.string.replaceAll(String.valueOf(Character.toChars(176)), "");
					setText(pad.string);
					requestFocusInWindow();
					setCaretPosition(pad.lastFocused);
					if (pad.wasSelected){
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								setSelectionStart(0);
								setSelectionEnd(getText().length());
								pad.wasSelected = false;
							}
						});

					}
				}
			});

		}

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
							pad.string = getText();
						parent.setPadFocus(pad, getCaretPosition());
						parent.refresh();
						

					}
				});
				super.keyTyped(e);
			}
		});
		
		addMouseMotionListener(mouseAdapter);
		addMouseListener(mouseAdapter);
	}

	public Pad getPad() {
		return pad;
	}

	public void setPad(Pad pad) {
		this.pad = pad;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		parent.setPadFocus(pad, getCaretPosition());
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		//setBorder(border);
		//setSize(getPreferredSize());
	}
	
	Field self = this;
	private int xx, yy;
	private boolean isPressed = false;
	MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			isPressed = true;
			super.mousePressed(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			xx = e.getX();
			yy = e.getY();
			if (xx < 0 || xx > getWidth()*1.2 || yy < 0 || yy > getHeight()*1.2){
				setEnabled(false);
				DDPanel.draggedPad = pad;
			}else
				setEnabled(true);
			super.mouseMoved(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			isPressed = false;
			int rx = e.getX() + getX(), ry =  e.getY() + getY();
			Component component = parent.contentPane.getComponentAt(rx,ry);
			boolean before;
			if (!isEnabled() && component instanceof Field){
				if (rx < component.getWidth()/2 + component.getX())
					before = true;
				else
					before = false;
				
				parent.dragPadto(self, (Field) component, before);
			}
			setEnabled(true);
			super.mouseReleased(e);
		}
	};
}

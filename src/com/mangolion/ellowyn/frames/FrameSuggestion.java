package com.mangolion.ellowyn.frames;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.panes.PaneEdit;
import com.mangolion.ellowyn.reference.ReferenceItem;

@SuppressWarnings("serial")
public class FrameSuggestion extends JFrame implements KeyListener{
	public static int WIDTH = 100, HEIGHT = 60;
	public DefaultListModel<String> model = new DefaultListModel<String>();
	public JList<String> jList = new JList<>(model);
	LinkedList<ReferenceItem> results;
	PaneEdit pane;

	public FrameSuggestion() {
		setUndecorated(true);
		setOpacity(0.9F);
		setAutoRequestFocus(false);
		JScrollPane scrollPane = new JScrollPane(jList);
		setContentPane(scrollPane);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		jList.addKeyListener(this);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				pane.setUpDownKey(true);
				super.componentHidden(e);
			}
		});
		
		jList.addListSelectionListener(new ListSelectionListener() {
			FrameSuggestionTooltip frameTooltip = new FrameSuggestionTooltip();
			@Override
			public void valueChanged(ListSelectionEvent e) {
					frameTooltip.setSize(0, 0);
					frameTooltip.setLocation(getX() + getWidth(), getY());
					frameTooltip.setVisible(true);
					if (jList.getSelectedIndex() != -1)
						results.get(jList.getSelectedIndex()).listener.onItemTooltip(frameTooltip);
			}
		});
	}

	public void setSuggestions(FrameNote note,LinkedList<ReferenceItem> results_) {
		System.out.println("x " + getX()+ "y "+ getY());
		results = results_;
		if (results == null)
			return;
		
		this.pane = note.paneEdit;
		if (results.size() == 0) {
			setVisible(false);
			return;
		}
		pane.setUpDownKey(false);
		setVisible(true);
		model.clear();
		
		for (ReferenceItem result : results){
			model.addElement(result.toString());
		}
		setSize(getPreferredSize().width, 175);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		//pane.setCaretPosition(pane.lastCaretPos);
		pane.requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			String result = "#" + results.get(jList.getSelectedIndex()).toString() + "# ";
			int pos = pane.lastCaretPos + result.length() - 2;
			pane.replaceWord(pane.getCaretPosition() - 1, result);
			pane.setCaretPosition(pos);
			
			//EllowMain.getSecondDesktop().addComp(results.get(jList.getSelectedIndex()));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

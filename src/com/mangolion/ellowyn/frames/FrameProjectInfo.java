package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyVetoException;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mangolion.ellowyn.panes.PaneView;
import com.mangolion.ellowyn.project.EllowynProject;

public class FrameProjectInfo extends JInternalFrame {
	public EllowynProject project;
	public FrameProjectInfo(final EllowynProject project) {
		super(project.name, true, true, true, true);
		this.project = project;
		setSize(500, 500);
		
		try {
			setMaximum(false);
			setIcon(false);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		setContentPane(tabbedPane);
		
		JPanel panelInfo = new JPanel();
		tabbedPane.addTab("Info", panelInfo);
		panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
		
		JLabel lbName = new JLabel("Project Name:");
		lbName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelInfo.add(lbName);
		
		JLabel lbDateCreated = new JLabel("Date Created:");
		panelInfo.add(lbDateCreated);
		
		final PaneView paneView = new PaneView();
		JScrollPane scrInfo = new JScrollPane(paneView);
		tabbedPane.addTab("Notes", scrInfo);
		paneView.refresh(project.getAllText());
		
		final JPanel panelVis = new JPanel(new BorderLayout());
		tabbedPane.addTab("Visualize", panelVis);
		tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
              /*  if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    if (pane.getSelectedIndex() == 2){
                    	DisplayEllow display = EllowGraph.createDisplayfromObjects(project.name, project.objects);
                    	panelVis.add(display);
                    	display.start();
                    }
                    else panelVis.removeAll();
                }*/
            }
        });
	}
}

package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.extension.ExtensionManager;
import com.mangolion.ellowyn.file.EllowFile;
import com.mangolion.ellowyn.panes.PaneView;
import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.Projects;

public class FrameToolTip extends JFrame {
	private FrameDesktop desktop;
	private JPanel contentPane;
	private Component component;
	
	public FrameToolTip(final FrameDesktop desktop) {
		this.desktop = desktop;
		setUndecorated(true);
		setOpacity(0.9F);
		setAutoRequestFocus(false);
		
		contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);	
	}
	
	public void setText(Component component, EllowynProject project, EllowFile file){
		this.component = component;
		if (component == null)
		return;
		
		contentPane.removeAll();
		setVisible(true);
		
		if (component instanceof Attribute && ((Attribute) component).parentObj.name.equals("R")){
			EllowFile file2 = new EllowFile(Projects.workspace + "\\\\" + project.name + "\\"+  component.toString());
			file2.parentProject = file.parentProject;
			ExtensionManager.triggerToolTip(file2, desktop);
		}
		else{
		PaneView view = new PaneView();
		JScrollPane pane = new JScrollPane(view);
		view.refresh(component.getPad(new LinkedList<Pad>()));
		contentPane.add(pane);
		}

		pack();
	}

	public Component getComponent() {
		// TODO Auto-generated method stub
		return component;
	}
}

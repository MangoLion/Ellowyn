package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.mangolion.ellowyn.project.EllowynProject;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.utility.Utility;

public class FrameProjectReference extends IDEForm{
	ListContentArea list = new ListContentArea();
	FrameProjectReference self = this;
	//LinkedList<EllowynProject> projectsBU = new LinkedList<EllowynProject>();
	public FrameProjectReference(final EllowynProject argProject) {
		super(argProject.name + "'s references");
		getInputMap().clear();
		//projectsBU.addAll(argProject.referenceProjects);
		panelUpper.remove(fieldName);
		for (EllowynProject project: argProject.referenceProjects)
			list.model.addElement(project.name);
		panelContent.add(list);
		setSize(230,300);
		list.btAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameProjectChooser chooser = new FrameProjectChooser(self);
				String result = chooser.showDialog();
				for (int i = 0; i < list.model.size(); i ++)
					if (result.equals(list.model.getElementAt(i)) || result.equals(argProject.name))
						return;
				list.model.addElement(result);
			}
		});
		
		btSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				argProject.referenceProjects.clear();
				for (int i = 0; i < list.model.size(); i ++)
					argProject.referenceProjects.add(Projects.getProject(list.model.getElementAt(i)));
				argProject.rebuild();
				dispose();
			}
		});

	}
	


}
class FrameProjectChooser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JList listProject;
	DefaultListModel<String> model = new DefaultListModel<String>();
	/**
	 * Create the dialog.
	 */
	public FrameProjectChooser(JComponent parent) {
		setTitle("Select Project");
		setLocationRelativeTo(parent);
		setModal(true);
		setSize(new Dimension( 390, 417));
		setLocation(getX() - getWidth()/2, getY() - getHeight()/2);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			for (String str: Projects.getAllProjectNames())
					model.addElement(str);
			listProject = new JList(model);
			listProject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			( (DefaultListCellRenderer) listProject.getCellRenderer()).setHorizontalAlignment(JLabel.CENTER);
			contentPanel.add(listProject);
		}
		{
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String showDialog(){
			setVisible(true);
			if (!listProject.isSelectionEmpty())
				return listProject.getSelectedValue().toString();
			else
				return null;
	}

}

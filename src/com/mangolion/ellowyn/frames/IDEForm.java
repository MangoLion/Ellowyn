package com.mangolion.ellowyn.frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IDEForm extends JInternalFrame {
	public JPanel panelUpper, panelButtons, panelContent;
	public DataField fieldName;
	public JButton btSave, btCancel, btOK;
	public static final int SAVE_MODE = 0, OK_MODE = 1;
	public int mode = SAVE_MODE;
	public LinkedList<DataField> fields = new LinkedList<DataField>();

	public IDEForm(String title, int mode_) {
		super(title, true, true, true, true);
		mode = mode_;
		init();
	}

	public IDEForm(String title) {
		super(title, true, true, true, true);
		init();
	}

	public void init(){
		setVisible(true);
		setLayout(new BorderLayout());
		panelUpper = new JPanel();
		add(panelUpper, BorderLayout.NORTH);
		panelUpper.setLayout(new BoxLayout(panelUpper, BoxLayout.Y_AXIS));
		fieldName = new DataField("Name");
		fields.add(fieldName);
		panelUpper.add(fieldName);
		
		panelContent = new JPanel(new BorderLayout());
		add(panelContent, BorderLayout.CENTER);
		
		panelButtons = new JPanel(new FlowLayout());
		add(panelButtons, BorderLayout.SOUTH);
		
		switch (mode){
			case SAVE_MODE:
				btSave = new JButton("Save");
				btCancel = new JButton("Cancel");
				panelButtons.add(btSave);
				panelButtons.add(btCancel);
				
				btCancel.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			break;
			case OK_MODE:
				btOK = new JButton("OK");
				panelButtons.add(btOK);
			break;
		}
	}

	public void addDataField(DataField field) {
		panelUpper.add(field);
		fields.add(field);
	}
	
	public void addDataField(DataField field, String data) {
		addDataField(field);
		field.tfData.setText(data);
	}
	
	public String getDataField(String str){
		for (DataField field: fields)
			if (str.equals(field.lbName.getText().trim()))
				return field.tfData.getText();
		return "";
	}

	public void setDataField(String fieldName, String data){
		for (DataField field: fields)
			if (fieldName.equals(field.lbName.getText().trim()))
				field.tfData.setText(data);
	}
	
}

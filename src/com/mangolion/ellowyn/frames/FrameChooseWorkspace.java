package com.mangolion.ellowyn.frames;

import images.Icons;
import iniwriter.IniWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.mangolion.ellowyn.main.EllowMain;
import com.mangolion.ellowyn.project.Projects;
import com.mangolion.ellowyn.utility.Utility;



/**
 * The Jframe used to pick the {@link EllowMain#workspace} for the program
 * 
 * @author MangoLion
 *
 */
public class FrameChooseWorkspace extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField tfWorkspace;
	File setting;

	JCheckBox cbUse;
	
	public FrameChooseWorkspace(String path) {
		setting = EllowMain.setting;
		setTitle("Workspace Window");
		setIconImage(Icons.orbIcon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 549, 231);

		// create the GUI
		JComponent contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open(tfWorkspace.getText());
			}
		});
		panel.add(btnSelect);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(btnCancel);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut_1);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		tfWorkspace = new JTextField();
		if (path.equals(""))
			// set the default workspace location to be the My Documents folder
			// of the user
			tfWorkspace.setText(javax.swing.filechooser.FileSystemView
					.getFileSystemView().getDefaultDirectory()
					.getAbsolutePath()
					+ "\\\\Ellowyn Workspace");
		else
			tfWorkspace.setText(path);

		tfWorkspace.setColumns(10);
		panel_3.add(tfWorkspace);

		JButton btnBrowse = new JButton("Browse");
		panel_3.add(btnBrowse);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showChooser(tfWorkspace.getText());
			}
		});

		Component verticalStrut = Box.createVerticalStrut(43);
		panel_1.add(verticalStrut);

		cbUse = new JCheckBox("Always Use this Workspace");
		panel_1.add(cbUse);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.darkGray);
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JLabel lblNewLabel = new JLabel("Select Workspace");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(lblNewLabel);

		JLabel lblEllowynUsesA = new JLabel(
				"Ellowyn Uses a workspace folder to store its projects");
		lblEllowynUsesA.setForeground(Color.WHITE);
		panel_2.add(lblEllowynUsesA);

		JLabel lblPleaseSelectOne = new JLabel("Please select one to continue:");
		lblPleaseSelectOne.setForeground(Color.WHITE);
		panel_2.add(lblPleaseSelectOne);
		Utility.setCenterPosition(this);
		loadSetting();
	}

	protected void loadSetting() {
		if (!setting.exists())
			return;
			boolean autoload = IniWriter.getAutoLoad();
			
			if (autoload){
				String path = IniWriter.getLastWorkSpace();
				File f = new File(path);
				if (!f.exists()){
					tfWorkspace.setText(path);
					return;
				}
				Projects.workspace = f.getAbsolutePath();
				EllowMain.showDesktop();
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						dispose();
					}
				});
			}
	}

	protected void open(String path) {
		// check if the workspace file exist, create one if nessesary
		File f = new File(path);
		if (f.exists() && f.isDirectory()) {
			Projects.workspace = f.getAbsolutePath();
			EllowMain.showDesktop();
			dispose();
		} else if (JOptionPane.showConfirmDialog(getParent(),
				"This folder doesn't exist, create a new one?") == JOptionPane.YES_OPTION) {
			f.mkdirs();
			Projects.workspace = f.getAbsolutePath();
			EllowMain.showDesktop();
			dispose();
		}

		IniWriter.setAutoLoad(cbUse.isSelected());
		IniWriter.setLastWorkSpace(path);
	}

	public void showChooser(String text) {
		// Create a file chooser
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		// In response to a button click:
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			tfWorkspace.setText(file.getAbsolutePath());
		} else {

		}
	}

}

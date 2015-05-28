package images;

import javax.swing.ImageIcon;

import com.mangolion.ellowyn.file.EllowFile;

/**
 * The class containing all of this ide's icons
 */
public class Icons {
	ClassLoader cl;
	public static ImageIcon orbIcon;
	public static ImageIcon newProjIcon;
	public static ImageIcon openIcon;
	public static ImageIcon saveIcon;
	public static ImageIcon saveAllIcon;
	public static ImageIcon frameIcon;
	public static  ImageIcon noteIcon;
	public static ImageIcon folderIcon;
	public static ImageIcon fileIcon;
	public static ImageIcon projectIcon;
	public static ImageIcon infoIcon;
	public static ImageIcon genIcon;
	public static ImageIcon txtIcon;

	public Icons() {
		cl = getClass().getClassLoader();
		newProjIcon = new ImageIcon(cl.getResource("images/newproj.png"));
		openIcon = new ImageIcon(cl.getResource("images/open.png"));
		saveIcon = new ImageIcon(cl.getResource("images/save.png"));
		saveAllIcon = new ImageIcon(cl.getResource("images/saveall.png"));
		orbIcon = new ImageIcon(cl.getResource("images/orb.png"));
		frameIcon = new ImageIcon(cl.getResource("images/note.png"));
		noteIcon = new ImageIcon(cl.getResource("images/note.png"));
		folderIcon = new ImageIcon(cl.getResource("images/folder.png"));
		fileIcon = new ImageIcon(cl.getResource("images/file.png"));
		projectIcon = new ImageIcon(cl.getResource("images/project.png"));
		infoIcon = new ImageIcon(cl.getResource("images/info.png"));
		genIcon = new ImageIcon(cl.getResource("images/gen.png"));
		txtIcon = new ImageIcon(cl.getResource("images/txt.png"));
	}
	
	/**
	 * @param file the {@link EllowFile} that needs an icon
	 * @return the appropriate icon for the file
	 */
	public static ImageIcon getIconforFile(EllowFile file){
		String ext = file.getExtension();
		if (ext.equals("ellowyn"))
			return noteIcon;
		else if (file.isProjectFile){
			return projectIcon;
		}
		else if (ext.equals("gen"))
			return genIcon;
		else if (ext.equals("project"))
			return infoIcon;
		else if (file.isDirectory())
			return folderIcon;
		else
			return fileIcon;
	}
}

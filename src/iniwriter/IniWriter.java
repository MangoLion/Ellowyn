package iniwriter;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.ini4j.Wini;
import org.pushingpixels.substance.api.skin.RavenSkin;

import com.mangolion.ellowyn.main.EllowMain;

public class IniWriter {
	public static final String catLoading = "Loading", catAppearance = "Appearance", valTheme = "theme",
			valDBF = "dynamicbackground", valWorkspace = "lastworkspace", valAutoLoad = "autoload",
			valCatFilter = "catFilter";

	public static Wini wini;

	public static void init() {
		try {
			if (!EllowMain.setting.exists()) {
				EllowMain.setting.createNewFile();
				wini = new Wini(EllowMain.setting);
				setAutoLoad(false);
				setTheme((new RavenSkin()).getDisplayName());
				setDynamicBackground("null");
			}
			wini = new Wini(EllowMain.setting);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setCatFilter(String cat) {
		wini.put(catLoading, valCatFilter, cat);
	}

	public static void setTheme(String theme) {
		wini.put(catAppearance, valTheme, theme);
		store();
	}

	public static String getTheme() {
		return wini.get(catAppearance, valTheme);
	}

	public static void setDynamicBackground(String dbf) {
		wini.put(catAppearance, valDBF, dbf);
		store();
	}

	public static String getDynamicBackground() {
		return wini.get(catAppearance, valDBF);
	}

	public static void setAutoLoad(boolean load) {
		wini.put(catLoading, valAutoLoad, load);
		store();
	}

	public static String getCatFilter() {
		String get = wini.get(catLoading, valCatFilter);
		return (get != null) ? get : "All";
	}

	public static boolean getAutoLoad() {
		return wini.get(catLoading, valAutoLoad, Boolean.class);
	}

	public static void setLastWorkSpace(String ws) {
		wini.put(catLoading, valWorkspace, ws);
		store();
	}

	public static String getLastWorkSpace() {
		return wini.get(catLoading, valWorkspace);
	}

	public static void store() {
		try {
			wini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<String> getOpenWindows() {
		try {
			LinkedList<String> results = new LinkedList<String>();
			results.addAll(FileUtils.readLines(EllowMain.windowsFile));
			FileUtils.write(EllowMain.windowsFile, "");
			return results;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new LinkedList<String>();
	}

	public static void saveOpenWindows(Collection<String> windows) {
		String save = "";
		for (String str : windows)
			save += str + "\n";
		try {
			FileUtils.write(EllowMain.windowsFile, save);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

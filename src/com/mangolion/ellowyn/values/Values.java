package com.mangolion.ellowyn.values;

import java.awt.Color;

public class Values {
	public static final String nextLine = "/n", tab = "/t",
			keyword = "keyword";
	public static String bracketMain = "[", bracketCloseMain = "]",
			reference = "#", statementEnd = ";", colon = ":", comma = ",", objectDef = "~";
	public static String keywords[] = { bracketMain, bracketCloseMain,
			reference, statementEnd, colon, objectDef }, hidenKeywords[] = { nextLine,
			keyword };
	
	public static Color colKeyword = Color.yellow, colValue = Color.lightGray,
			colCondition = Color.cyan, colObj = Color.orange,
			colAttribute = new Color(143, 212, 255),
			colStatement = Color.white, colError = Color.red,
			colFramebg = Color.darkGray, colCaret = Color.white,
			colEdge = Color.decode("#616161");
	public static String supportedReadingExtensions[] = { "ellowyn", "gen",
			 "txt", "xml" };
	public static String supportedAudioExtensions[] = { "mp3", "ogg"};
	public static String supportedImageExtensions[] = { "jpg", "png", "gif"};

	public static void setDarkTheme(boolean dark) {
		if (dark) {
			colKeyword = Color.yellow;
			colValue = Color.lightGray;
			colCondition = Color.cyan;
			colObj = Color.orange;
			colAttribute = new Color(143, 212, 255);
			colStatement = Color.white;
			colError = Color.red;
			colFramebg = Color.darkGray;
			colCaret = Color.white;
			colEdge = Color.decode("#616161");
		}else {
			colKeyword = Color.decode("#857A00");
			colValue = Color.darkGray;
			colCondition = Color.decode("#005257");
			colObj = Color.decode("#A34900");
			colAttribute = Color.decode("#004763");
			colStatement = Color.black;
			/*colError = Color.decode("#5C0000");*/
			colFramebg = Color.white;
			colCaret = Color.black;
			colEdge = Color.decode("#D9D9D9");
		}
	}

	public static boolean isKeyword(String input) {
		for (String key : keywords) {
			if (input.contains(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isReadable(String input) {
		for (String key : supportedReadingExtensions) {
			if (input.contains(key)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isViewable(String input) {
		for (String key : supportedImageExtensions) {
			if (input.contains(key)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlayable(String input) {
		for (String key : supportedAudioExtensions) {
			if (input.contains(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isHiddenKeyword(String input) {
		for (String key : hidenKeywords) {
			if (key.equals(input)) {
				return true;
			}
		}
		return false;
	}

	public static String rtrim(String s) {
		return s.replaceAll("\\s+$", "");
	}
}

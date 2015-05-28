package com.mangolion.ellowyn.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;

public class Utility {
	/**
	 * set the position of the Jframe into the middle of the screen
	 * 
	 * @param frame
	 */
	public static void setCenterPosition(JFrame frame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2
				- frame.getHeight() / 2);
	}
	
	 public static final int getComponentIndex(Component component) {
		    if (component != null && component.getParent() != null) {
		      Container c = component.getParent();
		      for (int i = 0; i < c.getComponentCount(); i++) {
		        if (c.getComponent(i) == component)
		          return i;
		      }
		    }

		    return -1;
		  }
	 
	 public static <T> T getInstance(Class<T> theClass) {

		    try {
				return theClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	 
	 public static <T> T getInstance(Class<T> theClass, Object ... args) {
		    try {
		 	for (Constructor<?> constructor: theClass.getConstructors()){
		 		int length = constructor.getParameterTypes().length;
		 		if (length == args.length){
		 			return (T) constructor.newInstance(args);
		 		}
		 	}

				return theClass.getConstructor().newInstance(args);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	 
	 /**
	  * returns true if item exist in the list
	  * @param list
	  * @param value
	  * @return
	  */
	 public static boolean checkExisting(Collection<String> list,String value){
		 for (String object : list){
			 if (object.equals(value))
				 return true;
		 }
		 return false;
	 }
	 
	 /**
	  * returns true if item exist in the list
	  * @param list
	  * @param value
	  * @return
	  */
	 public static <T> boolean checkExisting(Collection<T> list,T value){
		 for (Object object : list){
			 if (object == value)
				 return true;
		 }
		 return false;
	 }
	 
	 public static <T> void updateList(LinkedList<T> list,T value){
		 for (Object object : list){
			 if (object == value)
				 return;
		 }
		 list.add(value);
	 }
	 
	 public static void renameFile(String oldName, String newName) throws IOException {
		    File srcFile = new File(oldName);
		    boolean bSucceeded = false;
		    try {
		        File destFile = new File(newName);
		        if (destFile.exists()) {
		            if (!destFile.delete()) {
		                throw new IOException(oldName + " was not successfully renamed to " + newName); 
		            }
		        }
		        if (!srcFile.renameTo(destFile))        {
		            throw new IOException(oldName + " was not successfully renamed to " + newName);
		        } else {
		                bSucceeded = true;
		        }
		    } finally {
		          if (bSucceeded) {
		                srcFile.delete();
		          }
		    }
		}
	 
	 public static int getDivisableNum(int num, int div){
		 return Math.round(num / div) * div;
	 }
}

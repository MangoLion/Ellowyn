package plugin;

import java.util.LinkedList;

public class Plugins {
		private static LinkedList<PluginInterFace> plugins = new LinkedList<>();
		public static Function findFunction(String name){
			for (PluginInterFace plugin: plugins){
				for (Class<? extends Function> func: plugin.getFunctions()){
					Function function = Plugin.getInstance(func);
					if (function.getName().equals(name))
						return function;
				}
			}
			return null;
		}
		
		public static LinkedList<String> getAllFunctionNames(){
			LinkedList<String> names = new LinkedList<String>();
			for (PluginInterFace plugin: plugins){
				for (Class<? extends Function> func: plugin.getFunctions()){
					Function function = Plugin.getInstance(func);
						names.add(function.getName());
						System.out.println(function.getName());
				}
			}
			return names;
		}
		
		public static LinkedList<PluginInterFace> getplugins(){
			return plugins;
		}
}

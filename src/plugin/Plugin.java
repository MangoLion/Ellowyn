package plugin;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class Plugin implements PluginInterFace {
	private String name, desc;
	private  LinkedList<Class<? extends Function>> functions = new LinkedList<Class<? extends Function>>();

	public <T extends Function> Plugin(String name, String desc, Class<? extends Function> ... functions) {
		this.name = name;
		this.desc = desc;
		if (functions != null){
			this.functions.addAll(Arrays.asList(functions));
		}
	}

	public String getName() {
		return name;
	}
	public String getDesc(){
		return desc;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<Class<? extends Function>> getFunctions() {
		return functions;
	}
	
	public void setFunctions(LinkedList<Class<? extends Function>> functions) {
		this.functions = functions;
	}
	
	public void addFunction(Class<? extends Function> ... functions){
		if (functions != null){
			this.functions.addAll(Arrays.asList(functions));
		}
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

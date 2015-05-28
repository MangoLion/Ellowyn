package plugin;

import java.util.LinkedList;

import net.xeoh.plugins.base.Plugin;

public abstract interface PluginInterFace extends Plugin{
	public String getName();
	public String getDesc();
	public LinkedList<Class<? extends Function>> getFunctions();
}

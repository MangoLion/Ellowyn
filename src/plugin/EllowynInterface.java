package plugin;

import java.util.LinkedList;

import com.mangolion.ellowyn.components.Pad;

public class EllowynInterface {
	private Plugin plugin;
	
	public EllowynInterface(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public LinkedList<Pad> getNotePads(){
		return null;
	};
	public String getPadString(){
		return null;
	};
	public void writeToPad(String str){};
	
}

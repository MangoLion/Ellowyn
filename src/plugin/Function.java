package plugin;

import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.frames.FrameNote;

public abstract class Function {
	Class<? extends Plugin> plugin;
	
	public void execute(FrameNote note, Pad pad, String ... args){};
	
	private String name;
	private String desc;
	
	public Function(String name, String desc, Class<? extends Plugin> plugin) {
		this.name = name;
		this.desc = desc;
		this.plugin = plugin;
	}

	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setName(String name) {
		this.name = name;
	};
	
	public void writetoPad(FrameNote note, Pad pad, String string){
		note.paneEdit.lastCaretPos += string.length() - pad.string.length()-2;
		pad.string = string;
	}
}

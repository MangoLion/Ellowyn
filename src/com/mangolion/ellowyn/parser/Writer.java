package com.mangolion.ellowyn.parser;

import java.util.LinkedList;

import com.mangolion.ellowyn.components.Component;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Statement;

public class Writer {
	public static String write(LinkedList<? extends Component> llcomponents){
		Component components[] = new Component[llcomponents.size()];		
		for (int i = 0; i < llcomponents.size(); i ++)
			components[i] = llcomponents.get(i);
		return write(components);
	}
	
	public static String write(Component ... components){
		String out = "";
		for (Component component: components){
			if ((component instanceof EllowObject && ((EllowObject)component).parentObj == null) || (component instanceof Statement && ((Statement) component).parentAttribute == null))
				out = component.getCode(out, 1);
			/*out += "[ " + object.name ;
			if (object.attributes.size() > 0)
				out += ": ";
			for (Attribute attribute : object.attributes){
				if (attribute != object.attributes.getFirst())
				out += "\t";
				out += attribute.name;
				
				if (attribute.statements.size() > 0)
					out += ": ";
				for (Statement statement: attribute.statements){
					out += statement.getString();
				}
				out +=  ";";
				if (attribute != object.attributes.getLast())
				out += "\n";
			}
			out += " ]" + "\n";*/
		}
		return out;
	}
}

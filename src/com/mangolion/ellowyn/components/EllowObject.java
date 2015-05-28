package com.mangolion.ellowyn.components;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.mangolion.ellowyn.values.Values;

public class EllowObject extends Component{
	public LinkedList<Attribute> attributes = new LinkedList<Attribute>();
	public LinkedList<EllowObject> childObjs = new LinkedList<EllowObject>();
	public EllowObject parentObj;
	public int level ;
	
	public EllowObject(String name, Attribute ... attributes) {
		this.name = name;
		if (attributes != null){
			this.attributes.addAll(Arrays.asList(attributes));
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public String getCode(String detail, int tab) {
		detail += "[ " + name;
		
		String strTab = "";
		for (int i = 0; i < tab; i++)
			strTab += "\t";
			
		if (attributes.size()>0 || childObjs.size() > 0)
			if ((attributes.size() > 0 && !attributes.getFirst().name.equals(Values.objectDef)) || attributes.size() == 0)
			detail += ": ";
		for (Attribute attribute : attributes){
			if (attributes.size()> 1 && !attribute.name.trim().equals("~")){
				detail += "\n" + strTab ;
			}
			detail = attribute.getCode(detail, tab);
		}
		
		for (EllowObject object : childObjs){
				detail += "\n";
				detail += strTab ;
			detail = object.getCode(detail, tab + 1);
		}
		
		strTab = "";
		for (int i = 0; i < tab-1; i++)
			strTab += "\t";
		if (attributes.size()>1 || childObjs.size() > 1)
			detail += "\n" + strTab;
		
		detail += "]";
		
		if (parentObj != null)
			detail +=  ";";

		detail += "\n";
		return detail;
	}
	
	@Override
	public LinkedList<Pad> getPad(LinkedList<Pad> pads) {
		Pad pad  = new Pad(name, "");
		pad.obj = this;
		pads.add(pad);
		if (attributes.size() > 0 && !attributes.getFirst().name.equals(Values.objectDef))
			pad.string += ": ";
		
		for (Attribute attribute : attributes){
			attribute.getPad(pads);
			if (attribute != attributes.getLast() || childObjs.size() > 0)
				pads.getLast().string  += "\n\t"; 
		}
		for (EllowObject obj : childObjs){
			if (obj == childObjs.getFirst())
				pads.add(new Pad("\n", ""));
			obj.getPad(pads);
			if (obj != childObjs.getLast())
				pads.getLast().string  += "\t"; 
		}
		
		/*for (EllowObject obj : parentObjs){
			obj.getPad(pads);
			if (obj != parentObjs.getLast())
				pads.getLast().string  += "\n\t"; 
		}*/
		
		pads.add(new Pad("\n", ""));
		return pads;
	}

	public void addChild(EllowObject obj) {
		childObjs.add(obj);
		obj.parentObj = this;
	}
	
	public void addAttribute(Attribute attribute){
		attributes.add(attribute);
		attribute.parentObj = this;
	}
	
	public Attribute addAttribute(String name, String content){
		Attribute attribute = new Attribute(this, name);
		Statement statement = new Statement(name, attribute, new Pad(content, ""));
		attribute.addStatement(statement);
		attributes.add(attribute);
		return attribute;
	}

	public void changeParent(EllowObject newParent) {
		if (newParent == this || newParent.parentObj == this)
			return;
		if (parentObj != null)
			parentObj.childObjs.remove(this);
		newParent.addChild(this);
	}
	
	@Override
	public String getReference() {
		String ref = name;
		EllowObject parent = parentObj;
		while (parent != null){
			 ref = parent.name + "'" + ref;
			 parent = parent.parentObj;
		}
		return ref;
	}
	
	public String getAttributeStr(String name){
		for (Attribute attribute: attributes){
			if (attribute.name.equals(name)){
				return attribute.getStatement().trim();
			}
		}
		return "";
	}
	
	public Attribute getAttribute(String name){
		for (Attribute attribute: attributes){
			if (attribute.name.equals(name)){
				return attribute;
			}
		}
		return null;
	}
	
	public EllowObject getSubObject(String name){
		for (EllowObject object: childObjs){
			if (object.name.equals(name)){
				return object;
			}
		}
		return null;
	}

	public EllowObject addChild(String string, Attribute ...attributes) {
		EllowObject child = new EllowObject(string, attributes);
		childObjs.add(child);
		child.parentObj = this;
		return child;
	}
	
	@Override
	public Collection<Component> getSubComponents() {
		Collection<Component> results = new LinkedList<>();
		results.addAll(attributes);
		results.addAll(childObjs);
		return results;
	}
}

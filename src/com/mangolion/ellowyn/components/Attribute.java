package com.mangolion.ellowyn.components;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;

import com.mangolion.ellowyn.values.Values;

public class Attribute extends Component{
	public EllowObject parentObj;
	public LinkedList<Statement> statements = new LinkedList<Statement>();
	public Attribute(EllowObject parent, String name, Statement ... statements) {
		parentObj = parent;
		this.name = name;
		if (statements != null){
			this.statements.addAll(Arrays.asList(statements));
		}
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public String getStatement(){
		String result = "";
		for (Statement statement: statements){
			result += statement.getString();
		}
		return result;
	}
	
	@Override
	public String getCode(String detail, int tab) {
			detail += name;
			if (!name.contains(Values.objectDef) && statements.size() > 0)
				detail += ":";
		
		for (Statement statement : statements){
			if (statement != statements.getFirst())
				detail += "\n";
			detail = statement.getCode(detail, tab);
		}
		detail += ";";
		return detail;
	}
	
	@Override
	public LinkedList<Pad> getPad(LinkedList<Pad> pads) {
		Pad pad = new Pad(name, "");
		pad.attribute = this;
		pads.addLast(pad);
		if (statements.size() > 0)
			pad.string += ": ";
		
		for (Statement statement : statements){
			statement.getPad(pads);
			if (statement != statements.getFirst()){
				pads.getLast().string += "\n";
			}
		}
		return pads;
	}
	 
	public void addStatement(Statement statement){
		statements.add(statement);
		statement.parentAttribute = this;
	}
	
	public void changeParent(EllowObject newParent){
		parentObj.attributes.remove(this);
		newParent.addAttribute(this);
	}
	
	@Override
	public String getReference() {
		String ref = parentObj.getReference() + "'" + name;
		return ref;
	}
	
	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return Values.colAttribute;
	}
}

package com.mangolion.ellowyn.components;

import java.util.LinkedList;

public class Condition extends Component{
	public Statement parentStatement;
	public String name;
	public Condition(Statement statement, String name) {
		this.name = name;
		parentStatement = statement;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public LinkedList<Pad> getPad(LinkedList<Pad> pads) {
		Pad pad  = new Pad(name, "");
		pad.condition = this;
		return pads;
	}
}

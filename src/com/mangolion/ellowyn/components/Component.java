package com.mangolion.ellowyn.components;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

import com.mangolion.ellowyn.reference.Reference;

public class Component {
	public String name;
	
	public Component() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Component(String name) {
		this.name = name;
	}
	
	public String getCode(String out, int tab) {
		return out;
	}
	
	public LinkedList<Pad> getPad(LinkedList<Pad> pads){
		return pads;
	}
	
	public String getReference(){
		return name;
	}
	
	public Color getColor(){
		return new Color(0);
	}
	
	public Collection<Component> getSubComponents(){
		return new LinkedList<>();
	}
}

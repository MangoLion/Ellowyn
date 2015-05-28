package com.mangolion.ellowyn.components;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;

import com.mangolion.ellowyn.values.Values;

public class Statement extends Component{
	public static LinkedList<Condition> conditions = new LinkedList<>();
	public LinkedList<Pad> pads = new LinkedList<Pad>();
	public Attribute parentAttribute;

	public Statement(String name, Attribute parentAttribute, Pad ... pads) {
		this.parentAttribute = parentAttribute;
		if (pads != null){
			this.pads.addAll(Arrays.asList(pads));
		}
	}
	
	public String getString(){
		name = "";
		//System.out.println("[");
		for(Pad pad: pads){
			name += pad.paddling + pad.string;
		//	System.out.println("|" + pad.string +"|");
		}
		return name;
	}
	
	@Override
	public String getCode(String code, int tab){
		//System.out.println(getString() +"|" + parentAttribute +"|");
		if (parentAttribute == null)
			code += "\n";
		code += getString();
		if (code.contains("#")){
			if (code.charAt(code.length()-getString().length()-1) == '\n'){
				code = code.substring(0, code.length()-getString().length()-1) + code.substring(code.length()-getString().length());
				System.out.println("erased");
			}
		}else{
		if (parentAttribute == null)
			code += "\n";
		}
		return code;
	}
	
	@Override
	public LinkedList<Pad> getPad(LinkedList<Pad> pads) {

		Pad pad  = new Pad(getString(), "");
		pad.parentStatement = this;
		pads.add(pad);
		return pads;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getString();
	}

	public void changeParent(Attribute newParent) {
		System.out.println(parentAttribute +"|" + newParent);
		
		if (parentAttribute != null)
			this.parentAttribute.statements.remove(this);
		newParent.addStatement(this);
	}

	public void addPad(Pad pad) {
		pads.add(pad);
		pad.parentStatement = this;
	}
	
	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return Values.colStatement;
	}
}

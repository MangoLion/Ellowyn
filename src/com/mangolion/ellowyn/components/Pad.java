package com.mangolion.ellowyn.components;

import java.awt.Color;

import com.mangolion.ellowyn.values.Values;

public class Pad implements Cloneable {
	public String string, type, paddling = "";
	public int lastFocused = -1, alignOffset = 0, positionInText = 0;
	public EllowObject obj;
	public Condition condition;
	public Attribute attribute;
	public Statement parentStatement;
	public Color color;
	public boolean isKeyword, isReference = false;
	public Component component;
	public boolean wasSelected = false;

	public Pad(String string, String type) {
		this.string = string;
		this.type = type;
		separatePaddling();
	}

	public void refreshType() {
		if (isKeyword && !string.contains("-")) {
			type = "keyword";
			color = Values.colKeyword;
		} else if (attribute != null) {
			type = "attribute";
			color = Values.colAttribute;
			component = attribute;
		} else if (condition != null) {
			type = "condition";
			color = Values.colCondition;
			component = condition;
		} else if (obj != null) {
			type = "object";
			color = Values.colObj;
			component = obj;
		} else if (parentStatement != null) {
			if (isReference) {
				type = "reference";
				color = Values.colCondition;
				//refreshReference();
			} else {
				type = "statement";
				color = Values.colStatement;
			}
			component = parentStatement;
		} else {
			component = null;
			type = "N/A";
			color = Values.colError;
		}
	}

	public void refreshContent() {
		if (isKeyword) {
			isKeyword = Values.isKeyword(string);
		} else if (attribute != null) {
			attribute.name = string;
		} else if (condition != null) {
			condition.name = string;
		} else if (obj != null) {
			obj.name = string;
		} else if (parentStatement != null) {
			/*
			 * if (isReference) { type = "reference"; refreshReference(); } else
			 * { type = "statement"; color = Values.colStatement; } component =
			 * parentStatement;
			 */
		} else {
			/*
			 * component = null; type = "N/A"; color = Values.colError;
			 */
		}

		refreshType();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return string;
	}

	public void separatePaddling() {
		paddling = "";
		while (string.length() > 0) {
			char c = string.charAt(0);
			if (c == ' ' || c == '\t' || c == '\n') {
				string = string.substring(1);
				paddling += c;
			} else
				break;
		}
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

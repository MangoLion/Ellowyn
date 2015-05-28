package com.mangolion.ellowyn.parser;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import com.mangolion.ellowyn.components.Attribute;
import com.mangolion.ellowyn.components.Condition;
import com.mangolion.ellowyn.components.EllowObject;
import com.mangolion.ellowyn.components.Pad;
import com.mangolion.ellowyn.components.Statement;
import com.mangolion.ellowyn.values.Values;

public class Reader {
	static LinkedList<Pad> temp = new LinkedList<Pad>();
	static LinkedList<EllowObject> objs = null;
    static LinkedList<EllowObject> tobjs;
	public static LinkedList<EllowObject> read(LinkedList<Pad> pads) {
		temp.clear();
		LinkedList<Pad> editPads = new LinkedList<>();
		LinkedList<Pad> rp = new LinkedList<>();
		tobjs = new LinkedList<>();
		objs = new LinkedList<EllowObject>();
		for (Pad pad : pads) {
			pad.attribute = null;
			pad.condition = null;
			pad.isKeyword = false;
			pad.isReference = false;
			pad.obj = null;
			if (!Values.isHiddenKeyword(pad.string))
				editPads.add(pad);

			if (pad.string.equals("") && !pad.string.equals("\n"))
				rp.add(pad);
		}

		for (Pad pad : rp) {
			editPads.remove(pad);
		}

		int level = 0;
		EllowObject currentObj = null;
		for (int i = 0; i < editPads.size(); i++) {
			Pad pad = editPads.get(i);
			Pad nextPad = (i + 1 < editPads.size()) ? editPads.get(i + 1)
					: null;
			Pad prevPad = (i - 1 >= 0) ? editPads.get(i - 1) : null;
			Pad lastTemp = (temp.size() > 0 && i - 1 >= 0) ? temp.get(temp
					.size() - 1) : null;

			pad.isKeyword = Values.isKeyword(pad.string);
			
			if (pad.string.trim().equals(Values.objectDef)){
				if (lastTemp != null && lastTemp.obj != null && nextPad != null &&  !Values.isKeyword(nextPad.string)) {
					EllowObject pObj = lastTemp.obj;
					Attribute attribute = new Attribute(pObj, Values.objectDef);
					pad.attribute = attribute;
					temp.add(pad);
					pObj.attributes.add(attribute);
					
					Statement statement = new Statement("", attribute, nextPad);
					nextPad.parentStatement = statement;
					temp.add(nextPad);
					attribute.statements.add(statement);
					continue;
				} else if (prevPad != null){
					if (prevPad.attribute != null && nextPad != null){
						pads.remove(prevPad);
						pads.remove(pad);
						nextPad.string = prevPad.string + pad.string + nextPad.string;
						nextPad.attribute = prevPad.attribute;
						nextPad.attribute.name = nextPad.string;
						nextPad.isKeyword = false;
						System.out.println(nextPad.string);
						continue;
					}else if (prevPad.parentStatement != null){
						prevPad.parentStatement.addPad(pad);
					}
				}
			}
			
			if (nextPad == null || !pad.isKeyword) {
				// if temp and prevpad is null, this pad is the begin of a
				// statement
				if (prevPad == null || temp.size() == 0 && !prevPad.string.contains("[")) {
					//System.out.println(pad.string + "| 1");
					//if (prevPad != null)
					//	System.out.println(prevPad+"|| 1");
					Statement statement = new Statement("", null,pad);
					pad.parentStatement = statement;
				} else if ((prevPad != null && prevPad.parentStatement != null)) {
				//	System.out.println(pad.string + "| 2");
					Statement statement = (lastTemp == null) ? prevPad.parentStatement
							: lastTemp.parentStatement;
					// statement.pads.add(pad);
					// ???????????????????????????????????????????????????????
					if (statement  != null)
					statement.addPad(pad);
					// System.out.println("[" + pad.string);
				}
				continue;
			}
			
			if (nextPad == null)
				continue;
			if (pad.string.contains(Values.bracketMain)) {
				//System.out.println(nextPad);
				if (temp.size() == 0
						|| (temp.size() > 0 && temp.getLast().parentStatement == null)) {
					EllowObject obj = new EllowObject(nextPad.string);
					nextPad.obj = obj;
					nextPad.parentStatement = null;
					if (temp.size() > 0 && tobjs.size()>0) {
						tobjs.getLast().addChild(obj);
						level ++;
					} else {

					}
					temp.add(nextPad);
					objs.add(obj);
					tobjs.add(obj);
					currentObj = obj;
					obj.level = level;
				} else if (lastTemp != null && lastTemp.parentStatement != null && !lastTemp.string.equals("")) {
				//	System.out.println("|"+lastTemp+"|"+lastTemp.parentStatement.getString()+"|");
					Condition condition = new Condition(
							lastTemp.parentStatement,
							nextPad.string);
					nextPad.condition = condition;
					lastTemp.parentStatement.conditions
							.add(condition);
					temp.add(nextPad);
				}
			} else if (pad.string.contains(Values.reference) && nextPad != null) {
				if (prevPad == null
						|| (prevPad != null && prevPad.isReference == false)){
					nextPad.isReference = true;
				}
				if (prevPad != null && prevPad.parentStatement != null){
					nextPad.parentStatement = prevPad.parentStatement;
					pad.parentStatement = prevPad.parentStatement;
				}else if (lastTemp != null && lastTemp.attribute != null) {
					System.out.println(prevPad.string);
					Statement statement = new Statement("", null);
					statement.addPad(nextPad);
					statement.changeParent(lastTemp.attribute);
				}
			}
			/*if (lastTemp == null || lastTemp.isReference == true
					|| prevPad == null)
				continue;*/
			if (pad.string.contains(Values.colon)) {
				if (lastTemp.obj != null && !Values.isKeyword(nextPad.string)) {
					EllowObject pObj = lastTemp.obj;
					Attribute attribute = new Attribute(pObj, nextPad.string);
					nextPad.attribute = attribute;
					temp.add(nextPad);
					pObj.attributes.add(attribute);
				} else if (lastTemp.attribute != null) {
					Statement statement;
					if (prevPad.parentStatement == null)
						statement = new Statement(pad.string, lastTemp.attribute, nextPad);
					else{
						statement = prevPad.parentStatement;
						statement.addPad(nextPad);
					}
					nextPad.parentStatement = statement;
					temp.add(nextPad);
					lastTemp.attribute.statements.add(statement);
				}
			}
			if (pad.string.contains(Values.statementEnd)) {
				clearTemp("statement", true);
				/*
				 * for (Pad tpad: temp) System.out.println(tpad.string + "||");
				 */
				clearTemp("attribute", false);
				/*
				 * for (Pad tpad: temp) System.out.println(tpad.string + "||");
				 */

				// temp.removeLast();
				if (!Values.isKeyword(nextPad.string)
						) {
					EllowObject pObj = null;
					if (lastTemp.attribute != null)
						pObj = lastTemp.attribute.parentObj;
					else if (lastTemp.obj != null)
						pObj = lastTemp.obj;
					else if (lastTemp.parentStatement != null)
						pObj = lastTemp.parentStatement.parentAttribute.parentObj;
					else {
						System.out.println(lastTemp.string + "||||||||||");
						continue;
					}
					Attribute attribute = new Attribute(pObj, nextPad.string);
					nextPad.attribute = attribute;
					temp.add(nextPad);
					pObj.attributes.add(attribute);
				}
			} else if (pad.string.contains(Values.bracketCloseMain)
					&& prevPad != null) {
				if (prevPad.condition != null) {
					temp.remove(prevPad);

				} else {
					if ((lastTemp.attribute != null))
						clearToParentObj(lastTemp.attribute.parentObj);
					else if (lastTemp.obj != null)
						clearToParentObj(lastTemp.obj.parentObj);
					rollBack(level);
					level --;
					// else
					// temp.clear();
					/*
					 * if (prevPad.attribute != null) clearTemp("attribute",
					 * false); else clearTemp("object", true);
					 */
					/*
					 * if (temp.size() > 0) temp.removeLast();
					 */
				}
				// }
			}
		}
		return objs;
	}
	
	public static void rollBack(int level){
		if (tobjs.size()>0)
		if (tobjs.getLast().level == level){
			//System.out.println(tobjs.getLast().name);
			tobjs.removeLast();
			rollBack(level);
			try{
			for (Pad pad :temp){
				if (pad.obj != null && pad.obj.level == level)
					temp.remove(pad);
			}
			}catch(ConcurrentModificationException e){}
		}
	}

	public static void clearToParentObj(EllowObject pobj) {
		if (temp.size() == 0)
			return;
		Pad prevPad = temp.getLast();
		if (prevPad.obj != pobj) {
			// System.out.println(prevPad.string);
			temp.removeLast();
			clearToParentObj(pobj);
		}
	}

	private static void clearTemp(String string, boolean first) {
		if (temp.size() == 0)
			return;

		Pad lastPad = temp.getLast();
		if (string.contains("attribute") && lastPad.attribute != null) {
			temp.remove(lastPad);
			if (!first)
				clearTemp(string, first);
		} else if (string.contains("condition") && lastPad.condition != null) {
			temp.remove(lastPad);
			if (!first)
				clearTemp(string, first);
		} else if (string.contains("object") && lastPad.obj != null) {
			temp.remove(lastPad);
			if (!first)
				clearTemp(string, first);
		} else if (string.contains("statement")
				&& lastPad.parentStatement != null) {
			temp.remove(lastPad);
			if (!first)
				clearTemp(string, first);
		}
	}

	public static LinkedList<Pad> readString(String input) {
		LinkedList<Pad> pads = new LinkedList<Pad>();
		pads.clear();
		String padStr = "";
		for (int i = 0; i < input.length(); i++) {
			// the pad that will be added
			Pad pad = null;
			String chr = String.valueOf(input.charAt(i));
			if (Values.isKeyword(chr)) {
				pad = new Pad(padStr, "");
				pads.add(pad);
				padStr = "";
				pads.add(new Pad(chr, ""));
			} else
				padStr += chr;

			if (i == input.length() - 1) {
				pad = new Pad(padStr, "");
				pads.add(pad);
				padStr = "";
			}
		}
		return pads;
	}

	public static String trimLeft(String s) {
		return s.replaceAll("^\\s+", "");
	}

}

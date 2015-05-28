package com.mangolion.ellowyn.frames;

public class TestFrame extends IDEForm {
	ListContentArea list = new ListContentArea();
	public TestFrame() {
		super("test frame");
		list.model.addElement("apple");
		list.model.addElement("banana");
		panelContent.add(list);
		addDataField(new DataField("Age"));
		addDataField(new DataField("Height"));
		setSize(230,300);
	}
}

package com.mangolion.ellowyn.note;

import java.awt.Component;

public class Tab {
	public String name;
	public Component component;
	public TabListener listener;
	public Tab(String name, Component component, TabListener listener) {
		this.name = name;
		this.component = component;
		this.listener = listener;
	}
}

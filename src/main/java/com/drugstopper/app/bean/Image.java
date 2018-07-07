package com.drugstopper.app.bean;

public class Image {
	private String name;
	private String desc;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Image(String name, String desc) {
		super();
		this.name = name;
		this.desc = desc;
	}
	
}

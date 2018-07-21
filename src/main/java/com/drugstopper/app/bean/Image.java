package com.drugstopper.app.bean;

import java.io.Serializable;

/**
 * @author rpsingh
 *
 */
public class Image implements Serializable , Comparable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	@Override
	public int compareTo(Object o) {
		return this.getName().compareToIgnoreCase(((Image) o).getName());
	}
	
	
}

package com.jonmarx.game;

import java.util.UUID;

/**
 * Just an event
 * @author Jon
 *
 */
public class ECSEvent implements Cloneable {
	private String name;
	private String data;
	private UUID entity;
	
	public ECSEvent(String name, UUID entity, String data) {
		this.name = name;
		this.data = data;
		this.entity = entity;
	}
	
	public String getName() {
		return name;
	}
	
	public String getData() {
		return data;
	}
	
	public UUID getEntity() {
		return entity;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

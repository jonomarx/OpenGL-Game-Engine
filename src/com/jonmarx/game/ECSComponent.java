package com.jonmarx.game;

import java.util.Map;

import org.json.JSONObject;

/**
 * 
 * @author Jon
 *
 */
public abstract class ECSComponent implements Cloneable {
	
	private String name;
	private String[] requirements;
	private Map<String, Class<?>> fields;
	
	public ECSComponent(String name, String[] requirements, Map<String, Class<?>> fields) {
		this.name = name;
		this.fields = fields;
		this.requirements = requirements;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String[] getRequirements() {
		return requirements;
	}
	
	public final Map<String, Class<?>> getFields() {
		return fields;
	}
	
	
	@Override
	public final Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public final void setField(String field, Object value) {
		if(value != null && (!fields.get(field).isInstance(value))) {
			throw new RuntimeException("Wrong type for field. Did not assign. Recieved type: " + value.getClass().getName() + ", Expected: " + fields.get(field).getClass().getName());
		}
		setFieldi(field, value);
	}
	
	public abstract Object getField(String field);
	protected abstract void setFieldi(String field, Object value);
	
	public abstract JSONObject convertToJSON();
	public abstract ECSComponent parseJSON(JSONObject obj);
}

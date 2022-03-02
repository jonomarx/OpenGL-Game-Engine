package com.jonmarx.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A way to make an entity
 * @author Jon
 *
 */
public class ECSEntity {
	private String[] components;
	private Map<String, String> fieldList;
	private UUID id;
	
	/**
	 * override this method so constructor only requires name, id is handled on creation
	 * @param components the list of components
	 * @param name the name of this ECSEntity (when it is created, kind of like seperate objects)
	 * 
	 * eg. if there is an object called "gun" it'll be handled differently, many different objects can be called that
	 * id is unique id for each object like "23" or "56", doesn't matter on what "class" they are
	 */
	public ECSEntity(Set<String> components, String name) {
		Set<String> c = new HashSet<>(components);
		for(String component : c) {
			for(String dep : ECSStorage.getComponentSample(component).getRequirements()) {
				components.add(dep);
			}
		}
		
		this.components = components.toArray(new String[0]);
		createFieldDefinitions();
	}
	
	private void createFieldDefinitions() {
		fieldList = new HashMap<>();
		for(String component : components) {
			for(String field : ECSStorage.getFieldDefinitions(component).keySet()) {
				fieldList.put(field, component);
			}
		}
	}
	
	public final UUID getId() {
		return id;
	}
	
	/**
	 * for use in ECSStorage
	 * @param id
	 */
	protected final void setId(UUID id) {
		this.id = id;
	}
	
	public final Object getField(String fieldName) {
		if(fieldName.equals("id")) return id;
		String componentName = fieldList.get(fieldName);
		return ECSStorage.getComponent(componentName, id).getField(fieldName);
	}
	
	public final void setField(String fieldName, Object value) {
		String componentName = fieldList.get(fieldName);
		ECSStorage.getComponent(componentName, id).setField(fieldName, value);
	}
	
	public final Map<String, String> getFieldDefinitions() {
		return fieldList;
	}
	
	public final String[] getComponents() {
		return components;
	}
	
	public final boolean containsComponent(String component) {
		for(String com : components) {
			if(com.equals(component)) return true;
		}
		return false;
	}
}

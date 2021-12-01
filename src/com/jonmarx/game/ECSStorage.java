package com.jonmarx.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores components
 * @author Jon
 *
 */
public class ECSStorage {
	// structure:
	// components.get(componentName).get(entityId)
	private static Map<String, Map<UUID, ECSComponent>> components = new HashMap<>();
	private static Map<String, ECSComponent> sampleStore = new HashMap<>();
	
	private static Map<UUID, ECSEntity> entities = new HashMap<>();
	
	public static void init() {
		
	}
	
	/**
	 * 
	 * @param base: the object prototype to create
	 * @param data: the data for the object in format key:value
	 */
	public static void createEntity(ECSEntity base, Map<String, Object> data) {
		// go through some crazy way to generate ID: WIP
		UUID id = UUID.randomUUID();
		base.setId(id);
		
		// fields is in format key:component
		Map<String,String> fields = base.getFieldDefinitions();
		// create components, init fields
		for(String component : base.getComponents()) {
			initComponent(component, data, id);
		}
		entities.put(base.getId(), base);
	}
	
	/**
	 * Recursively creates components
	 * @param component
	 * @param data
	 * @param id
	 */
	private static void initComponent(String component, Map<String, Object> data, UUID id) {
		if(getComponent(component, id) != null) return;
		
		ECSComponent newComponent = putComponent(component, id);
		for(String field : newComponent.getFields().keySet()) {
			newComponent.setField(field, data.get(field));
		}
	}
	
	public static ECSEntity getEntity(UUID entity) {
		return entities.get(entity);
	}
	
	public static List<ECSEntity> getEntities() {
		return new LinkedList<ECSEntity>(entities.values());
	}
	
	public static void removeEntity(UUID entity) {
		ECSEntity base = entities.remove(entity);
		for(String component : base.getComponents()) {
			removeComponent(component, entity);
		}
	}
	
	public static Map<String, Class<?>> getFieldDefinitions(String component) {
		return sampleStore.get(component).getFields();
	}
	
	public static void registerComponent(ECSComponent component) {
		components.put(component.getName(), new HashMap<>());
		sampleStore.put(component.getName(), component);
	}
	
	private static ECSComponent putComponent(String componentName, UUID entityId) {
		ECSComponent component;
		try {
			component = (ECSComponent) sampleStore.get(componentName).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		components.get(componentName).put(entityId, component);
		return component;
	}
	
	protected static ECSComponent getComponent(String componentName, UUID entityId) {
		return components.get(componentName).get(entityId);
	}
	
	protected static ECSComponent getComponentSample(String componentName) {
		return sampleStore.get(componentName);
	}
	
	private static void removeComponent(String componentName, UUID entityId) {
		components.get(componentName).remove(entityId);
	}
}
 
package com.jonmarx.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	private static Map<String, Object> globalVariableTable = new HashMap<>();
	
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
	 * Creats an entity from JSON
	 * @param the JSON object used to make the entity
	 */
	public static ECSEntity createEntity(JSONObject json) {
		UUID uuid;
		if(json.isNull("id")) {
			uuid = UUID.randomUUID();
		} else {
			String id = json.getString("id");
			uuid = UUID.fromString(id);
		}
		
		HashSet<String> componentz = new HashSet<>();
		JSONArray array = json.getJSONArray("components");
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject component = array.getJSONObject(i);
			ECSComponent sample = sampleStore.get(component.getString("type"));
			componentz.add(sample.getName());
			
			ECSComponent data = sample.parseJSON(component);
			components.get(sample.getName()).put(uuid, data);
		}
		
		ECSEntity entity = new ECSEntity(componentz, "json-entity");
		entity.setId(uuid);
		entities.put(uuid, entity);
		return entity;
	}
	
	/**
	 * Loads JSON from a file
	 * @param file
	 * @return
	 */
	public static ECSEntity[] createEntities(String file) {
		StringBuilder data = new StringBuilder();
		Scanner sc = new Scanner(ECSStorage.class.getResourceAsStream(file));
		while(sc.hasNextLine()) {
			data.append(sc.nextLine() + "\n");
		}
		
		JSONArray raw = new JSONArray(data.toString());
		ECSEntity[] out = new ECSEntity[raw.length()];
		for(int i = 0; i < raw.length(); i++) {
			out[i] = createEntity(raw.getJSONObject(i));
		}
		
		return out;
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
	
	public static void putVariable(String name, Object variable) {
		globalVariableTable.put(name, variable);
	}
	
	public static Object getVariable(String name) {
		return globalVariableTable.get(name);
	}
	
	public static void deleteVariable(String name) {
		globalVariableTable.remove(name);
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
 
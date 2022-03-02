package com.jonmarx.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ECSEventManager {
	
	private static Map<String, List<ECSEvent>> eventLists = new HashMap<>();
	private static Map<String, Class<?>> events = new HashMap<>();
	
	private static List<ECSSystem> systems = new LinkedList<>();
	
	public static void registerEvent(ECSEvent event) {
		eventLists.put(event.getName(), new LinkedList<>());
		events.put(event.getName(), event.getClass());
	}
	
	public static void addEvent(ECSEvent event) {
		eventLists.get(event.getName()).add(event);
	}
	
	protected static void removeEvent(ECSEvent event) {
		eventLists.get(event.getName()).remove(event);
	}
	
	public static List<ECSEvent> retrieveEvents(String list) {
		return eventLists.get(list);
	}
	
	protected static void flushEvents() {
		for(List<ECSEvent> list : eventLists.values()) {
			list.clear();
		}
	}
	
	public static void addSystem(ECSSystem system) {
		systems.add(system);
	}
	
	public static void overrideSystem(ECSSystem old, ECSSystem system) {
		int pos = systems.indexOf(old);
		systems.remove(pos);
		systems.add(pos, system);
	}
	
	public static void removeSystem(ECSSystem system) {
		systems.remove(systems.indexOf(system));
	}
	
	public static void init() {
		for(ECSSystem system : systems) {
			system.init();
		}
	}
	
	public static void update() {
		for(ECSSystem system : systems) {
			system.update();
		}
	}
}

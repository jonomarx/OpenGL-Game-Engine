package com.jonmarx.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

public abstract class ECSSystem {
	private String name;
	private String[] messageFilter;
	private String[] componentFilter;
	
	private Predicate<ECSEvent> masterRule;
	
	public ECSSystem(String name, String[] messageFilter, String[] componentFilter) {
		this.name = name;
		this.messageFilter = messageFilter;
		this.componentFilter = componentFilter;
		
		compileRule();
	}
	
	private void compileRule() {
		for(String str : componentFilter) {
			Predicate<ECSEvent> rule;
			switch(str.substring(0,1)) {
				case "+":
					rule = a -> ECSStorage.getEntity(a.getEntity()).containsComponent(str);
					break;
				case "-":
					rule = a -> !ECSStorage.getEntity(a.getEntity()).containsComponent(str);
					break;
				default:
					throw new RuntimeException("Hey, the rule isn't a + or -, it is a " + str.substring(0,1));
			}
			if(masterRule == null) {
				masterRule = rule;
				continue;
			}
			masterRule = masterRule.and(rule);
		}
	}
	
	public final String getName() {
		return name;
	}
	
	public final String[] getMessageFilter() {
		return messageFilter;
	}
	
	public final String[] getComponentFilter() {
		return componentFilter;
	}
	
	protected final void update() {
		List<ECSEvent>[] events = (List<ECSEvent>[]) new List[messageFilter.length];
		for(int i = 0; i < messageFilter.length; i++) {
			List<ECSEvent> eventz = ECSEventManager.retrieveEvents(messageFilter[i]);
			if(eventz == null) continue;
			events[i] = new LinkedList<>(eventz);
			events[i].stream().filter(masterRule);
		}
		for(List<ECSEvent> event : events) {
			if(event == null) continue;
			updateI(event);
		}
		updateO();
	}
	
	protected final void consumeEvent(ECSEvent event) {
		ECSEventManager.removeEvent(event);
	}
	
	/**
	 * Update function implemented by subclass, runs per event
	 * @param events
	 */
	protected abstract void updateI(List<ECSEvent> events);
	/**
	 * Update function implemented by subclass, runs every tick
	 */
	protected abstract void updateO();
	
	/**
	 * initializer
	 */
	protected abstract void init();
}

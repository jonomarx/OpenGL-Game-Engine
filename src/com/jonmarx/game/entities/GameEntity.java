package com.jonmarx.game.entities;

import java.util.HashSet;
import java.util.Set;

import com.jonmarx.game.ECSEntity;

public class GameEntity extends ECSEntity {
	
	private static Set<String> components = new HashSet<>();
	static {
		components.add("modelComponent");
	}

	public GameEntity(String name) {
		super(components, name);
	} 
}

package com.jonmarx.game.entities;

import java.util.HashSet;
import java.util.Set;

import com.jonmarx.game.ECSEntity;

public class CollisionGameEntity extends ECSEntity {
	
	private static Set<String> components = new HashSet<>();
	static {
		components.add("collisionComponent");
		components.add("modelComponent");
	}

	public CollisionGameEntity(String name) {
		super(components, name);
	} 
}

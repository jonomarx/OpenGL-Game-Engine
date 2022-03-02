package com.jonmarx.game.systems;

import java.util.LinkedList;
import java.util.List;

import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSEventManager;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;

import glm_.vec3.Vec3;

public class GravitySystem extends ECSSystem {
	
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};
	
	private final Vec3 GRAVITY = new Vec3(0, -0.163f, 0);

	public GravitySystem() {
		super("gravitySystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		
	}

	@Override
	protected void updateO() {
		List<ECSEntity>entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		for(ECSEntity entity : e) {
			if(entity.containsComponent("physicsComponent")) {
				float mass = (Float) entity.getField("mass");
				Vec3 plusForce = GRAVITY.times(mass);
				//entity.setField("force", ((Vec3) entity.getField("force")).plus(plusForce));
			}
		}
	}
	
	@Override
	protected void init() {
		
	}

}

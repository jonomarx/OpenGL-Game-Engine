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
	
	private List<ECSEntity> entities = new LinkedList<>();
	private final Vec3 GRAVITY = new Vec3(0, -0.163f, 0);

	public GravitySystem() {
		super("gravitySystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		for(ECSEvent event : events) {
			ECSEntity entity = ECSStorage.getEntity(event.getEntity());
			entities.remove(entity);
			
			String[] data = event.getData().split("\\|");
			Vec3 position = new Vec3(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2])).plus(GRAVITY);
			event.setData(position.getX() + "|" + position.getY() + "|" + position.getZ());
		}
	}

	@Override
	protected void updateO() {
		for(ECSEntity entity : entities) {
			ECSEvent event = new ECSEvent("move", entity.getId(), "0|-0.163|0");
			ECSEventManager.addEvent(event);
		}
		
		entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		for(ECSEntity entity : e) {
			if(!entity.containsComponent("collisionComponent")) {
				entities.remove(entity);
			}
		}
	}
	
	@Override
	protected void init() {
		
	}

}

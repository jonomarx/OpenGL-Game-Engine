package com.jonmarx.game.systems;

import java.util.List;

import glm_.vec3.Vec3;

import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;

public class MovementSystem extends ECSSystem {
	private static String[] messageFilter = new String[] {"move"};
	private static String[] componentFilter = new String[] {"+positionComponent"};

	public MovementSystem() {
		super("movementSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		for(ECSEvent event : events) {
			String[] data = event.getData().split("\\|");
			float x = Float.parseFloat(data[0]);
			float y = Float.parseFloat(data[1]);
			float z = Float.parseFloat(data[2]);
			
			ECSEntity entity = ECSStorage.getEntity(event.getEntity());
			Vec3 position = ((Vec3) entity.getField("position")).plus(x,y,z);
			entity.setField("position", position);
			entity.setField("velocity", new Vec3(x,y,z));
			
			consumeEvent(event);
		}
	}
	
	@Override
	protected void updateO() {
		
	}
	
	@Override
	protected void init() {
		
	}
}

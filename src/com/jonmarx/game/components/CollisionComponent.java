package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

public class CollisionComponent extends ECSComponent {
	private Vec3 hitbox;
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("hitbox", Vec3.class);
	}

	public CollisionComponent() {
		super("collisionComponent", new String[] {"positionComponent"}, fields);
	}

	@Override
	public Object getField(String field) {
		switch(field) {
			case "hitbox":
				return hitbox;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		switch(field) {
			case "hitbox":
				hitbox = (Vec3) value;
				break;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW: " + field + " is not part of PositionComponent");
		}
	}
}

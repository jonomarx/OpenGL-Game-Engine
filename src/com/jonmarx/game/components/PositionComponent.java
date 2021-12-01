package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

public class PositionComponent extends ECSComponent {
	
	private Vec3 position;
	private Vec3 rotation;
	private Vec3 scale;
	
	private Vec3 velocity;
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("position", Vec3.class);
		fields.put("rotation", Vec3.class);
		fields.put("scale", Vec3.class);
		fields.put("velocity", Vec3.class);
	}

	public PositionComponent() {
		super("positionComponent", new String[0], fields);
	}

	@Override
	public Object getField(String field) {
		switch(field) {
			case "position":
				return position;
			case "rotation":
				return rotation;
			case "scale":
				return scale;
			case "velocity":
				return velocity;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		switch(field) {
			case "position":
				position = (Vec3) value;
				break;
			case "scale":
				scale = (Vec3) value;
				break;
			case "rotation":
				rotation = (Vec3) value;
				break;
			case "velocity":
				velocity = (Vec3) value;
				break;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW: " + field + " is not part of PositionComponent");
		}
	}

}

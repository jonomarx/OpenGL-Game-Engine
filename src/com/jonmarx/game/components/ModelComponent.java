package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

public class ModelComponent extends ECSComponent {
	
	private String model;
	private String shader;
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("model", String.class);
		fields.put("shader", String.class);
	}

	public ModelComponent() {
		super("modelComponent", new String[] {"positionComponent"}, fields);
	}

	@Override
	public Object getField(String field) {
		switch(field) {
			case "model":
				return model;
			case "shader":
				return shader;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		switch(field) {
			case "model":
				model = (String) value;
				break;
			case "shader":
				shader = (String) value;
				break;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

}

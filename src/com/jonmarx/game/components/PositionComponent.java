package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
	
	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		
		JSONObject position = new JSONObject();
		position.put("x", this.position.getX());
		position.put("y", this.position.getY());
		position.put("z", this.position.getZ());
		JSONObject rotation = new JSONObject();
		rotation.put("x", this.rotation.getX());
		rotation.put("y", this.rotation.getY());
		rotation.put("z", this.rotation.getZ());
		JSONObject scale = new JSONObject();
		scale.put("x", this.scale.getX());
		scale.put("y", this.scale.getY());
		scale.put("z", this.scale.getZ());
		JSONObject velocity = new JSONObject();
		velocity.put("x", this.velocity.getX());
		velocity.put("y", this.velocity.getY());
		velocity.put("z", this.velocity.getZ());
		
		out.put("type", "positionComponent");
		out.put("position", position);
		out.put("rotation", rotation);
		out.put("scale", scale);
		out.put("velocity", velocity);
		
		return out;
	}
	
	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		JSONObject position = obj.getJSONObject("position");
		JSONObject rotation = obj.getJSONObject("rotation");
		JSONObject scale = obj.getJSONObject("scale");
		JSONObject velocity = obj.getJSONObject("velocity");
		
		PositionComponent out = new PositionComponent();
		out.position = new Vec3(position.getFloat("x"), position.getFloat("y"), position.getFloat("z"));
		out.rotation = new Vec3(rotation.getFloat("x"), rotation.getFloat("y"), rotation.getFloat("z"));
		out.scale = new Vec3(scale.getFloat("x"), scale.getFloat("y"), scale.getFloat("z"));
		out.velocity = new Vec3(velocity.getFloat("x"), velocity.getFloat("y"), velocity.getFloat("z"));
		
		return out;
	}
}

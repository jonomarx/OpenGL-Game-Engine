package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
	
	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		
		JSONObject hitbox = new JSONObject();
		hitbox.put("x", this.hitbox.getX());
		hitbox.put("y", this.hitbox.getY());
		hitbox.put("z", this.hitbox.getZ());
		
		out.put("type", "collisionComponent");
		out.put("hitbox", hitbox);
		
		return out;
	}
	
	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		JSONObject hitbox = obj.getJSONObject("hitbox");
		
		CollisionComponent out = new CollisionComponent();
		out.hitbox = new Vec3(hitbox.getFloat("x"), hitbox.getFloat("y"), hitbox.getFloat("z"));
		
		return out;
	}
}

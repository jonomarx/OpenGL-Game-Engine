package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

public class PhysicsComponent extends ECSComponent {
	
	private float mass;
	private Vec3 force = new Vec3(0,0,0);
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("mass", Float.class);
		fields.put("force", Vec3.class);
	}

	public PhysicsComponent() {
		super("physicsComponent", new String[] {"positionComponent"}, fields);
	}

	@Override
	public Object getField(String field) {
		switch(field) {
			case "mass":
				return mass;
			case "force":
				return force;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		switch(field) {
			case "mass":
				mass = (Float) value;
				break;
			case "force":
				force = (Vec3) value;
				break;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}
	
	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		
		out.put("type", "modelComponent");
		out.put("mass", mass);
		
		JSONObject force = new JSONObject();
		force.put("x", this.force.getX());
		force.put("y", this.force.getY());
		force.put("z", this.force.getZ());
		out.put("force", force);
		
		return out;
	}
	
	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		PhysicsComponent out = new PhysicsComponent();
		out.mass = obj.getFloat("mass");
		if(!obj.isNull("force")) {
			JSONObject force = obj.getJSONObject("force");
			out.force = new Vec3(force.getFloat("x"), force.getFloat("y"), force.getFloat("z"));
		}
		return out;
	}
}

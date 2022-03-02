package com.jonmarx.game.components;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.lwjgl.system.MemoryUtil;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alGetSource3f;
import static org.lwjgl.openal.AL11.*;

public class AudioListenerComponent extends ECSComponent {
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("AL_GAIN", Float.class);
		fields.put("AL_POSITION", Vec3.class);
		fields.put("AL_VELOCITY", Vec3.class);
		fields.put("AL_ORIENTATION", float[].class);
	}
	
	public AudioListenerComponent() {
		super("audioListenerComponent", new String[] {}, fields);
	}

	@Override
	public Object getField(String field) {
		switch(field) {
			case "AL_GAIN":
				return alGetListenerf(AL_GAIN);
			case "AL_POSITION":
				FloatBuffer x = MemoryUtil.memAllocFloat(1);
				FloatBuffer y = MemoryUtil.memAllocFloat(1);
				FloatBuffer z = MemoryUtil.memAllocFloat(1);
				alGetListener3f(AL_POSITION, x, y, z);
				Vec3 out = new Vec3(x.get(), y.get(), z.get());
				MemoryUtil.memFree(x);
				MemoryUtil.memFree(y);
				MemoryUtil.memFree(z);
				return out;
			case "AL_VELOCITY":
				x = MemoryUtil.memAllocFloat(1);
				y = MemoryUtil.memAllocFloat(1);
				z = MemoryUtil.memAllocFloat(1);
				alGetListener3f(AL_VELOCITY, x, y, z);
				out = new Vec3(x.get(), y.get(), z.get());
				MemoryUtil.memFree(x);
				MemoryUtil.memFree(y);
				MemoryUtil.memFree(z);
				return out;
			case "AL_ORIENTATION":
				FloatBuffer data = MemoryUtil.memAllocFloat(6);
				alGetListenerfv(AL_ORIENTATION, data);
				float[] outt = data.array();
				MemoryUtil.memFree(data);
				return outt;
			default:
				throw new RuntimeException("Oops. " + field + " is not in here");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		switch(field) {
			case "AL_GAIN":
				alListenerf(AL_GAIN, (float) value);
				break;
			case "AL_POSITION":
				Vec3 val = (Vec3) value;
				alListener3f(AL_POSITION, val.getX(), val.getY(), val.getZ());
				break;
			case "AL_VELOCITY":
				val = (Vec3) value;
				val = val.times(10);
				alListener3f(AL_VELOCITY, val.getX(), val.getY(), val.getZ());
				break;
			case "AL_ORIENTATION":
				float[] data = (float[]) value;
				alListenerfv(AL_ORIENTATION, data);
				break;
			default:
				throw new RuntimeException("Oops. " + field + " is not in here");
		}
	}

	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		out.put("type", "AudioListenerComponent");
		out.put("AL_GAIN", (float) getField("AL_GAIN"));
		
		Vec3 pos = (Vec3) getField("AL_POSITION");
		JSONObject position = new JSONObject();
		position.put("x", pos.getX());
		position.put("y", pos.getY());
		position.put("z", pos.getZ());
		Vec3 vel = (Vec3) getField("AL_VELOCITY");
		JSONObject velocity = new JSONObject();
		velocity.put("x", vel.getX());
		velocity.put("y", vel.getY());
		velocity.put("z", vel.getZ());
		
		float[] orient = (float[]) getField("AL_ORIENTATION");
		JSONObject orientation = new JSONObject();
		for(int i = 0; i < 6; i++) {
			orientation.put(""+i, orient[i]);
		}
		
		out.put("AL_POSITION", position);
		out.put("AL_VELOCITY", velocity);
		out.put("AL_ORIENTATION", orientation);
		
		return null;
	}

	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		AudioListenerComponent out = new AudioListenerComponent();
		out.setFieldi("AL_GAIN", obj.getFloat("AL_GAIN"));
		if(!obj.isNull("AL_POSITION")) {
			JSONObject data = obj.getJSONObject("AL_POSITION");
			out.setFieldi("AL_POSITION", new Vec3(data.getFloat("x"), data.getFloat("y"), data.getFloat("z")));
		}
		if(!obj.isNull("AL_VELOCITY")) {
			JSONObject data = obj.getJSONObject("AL_VELOCITY");
			out.setFieldi("AL_POSITION", new Vec3(data.getFloat("x"), data.getFloat("y"), data.getFloat("z")));
		}
		if(!obj.isNull("AL_ORIENTATION")) {
			JSONObject data = obj.getJSONObject("AL_ORIENTATION");
			out.setFieldi("AL_ORIENTATION", new float[] {data.getFloat("0"),data.getFloat("1"),data.getFloat("2"),data.getFloat("3"),data.getFloat("4"),data.getFloat("5")});
		}
		return out;
	}

}

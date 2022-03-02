package com.jonmarx.game.components;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.lwjgl.system.MemoryUtil;

import com.jonmarx.game.ECSComponent;

import glm_.vec3.Vec3;

import static org.lwjgl.openal.AL11.*;

/**
 * Provides low level audio settings, preferable to write a better system ontop
 * @author jon
 *
 */
public class AudioComponent extends ECSComponent {
	
	private int buffer;
	
	private static String[] writeFields = {
			"AL_PITCH",
			"AL_GAIN",
			"AL_MAX_DISTANCE",
			"AL_ROLLOFF_FACTOR",
			"AL_REFERENCE_DISTANCE",
			"AL_MIN_GAIN",
			"AL_MAX_GAIN",
			"AL_CONE_INNER_ANGLE",
			"AL_CONE_OUTER_ANGLE",
			"AL_SOURCE_RELATIVE",
			"AL_SOURCE_TYPE",
			"AL_LOOPING",
			"AL_BUFFER",
			"AL_SOURCE_STATE",
			"AL_BYTE_OFFSET"
	};
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	static {
		fields.put("AL_PITCH", Float.class);
		fields.put("AL_GAIN", Float.class);
		fields.put("AL_MAX_DISTANCE", Float.class);
		fields.put("AL_ROLLOFF_FACTOR", Float.class);
		fields.put("AL_REFERENCE_DISTANCE", Float.class);
		fields.put("AL_MIN_GAIN", Float.class);
		fields.put("AL_MAX_GAIN", Float.class);
		fields.put("AL_CONE_OUTER_GAIN", Float.class);
		fields.put("AL_CONE_INNER_ANGLE", Float.class);
		fields.put("AL_CONE_OUTER_ANGLE", Float.class);
		fields.put("AL_POSITION", Vec3.class);
		fields.put("AL_VELOCITY", Vec3.class);
		fields.put("AL_DIRECTION", Vec3.class);
		fields.put("AL_SOURCE_RELATIVE", Integer.class);
		fields.put("AL_SOURCE_TYPE", Integer.class);
		fields.put("AL_LOOPING", Integer.class);
		fields.put("AL_BUFFER", Integer.class);
		fields.put("AL_SOURCE_STATE", Integer.class);
		fields.put("AL_BUFFERS_QUEUED", Integer.class);
		fields.put("AL_BUFFERS_PROCESSED", Integer.class);
		fields.put("AL_SEC_OFFSET", Float.class);
		fields.put("AL_SAMPLE_OFFSET", Float.class);
		fields.put("AL_BYTE_OFFSET", Float.class);
		
		fields.put("audio_id", Integer.class);
	}

	public AudioComponent() {
		super("audioComponent", new String[] {}, fields);
	}

	@Override
	public Object getField(String field) {
		if(buffer == 0) buffer = alGenSources();
		switch(field) {
			case "AL_PITCH":
				return alGetSourcef(buffer, AL_PITCH);
			case "AL_GAIN":
				return alGetSourcef(buffer, AL_GAIN);
			case "AL_MAX_DISTANCE":
				return alGetSourcef(buffer, AL_MAX_DISTANCE);
			case "AL_ROLLOFF_FACTOR":
				return alGetSourcef(buffer, AL_ROLLOFF_FACTOR);
			case "AL_REFERENCE_DISTANCE":
				return alGetSourcef(buffer, AL_REFERENCE_DISTANCE);
			case "AL_MIN_GAIN":
				return alGetSourcef(buffer, AL_MIN_GAIN);
			case "AL_MAX_GAIN":
				return alGetSourcef(buffer, AL_MAX_GAIN);
			case "AL_CONE_OUTER_GAIN":
				return alGetSourcef(buffer, AL_CONE_OUTER_GAIN);
			case "AL_CONE_INNER_ANGLE":
				return alGetSourcef(buffer, AL_CONE_INNER_ANGLE);
			case "AL_CONE_OUTER_ANGLE":
				return alGetSourcef(buffer, AL_CONE_OUTER_ANGLE);
			case "AL_SEC_OFFSET":
				return alGetSourcef(buffer, AL_SEC_OFFSET);
			case "AL_SAMPLE_OFFSET":
				return alGetSourcef(buffer, AL_SAMPLE_OFFSET);
			case "AL_BYTE_OFFSET":
				return alGetSourcef(buffer, AL_BYTE_OFFSET);
				
			case "AL_SOURCE_RELATIVE":
				return alGetSourcei(buffer, AL_SOURCE_RELATIVE);
			case "AL_SOURCE_TYPE":
				return alGetSourcei(buffer, AL_SOURCE_TYPE);
			case "AL_LOOPING":
				return alGetSourcei(buffer, AL_LOOPING);
			case "AL_BUFFER":
				return alGetSourcei(buffer, AL_BUFFER);
			case "AL_SOURCE_STATE":
				return alGetSourcei(buffer, AL_SOURCE_STATE);
			case "AL_BUFFERS_QUEUED":
				return alGetSourcei(buffer, AL_BUFFERS_QUEUED);
			case "AL_BUFFERS_PROCESSED":
				return alGetSourcei(buffer, AL_BUFFERS_PROCESSED);
				
			case "AL_POSITION":
				FloatBuffer x = MemoryUtil.memAllocFloat(1);
				FloatBuffer y = MemoryUtil.memAllocFloat(1);
				FloatBuffer z = MemoryUtil.memAllocFloat(1);
				alGetSource3f(buffer, AL_POSITION, x, y, z);
				Vec3 out = new Vec3(x.get(), y.get(), z.get());
				MemoryUtil.memFree(x);
				MemoryUtil.memFree(y);
				MemoryUtil.memFree(z);
				return out;
			case "AL_VELOCITY":
				x = MemoryUtil.memAllocFloat(1);
				y = MemoryUtil.memAllocFloat(1);
				z = MemoryUtil.memAllocFloat(1);
				alGetSource3f(buffer, AL_VELOCITY, x, y, z);
				out = new Vec3(x.get(), y.get(), z.get());
				MemoryUtil.memFree(x);
				MemoryUtil.memFree(y);
				MemoryUtil.memFree(z);
				return out;
			case "AL_DIRECTION":
				x = MemoryUtil.memAllocFloat(1);
				y = MemoryUtil.memAllocFloat(1);
				z = MemoryUtil.memAllocFloat(1);
				alGetSource3f(buffer, AL_DIRECTION, x, y, z);
				out = new Vec3(x.get(), y.get(), z.get());
				MemoryUtil.memFree(x);
				MemoryUtil.memFree(y);
				MemoryUtil.memFree(z);
				return out;
				
			case "audio_id":
				return buffer;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}

	@Override
	protected void setFieldi(String field, Object value) {
		if(buffer == 0) buffer = alGenSources();
		switch(field) {
			case "AL_PITCH":
				alSourcef(buffer, AL_PITCH, (Float) value);
				break;
			case "AL_GAIN":
				alSourcef(buffer, AL_GAIN, (Float) value);
				break;
			case "AL_MAX_DISTANCE":
				alSourcef(buffer, AL_MAX_DISTANCE, (Float) value);
				break;
			case "AL_ROLLOFF_FACTOR":
				alSourcef(buffer, AL_ROLLOFF_FACTOR, (Float) value);
				break;
			case "AL_REFERENCE_DISTANCE":
				alSourcef(buffer, AL_REFERENCE_DISTANCE, (Float) value);
				break;
			case "AL_MIN_GAIN":
				alSourcef(buffer, AL_MIN_GAIN, (Float) value);
				break;
			case "AL_MAX_GAIN":
				alSourcef(buffer, AL_MAX_GAIN, (Float) value);
				break;
			case "AL_CONE_OUTER_GAIN":
				alSourcef(buffer, AL_CONE_OUTER_GAIN, (Float) value);
				break;
			case "AL_CONE_INNER_ANGLE":
				alSourcef(buffer, AL_CONE_INNER_ANGLE, (Float) value);
				break;
			case "AL_CONE_OUTER_ANGLE":
				alSourcef(buffer, AL_CONE_OUTER_ANGLE, (Float) value);
				break;
			case "AL_SEC_OFFSET":
				alSourcef(buffer, AL_SEC_OFFSET, (Float) value);
				break;
			case "AL_SAMPLE_OFFSET":
				alSourcef(buffer, AL_SAMPLE_OFFSET, (Float) value);
				break;
			case "AL_BYTE_OFFSET":
				alSourcef(buffer, AL_BYTE_OFFSET, (Float) value);
				
			case "AL_SOURCE_RELATIVE":
				alSourcei(buffer, AL_SOURCE_RELATIVE, (Integer) value);
				break;
			case "AL_LOOPING":
				alSourcei(buffer, AL_LOOPING, (Integer) value);
				break;
			case "AL_BUFFER":
				alSourcei(buffer, AL_BUFFER, (Integer) value);
				break;
			case "AL_SOURCE_STATE":
				alSourcei(buffer, AL_SOURCE_STATE, (Integer) value);
				break;
				
			case "AL_POSITION":
				Vec3 val = (Vec3) value;
				alSource3f(buffer, AL_POSITION, val.getX(), val.getY(), val.getZ());
				break;
			case "AL_VELOCITY":
				val = (Vec3) value;
				alSource3f(buffer, AL_VELOCITY, val.getX(), val.getY(), val.getZ());
				break;
			case "AL_DIRECTION":
				val = (Vec3) value;
				alSource3f(buffer, AL_DIRECTION, val.getX(), val.getY(), val.getZ());
				return;
			default:
				throw new RuntimeException("THIS SHOULDNT HAPPEN BTW");
		}
	}
	
	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		
		out.put("type", "audioComponent");
		for(String str : writeFields) {
			Object value = getField(str);
			if(value instanceof Float) out.put(str, (Float) value);
			if(value instanceof Integer) out.put(str, (Integer) value);
		}
		
		Vec3 pos = (Vec3) getField("AL_POSITION");
		Vec3 vel = (Vec3) getField("AL_VELOCITY");
		Vec3 dir = (Vec3) getField("AL_DIRECTION");
		
		JSONObject position = new JSONObject();
		position.put("x", pos.getX());
		position.put("y", pos.getY());
		position.put("z", pos.getZ());
		JSONObject velocity = new JSONObject();
		velocity.put("x", vel.getX());
		velocity.put("y", vel.getY());
		velocity.put("z", vel.getZ());
		JSONObject direction = new JSONObject();
		direction.put("x", dir.getX());
		direction.put("y", dir.getY());
		direction.put("z", dir.getZ());
		
		out.put("AL_POSITION", position);
		out.put("AL_VELOCITY", velocity);
		out.put("AL_DIRECTION", direction);
		
		return out;
	}
	
	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		AudioComponent out = new AudioComponent();
		for(String str : writeFields) {
			if(obj.isNull(str)) continue;
			switch(fields.get(str).getSimpleName()) {
				case "Float":
					Float f = obj.getFloat(str);
					out.setFieldi(str, f);
					break;
				case "Integer":
					Integer i = obj.getInt(str);
					out.setFieldi(str, i);
					break;
				case "Vec3":
					JSONObject inner = obj.getJSONObject(str);
					out.setFieldi(str, new Vec3(inner.getFloat("x"), inner.getFloat("y"), inner.getFloat("z")));
					break;
			}
		}
		return out;
	}
}

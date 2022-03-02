package com.jonmarx.game.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jonmarx.game.ECSComponent;

public class LoopedAudioComponent extends ECSComponent {
	
	private static Map<String, Class<?>> fields = new HashMap<>();
	
	// list of references
	private List<String> buffers = new LinkedList<>();
	private int spot = 0;
	
	static {
		fields.put("buffers", List.class);
		fields.put("audio_spot", Integer.class);
	}
	
	public LoopedAudioComponent() {
		super("loopedAudioComponent", new String[] {}, fields);
	}

	@Override
	public Object getField(String field) {
		if(field.equals("buffers")) return buffers;
		if(field.equals("audio_spot")) return spot;
		return null;
	}

	@Override
	protected void setFieldi(String field, Object value) {
		if(field.equals("audio_spot")) {
			spot = (Integer) value;
			return;
		}
		throw new RuntimeException("plz dont edit");
	}

	@Override
	public JSONObject convertToJSON() {
		JSONObject out = new JSONObject();
		JSONArray list = new JSONArray();
		for(String str : buffers) {
			list.put(str);
		}
		out.put("buffers", list);
		out.put("audio_spot", spot);
		
		return null;
	}

	@Override
	public ECSComponent parseJSON(JSONObject obj) {
		LoopedAudioComponent out = new LoopedAudioComponent();
		JSONArray bufs = obj.getJSONArray("buffers");
		for(int i = 0; i < bufs.length(); i++) {
			out.buffers.add(bufs.getString(i));
		}
		out.spot = obj.getInt("audio_spot");
		return out;
	}

}

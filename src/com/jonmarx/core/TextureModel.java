package com.jonmarx.core;

import java.util.HashMap;
import java.util.List;

import org.lwjgl.assimp.AIScene;

/**
 * its a model, except multiple textures can be loaded into it
 * and be swapped :D
 * @author Jon
 */
public class TextureModel extends Model {
	protected HashMap<String, List<Texture>> textureMap = new HashMap<>();
	protected String activeTextures = "default";

	public TextureModel(AIScene scene, String fileName, String basePath) {
		super(scene, fileName, basePath);
		textureMap.put("default", textures);
	}
	
	/**
	 * Convert a model into a texture model
	 * @param model
	 */
	public TextureModel(Model model) {
		this.animator = model.animator;
		this.boneCounter = model.boneCounter;
		this.mBoneInfoMap = model.mBoneInfoMap;
		this.meshes = model.meshes;
		this.path = model.path;
		this.textures = model.textures;
	}
	
	public void addTextures(String name, List<Texture> textures) {
		textureMap.put(name, textures);
	}
	
	public void swapTextures(String map) {
		if(textureMap.get(map) != null) {
			textures = textureMap.get(map);
			activeTextures = map;
		}
	}
	
	public String getActiveTexture() {
		return activeTextures;
	}
}

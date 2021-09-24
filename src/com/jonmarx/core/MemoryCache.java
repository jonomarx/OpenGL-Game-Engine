package com.jonmarx.core;

import java.util.HashMap;

/**
 * Stores Models and Shaders for later use
 * @author Jon
 *
 */
public class MemoryCache {
    private static HashMap<String, Model> modelResources = new HashMap<>();
    private static HashMap<String, Shader> shaderResources = new HashMap<>();
    
    // i think its called a proxy?
    private static HashMap<String, String> modelProxies = new HashMap<>();
    private static HashMap<String, String> animInfo = new HashMap<>();
    
    private static HashMap<String, String> shaderProxies = new HashMap<>();
    
    public static void registerModel(String modelName, String resourceLocation) {
        modelProxies.put(modelName, resourceLocation);
    }
    
    public static void registerModel(String modelName, String resourceLocation, String animationName) {
        modelProxies.put(modelName, resourceLocation);
        animInfo.put(modelName, animationName);
    }
    
    public static void registerShader(String shaderName, String resourceLocation) {
    	shaderProxies.put(shaderName, resourceLocation);
    }
    
    public static Model getModel(String modelName) {
        Model resource = modelResources.get(modelName);
        if(resource != null) return resource;
        
        Model model;
        String anim = animInfo.get(modelName);
        if(anim == null) {
            model = MeshLoader.loadMesh(modelProxies.get(modelName), "/res/models/");
        } else {
            model = MeshLoader.loadMesh(modelProxies.get(modelName), "/res/models/", anim);
        }
        
        if(model != null) {
            modelResources.put(modelName, model);
            return model;
        }
        
        return null;
    }
    
    public static Shader getShader(String shaderName) {
    	Shader resource = shaderResources.get(shaderName);
        if(resource != null) return resource;
        
        Shader shader = new Shader(shaderName + ".vs", shaderName + ".fs");
        if(shader != null) {
            shaderResources.put(shaderName, shader);
            return shader;
        }
        
        return null;
    }
}

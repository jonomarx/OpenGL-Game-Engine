package com.jonmarx.core;

import java.util.ArrayList;
import java.util.List;

import com.jonmarx.plugin.Plugin;

import glm_.vec3.Vec3;

public class Game {
    private List<Plugin> plugins = new ArrayList<>();
    private State state;
    
    public Game(List<Plugin> plugins, State state) {
        this.plugins = plugins;
        this.state = state;
    }
    
    public void init() {
    	for(Plugin plugin : plugins) {
        	plugin.onInit();
        }
    }
    
    public void render() {
    	state.render();
    }
    
    public void update() {
    	state.update();
    	for(Plugin plugin : plugins) {
        	plugin.onUpdate();
        }
    }
    
    public void addEntity(Entity entity) {
    	state.addEntity(entity);
    	for(Plugin plugin : plugins) {
    		plugin.onEntityCreated(entity);
    	}
    }
    
    public boolean removeEntity(String id) {
    	for(Plugin plugin : plugins) {
    		plugin.onEntityDeleted(state.getEntity(id));
    	}
    	return state.removeEntity(id);
    }
    
    public void translateEntity(String id, Vec3 translation) {
    	
    }
    
    public void rotateEntity(String id, Vec3 rotation) {
    	
    }
    
    public void scaleEntity(String id, Vec3 scale) {
    	
    }
    
    public Entity[] getEntities() {
        return state.getEntities();
    }
    
    public Entity getEntity(String id) {
    	return state.getEntity(id);
    }
    
    public Entity getEntity(int pos) {
        return state.getEntity(pos);
    }
}

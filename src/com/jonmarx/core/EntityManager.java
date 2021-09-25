package com.jonmarx.core;

import java.util.ArrayList;

public class EntityManager {
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<String> shaders = new ArrayList<>();
    
    public EntityManager() {
        
    }
    
    public void addEntity(Entity entity, String shader) {
    	entities.add(entity);
    	shaders.add(shader);
    }
    
    public void removeEntity(String id) {
    	for(Entity entity : entities) {
            if(entity.getId().equals(id)) {
                entities.remove(entity);
                return;
            }
        }
    }
    
    public Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
    }
    
    public String[] getShaders() {
        return shaders.toArray(new String[0]);
    }
    
    public Entity getEntity(String id) {
        for(Entity entity : entities) {
            if(entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }
    
    public Entity getEntity(int pos) {
        return entities.get(pos);
    }
    
    public String getShader(int pos) {
        return shaders.get(pos);
    }
    
    public String getShader(String id) {
        int count = 0;
        for(Entity entity : entities) {
            if(entity.getId().equals(id)) {
                return shaders.get(count);
            }
            count++;
        }
        return null;
    }
}

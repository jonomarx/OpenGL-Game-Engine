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
}

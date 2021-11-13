/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import java.util.ArrayList;
import java.util.List;

/**
 * manages state stuff
 * @author Jon
 */
public abstract class State {
	private List<Entity> entities;
	
	public State() {
		entities = new ArrayList<>();
	}
	
	public void addEntity(Entity entity) {
    	entities.add(entity);
    }
    
    public boolean removeEntity(String id) {
    	for(Entity entity : entities) {
            if(entity.getId().equals(id)) {
                entities.remove(entity);
                return true;
            }
        }
    	return false;
    }
    
    public Entity[] getEntities() {
        return entities.toArray(new Entity[0]);
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
	
	public abstract void update();
	public abstract void render();
}

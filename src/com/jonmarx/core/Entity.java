package com.jonmarx.core;

import glm_.mat4x4.Mat4;

/**
 * A drawable entity
 * @author Jon
 */
public abstract class Entity extends Object3D {
    /**
     * Stores the model information of the Entity
     */
    protected Model model;
    protected String id;

    public Entity(Mat4 locrot, Model model, String id) {
        super(locrot);
        this.model = model;
        this.id = id;
    }
    
    /**
     * returns the Model
     * @return
     */
    public Model getModel() {
        return model;
    }
    
    public String getId() {
        return id;
    }
    
    public abstract void update();
}

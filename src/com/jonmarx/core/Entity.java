package com.jonmarx.core;

import static glm_.Java.glm;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

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
    protected String shader;
    
    protected Vec3 pos;
    protected float yaw;
    protected float pitch;
    protected float roll;
    protected Vec3 scale;

    public Entity(Mat4 locrot, Model model, String id, String shader) {
        super(locrot);
        this.model = model;
        this.id = id;
        this.shader = shader;
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
    
    public String getShader() {
    	return shader;
    }
    
    private void updateRotation() {
        locrot = new Mat4().translate(pos).rotateXYZ(glm.radians(roll),glm.radians(-yaw),glm.radians(pitch)).scale(scale);
    }
    
    protected void translate(Vec3 translation) {
    	pos = pos.plus(translation);
    	updateRotation();
    }
    protected void rotate(Vec3 rotation) {
    	yaw += rotation.getX();
    	pitch += rotation.getY();
    	roll += rotation.getZ();
    	updateRotation();
    }
    protected void scale(Vec3 amount) {
    	scale = scale.plus(amount);
    	updateRotation();
    }
    
    public abstract void update();
}

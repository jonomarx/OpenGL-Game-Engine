/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Entity;
import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.core.Model;
import com.jonmarx.core.Renderer;
import com.jonmarx.core.State;

import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

/**
 * The impostor's gun
 * @author Jon
 */
public class Gun extends Entity {
    /**
     * Camera class
     * for math
     */
    protected Camera camera = new Camera();
    
    /**
     * uhh. blender doesn't like scaling down armature actions, temporary fix.
     */
    protected float SCALESIZE = 0.8f;
    
    /**
     * thing, i should really just create a superclass for this...
     * @param yaw - rotation of the object, pitch may come later
     * @param pitch pitch
     * @param pos - position
     * @param model - What model to use
     * @param id - identification to retrieve later from <code>Renderer</code>
     */
    public Gun(float yaw, float pitch, Vec3 pos, Model model, String id, String shader) {
        super(new Mat4().rotateY(glm.radians(yaw)).translate(pos), model, id, shader);
        camera.setYaw(yaw);
        camera.setPitch(pitch);
        camera.setPos(pos);
        updateRotation();
    }
    
    /**
     * Rotates by said degrees
     * @param yaw 
     */
    public void rotateX(float yaw) {
        camera.rotateX(yaw);
        updateRotation();
    }
    
    /**
     * Sets rotation in degrees
     * @param yaw 
     */
    public void setXRotation(float yaw) {
        camera.setYaw(yaw);
        updateRotation();
    }
    
    /**
     * !NOT IMPLEMENTED!
     * Rotates by said degrees
     * @param pitch
     */
    public void rotateY(float pitch) {
        camera.rotateY(pitch);
        updateRotation();
    }
    
    /**
     * !NOT IMPLEMENTED!
     * Sets rotation in degrees 
     * @param pitch
     */
    public void setYRotation(float pitch) {
        camera.setPitch(pitch);
        updateRotation();
    }
    /**
     * wraps {@see com.jonmarx.core.Camera#moveForward(float) Camera.moveForward}
     * @param distance 
     */
    public void moveForward(float distance) {
        camera.moveForward(distance);
        updateRotation();
    }
    /**
     * wraps {@see com.jonmarx.core.Camera#moveRight(float) Camera.moveRight}
     * @param distance 
     */
    public void moveRight(float distance) {
        camera.moveRight(distance);
        updateRotation();
    }
    /**
     * wraps {@see com.jonmarx.core.Camera#moveUp(float) Camera.moveUp}
     * @param distance 
     */
    public void moveUp(float distance) {
        camera.moveUp(distance);
        updateRotation();
    }
    
    public float getYaw() {
        return camera.getYaw();
    }
    
    public float getPitch() {
        return camera.getPitch();
    }
    
    public Vec3 getPos() {
        return camera.getPos();
    }
    
    public Vec3 getForward() {
        return camera.getFront();
    }
    
    public void setPos(Vec3 pos) {
        camera.setPos(pos);
        updateRotation();
    }
    
    /**
     * Updates model matrix
     */
    protected void updateRotation() {
        locrot = new Mat4().translate(camera.getPos()).rotateXYZ(0, glm.radians(-camera.getYaw()), glm.radians(camera.getPitch())).scale(SCALESIZE,SCALESIZE,SCALESIZE);
    }
    
    /**
     * spawns a bullet that dies after some time
     */
    public void shoot() {
        // crappy calculation for up vector, but close enough in most cases
    	Game state = Main.getInstance().getGame();
        Vec3 front = camera.getFront().normalize();
        Vec3 right = front.cross(new Vec3(0,1,0)).normalize();
        Vec3 up = front.cross(right);
        state.addEntity(new Bullet(getYaw(), getPitch(), getPos().plus(up.times(-0.4f)).plus(right.times(-0.15f)), 30, 0.1f, MemoryCache.getModel("bullet"), "bullet", "lightShader"));
    }

    @Override
    public void update() {
        
    }
    
}

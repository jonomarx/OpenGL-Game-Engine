/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Entity;
import com.jonmarx.core.Model;
import com.jonmarx.core.Renderer;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

/**
 * An Among Us Crewmate 
 * please don't sue :wink:
 * @author Jon
 */
public class Crewmate extends CollidableEntity {
    /**
     * Camera class
     * for math
     */
    protected Camera camera = new Camera();
    
    /**
     * uhh. blender doesn't like scaling down armature actions, temporary fix.
     */
    protected final Vec3 SCALESIZE = new Vec3(1f);
    
    protected boolean clicking = false;
    
    /**
     * If extracting from a camera, invert yaw!
     * @param yaw - rotation of the object, pitch may come later
     * @param pos - position
     * @param model - What model to use
     * @param id - identification to retrieve later from <code>Renderer</code>
     */
    public Crewmate(float yaw, Vec3 pos, Model model, String id) {
        super(pos, new Vec3(), new Vec3(1f), model, id, new Vec3(0.61f,1.06f,0.61f));
        camera.setYaw(yaw);
        camera.setPitch(0.1f);
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
        //camera.moveForward(distance);
        //updateRotation();
        move(noY(camera.getFront()).times(distance));
    }
    /**
     * wraps {@see com.jonmarx.core.Camera#moveRight(float) Camera.moveRight}
     * @param distance 
     */
    public void moveRight(float distance) {
        //camera.moveRight(distance);
        //updateRotation();
        move(noY(camera.getFront().cross(new Vec3(0,1,0)).normalize()).times(distance));
    }
    /**
     * wraps {@see com.jonmarx.core.Camera#moveUp(float) Camera.moveUp}
     * @param distance 
     */
    public void moveUp(float distance) {
        //camera.moveUp(distance);
        //updateRotation();
        move(new Vec3(0,distance,0));
    }
    
    private Vec3 noY(Vec3 in) {
        in.setY(0);
        return in.normalize();
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
    
    @Override
    public void move(Vec3 direction) {
        super.move(direction);
        camera.setPos(pos);
        updateRotation();
    }
    
    /**
     * Updates model matrix
     */
    @Override
    public void updateRotation() {
        locrot = new Mat4().translate(camera.getPos()).rotateXYZ(0, glm.radians(-camera.getYaw()), 0).scale(SCALESIZE);
        
        Gun gun = (Gun) Renderer.getEntity("gun");
        
        float yaw = getYaw();
        float pitch = getPitch();
        Vec3 direction = new Vec3();
        direction.setX(glm.cos(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction.setY(glm.sin(glm.radians(pitch)));
        direction.setZ(glm.sin(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction = direction.normalize();
        
        Vec3 rightDir = direction.cross(new Vec3(0,1,0)).normalize();
        
        if(gun == null) return;
        gun.setXRotation(getYaw());
        gun.setYRotation(getPitch());
        gun.setPos(getPos().plus(0f,0.75f,0f).plus(rightDir.times(0.3f)).plus(direction.times(1.2f)));
    }
    
    /**
     * Clicks.
     */
    public void click() {
        clicking = true;
    }
    
    /**
     * Releases click
     */
    public void unClick() {
        clicking = false;
    }
    
    boolean isFirstFrame = true;
    @Override
    public void update() {
        super.update();
        if(clicking) {
            if(isFirstFrame) {
                isFirstFrame = false;
                Gun gun = (Gun) Renderer.getEntity("gun");
                gun.shoot();
            }
        } else {
            isFirstFrame = true;
        }
    }
}

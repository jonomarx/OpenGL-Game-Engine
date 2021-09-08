/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import glm_.vec3.Vec3;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;

/**
 * thanks, LearnOpenGL.com!
 * @author Jon
 */
public class Camera {
    private Vec3 cameraPos = new Vec3(0f, 0f, 0f);
    private Vec3 cameraFront = new Vec3(0f, 0f, -1f);
    private Vec3 cameraUp = new Vec3(0f, 1f, 0f);
    
    private float yaw = 90f;
    private float pitch = 0.1f;
    private Vec3 direction = new Vec3();
    
    public Camera() {
        updateRotation();
    }
    
    public void moveForward(float distance) {
        cameraPos = cameraPos.plus(noY(cameraFront.times(distance)));
    }
    
    public void moveRight(float distance) {
        cameraPos = cameraPos.plus(noY(cameraFront.cross(cameraUp).normalize().times(distance)));
    }
    
    public void moveUp(float distance) {
        cameraPos = cameraPos.plus(0, distance, 0);
    }
    
    public Mat4 getView() {
        return glm.lookAt(cameraPos, cameraPos.plus(cameraFront), cameraUp);
    }
    
    public Vec3 getPos() {
        return cameraPos;
    }
    
    public void rotateX(float amount) {
        yaw += amount;
        updateRotation();
    }
    
    public void rotateY(float amount) {
        pitch += amount;
        updateRotation();
    }
    
    private void updateRotation() {
        if(pitch > 89.9f) pitch = 89.9f;
        if(pitch < -89.9f) pitch = -89.9f;
        
        direction.setX(glm.cos(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction.setY(glm.sin(glm.radians(pitch)));
        direction.setZ(glm.sin(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        
        cameraFront = direction.normalize();
    }
    
    // FPS camera, disables fly camera
    private Vec3 noY(Vec3 pos) {
        pos.setY(0);
        pos = pos.normalize();
        return pos;
    }
    
    public float getYaw() {
        return yaw;
    }
    
    public float getPitch() {
        return pitch;
    }
    
    public Vec3 getFront() {
        return cameraFront;
    }
    
    public void setYaw(float yaw) {
        this.yaw = yaw;
        updateRotation();
    }
    
    public void setPitch(float pitch) {
        this.pitch = pitch;
        updateRotation();
    }
    
    public void setPos(Vec3 pos) {
        this.cameraPos = pos;
    }
}

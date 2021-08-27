/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Entity;
import com.jonmarx.core.Main;
import com.jonmarx.core.Mesh;
import com.jonmarx.core.Renderer;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.vec2.Vec2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import glm_.vec3.Vec3;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Controls the camera, does not render.
 * @author Jon
 */
public class CameraController extends Entity {

    public CameraController(String id) {
        super(null, null, id);
    }

    @Override
    public void update() {
        Crewmate entity = (Crewmate) Renderer.getEntity("amongus");
        Main main = Main.getInstance();
        Vec2 delta = main.getMouseMovement();
        float deltaX = delta.getX();
        float deltaY = delta.getY();
        
        float rotate = 0.1f;
        entity.rotateX(deltaX * rotate);
        entity.rotateY(deltaY * rotate);
        
        float distance = 0.3f;
        
        if(main.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            entity.moveForward(distance);
        }
        if(main.getKey(GLFW_KEY_A) == GLFW_PRESS) {
            entity.moveRight(-distance);
        }
        if(main.getKey(GLFW_KEY_S) == GLFW_PRESS) {
            entity.moveForward(-distance);
        }
        if(main.getKey(GLFW_KEY_D) == GLFW_PRESS) {
            entity.moveRight(distance);
        }
        distance = 0.2f/2;
        if(main.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
            entity.jump();
        }
        if(main.getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            //entity.moveUp(-distance);
        }
        if(main.getMouseClicked(GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            entity.click();
        }
        if(main.getMouseClicked(GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
            entity.unClick();
        }
        
        float yaw = entity.getYaw();
        Vec3 direction = new Vec3();
        direction.setX(glm.cos(glm.radians(yaw)));
        direction.setY(0);
        direction.setZ(glm.sin(glm.radians(yaw)));
        
        Renderer.getCamera().setYaw(entity.getYaw());
        Renderer.getCamera().setPitch(entity.getPitch());
        Renderer.getCamera().setPos(entity.getPos().plus(new Vec3(0f,1.05f,0f).plus(direction.times(0.6f))));
    }
}

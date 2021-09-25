/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.EntityManager;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.core.Renderer;
import com.jonmarx.core.SimpleEntity;
import com.jonmarx.core.State;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

/**
 * An example of a state
 * @author Jon
 */
public class GameState extends State {
    
    protected EntityManager entities = new EntityManager();
    
    public GameState() {
        init();
    }
    
    protected void init() {
        entities.addEntity(new Gun(90f, 0.1f, new Vec3(0f), MemoryCache.getModel("gun"), "gun"), "lightShader");
        entities.addEntity(new Crewmate(90, new Vec3(0f,8f,0f), MemoryCache.getModel("amongus"), "amongus"), "lightShader");
        entities.addEntity(new SimpleEntity(new Mat4(), MemoryCache.getModel("terrain"), "terrain"), "lightShader");
        entities.addEntity(new CameraController("camera-controller"), null);
    }
    
    @Override
    public void render() {
        Renderer.renderFromList(entities);
    }
    
    private Vec3 GRAVITY = new Vec3(0f, -0.163f, 0f);
    @Override
    public void update() {
        Entity[] entityList = entities;
        for(int i = 0; i < entityList.length; i++) {
            Entity entity = entityList[i];
            if(entity instanceof CollidableEntity) {
                ((CollidableEntity) entity).move(GRAVITY);
            }
            entity.update();
        }
    }
}

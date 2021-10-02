package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.EntityManager;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.core.Mesh;
import com.jonmarx.core.State;
import com.jonmarx.core.Renderer;
import com.jonmarx.core.SimpleEntity;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

public class MainState extends State {
    
    protected EntityManager entities = new EntityManager();
    
    public MainState() {
        init();
    }
    
    protected void init() {
    	entities.addEntity(new SimpleEntity(new Mat4(), MemoryCache.getModel("billboard"), "billboard"), "2dShader");
        Mesh meshes = entities.getEntity(0).getModel().getMeshes()[0];
        for(int i = 0; i < meshes.getVertices().length; i++) {
        	System.out.println(meshes.getVertices()[i].getTx() + " " + meshes.getVertices()[i].getTy());
            System.out.println(meshes.getVertices()[i].getPos());
        }
    }
    
    @Override
    public void render() {
        Renderer.renderFromList(entities);
    }
    
    @Override
    public void update() {
        
    }
}

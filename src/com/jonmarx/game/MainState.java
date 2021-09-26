package com.jonmarx.game;

import com.jonmarx.core.EntityManager;
import com.jonmarx.core.State;
import com.jonmarx.core.Renderer;
import glm_.vec3.Vec3;

public class MainState extends State {
    
    protected EntityManager entities = new EntityManager();
    
    public MainState() {
        init();
    }
    
    protected void init() {
        
    }
    
    @Override
    public void render() {
        Renderer.renderFromList(entities);
    }
    
    @Override
    public void update() {
        
    }
}

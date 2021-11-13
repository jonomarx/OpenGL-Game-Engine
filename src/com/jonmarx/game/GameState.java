/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import java.util.ArrayList;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
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
    
    public GameState() {
        init();
    }
    
    protected void init() {
    	
    }
    
    @Override
    public void render() {
        Renderer.renderFromList(Main.getInstance().getGame());
    }
    
    @Override
    public void update() {
    	
    }
}

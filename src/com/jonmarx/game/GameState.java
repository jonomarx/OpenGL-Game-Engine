/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.core.Renderer;
import com.jonmarx.core.SimpleEntity;
import com.jonmarx.core.State;
import com.jonmarx.discord.DiscordSystem;
import com.jonmarx.game.components.CollisionComponent;
import com.jonmarx.game.components.ModelComponent;
import com.jonmarx.game.components.PositionComponent;
import com.jonmarx.game.entities.CollisionGameEntity;
import com.jonmarx.game.entities.GameEntity;
import com.jonmarx.game.systems.AudioSystem;
import com.jonmarx.game.systems.CollisionSystem;
import com.jonmarx.game.systems.GravitySystem;
import com.jonmarx.game.systems.InputUserSystem;
import com.jonmarx.game.systems.MovementSystem;
import com.jonmarx.game.systems.RenderingSystem;
import com.jonmarx.game.systems.UserSystem;
import com.jonmarx.sound.SoundPlayer;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

/**
 * An example of a state
 * @author Jon
 */
public class GameState extends State {
    
    public GameState() {
        init();
        
        ECSEventManager.addSystem(new InputUserSystem());
        ECSEventManager.addSystem(new GravitySystem());
        ECSEventManager.addSystem(new CollisionSystem());
        ECSEventManager.addSystem(new AudioSystem());
        ECSEventManager.addSystem(new MovementSystem());
        ECSEventManager.addSystem(new DiscordSystem());
        ECSEventManager.addSystem(new UserSystem());
        
        ECSEventManager.addSystem(new RenderingSystem());
        
        ECSEventManager.init();
    }
    
    protected void init() {
    	
    }
    
    @Override
    public void render() {
        //Renderer.renderFromList(Main.getInstance().getGame());
    }
    
    @Override
    public void update() {
    	ECSEventManager.update();
    	ECSEventManager.flushEvents();
    }
}

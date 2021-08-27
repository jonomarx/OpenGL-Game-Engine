/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Model;

/**
 *
 * @author Jon
 */
public class Game {
    private World world;
    
    public Game(Entity test) {
        this.world = new World(test);
    }
    
    public World getCollisionBox() {
        return world;
    }
}

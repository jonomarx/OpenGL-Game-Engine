/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

/**
 * runs during update() and render()
 * runs game code
 * interchangable with other States, well maybe
 * @author Jon
 */
public abstract class State {
    public abstract void render();
    public abstract void update();
}

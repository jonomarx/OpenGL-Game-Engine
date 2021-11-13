/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.plugin;

import com.jonmarx.core.Entity;
import com.jonmarx.core.State;

import glm_.vec3.Vec3;

/**
 * Its a plugin!
 * idk actually how to write one.
 * @author Jon
 */
public interface Plugin {
    public default void onInit() {} // when the state is created
    public default void onUpdate() {} // update tick
    public default void onDiscard() {} // when the state is discarded
    
    public default void onStateSwitch(State state) {} // when the state is switched (either)
    
    public default void onEntityCreated(Entity entity) {} // when an entity is created
    public default void onEntityDeleted(Entity entity) {} // when an entity is destroyed
    
    public default void onEntityMoved(Entity entity, Vec3 direction) {} // when an entity is moved
    public default void onEntityRotated(Entity entity, Vec3 rotation) {} // when an entity is rotated
    public default void onEntityScaled(Entity entity, Vec3 scale) {} // when an entity is scaled
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import glm_.vec3.Vec3;

/**
 * a collidable object - this interface provides access to the hitbox
 * @author Jon
 */
public interface Collidable {
    public Vec3 getESpace();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.vec3.Vec3;

/**
 *
 * @author Jon
 */
public class CollisionPacket {
    public Vec3 R3Pos;
    public Vec3 R3Velocity;
    
    public Vec3 eSpace;
    public Vec3 velocity;
    public Vec3 nVelocity;
    public Vec3 basePoint;
    
    public boolean collided = false;
    public float nearestDistance;
    public Vec3 intersectionPoint;
}

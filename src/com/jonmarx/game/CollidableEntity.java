/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Main;
import com.jonmarx.core.Model;
import com.jonmarx.util.CollisionPacket;
import com.jonmarx.util.Plane;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import static glm_.Java.glm;

/**
 *
 * @author Jon
 */
public class CollidableEntity extends Entity implements Collidable {
    protected Vec3 eSpace;
    protected Vec3 pos;
    protected Vec3 rotation;
    protected Vec3 scale;

    public CollidableEntity(Vec3 pos, Vec3 rotation, Vec3 scale, Model model, String id, Vec3 hitbox) {
        super(new Mat4().translate(pos).rotateXYZ(rotation).scale(scale), model, id);
        eSpace = hitbox;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }
    
    
    /**
     * almost the same as on the website
     * @param direction 
     */
    public void move(Vec3 direction) {
        CollisionPacket packet = new CollisionPacket();
        packet.R3Pos = pos;
        packet.R3Velocity = direction;
        packet.eSpace = eSpace;
        convertToESpace(packet);
        
        // hard coded
        boolean isGravity = false;
        if(direction.getX() == 0 && direction.getY() < 0 && direction.getZ() == 0) {
            isGravity = true;
        }
        
        int loop = 0;
        while(true) {
            if(loop > 5) {
                break;
            }
            
            GameState state = (GameState) Main.getState();
            World world = new World(state.getEntity("terrain"));
            world.testCollision(packet);
            
            
            if(!packet.collided) {
                packet.basePoint = packet.basePoint.plus(packet.velocity);
                packet.velocity = new Vec3(0);
                break;
            }
            Vec3 newPos = packet.basePoint;
            Vec3 destinationPoint = packet.basePoint.plus(packet.velocity);
            
            if(packet.nearestDistance >= 0.005f) {
                Vec3 v = packet.nVelocity.times(packet.nearestDistance - 0.005f);
                newPos = packet.basePoint.plus(v);
                packet.intersectionPoint = packet.intersectionPoint.minus(v.normalize().times(0.005f));
            }
            
            Vec3 planePos = packet.intersectionPoint;
            Vec3 planeNorm = newPos.minus(planePos).normalize();
            Plane plane = new Plane(planePos, planeNorm);
            
            Vec3 newDestinationPoint = destinationPoint.minus(planeNorm.times(plane.distanceTo(destinationPoint)));
            Vec3 newVelocity = newDestinationPoint.minus(planePos);
            if(glm.length(newVelocity) < 0.005f) {
                packet.basePoint = newPos;
                break;
            }
            
            packet.basePoint = newPos;
            packet.velocity = newVelocity;
            packet.nVelocity = newVelocity.normalize();
            packet.collided = false;
            
            Vec3 dir = packet.velocity.times(packet.eSpace).normalize();
            float dot = 1 - glm.abs(dir.dot(new Vec3((float)dir.getX(), 0f, (float)dir.getZ())));
            if(isGravity) {
                // do NOT slide down a slope less than 18 degrees
                if(dot < 0.2) {
                    break;
                    // just a guess
                }
            } else {
                // do NOT go up a slope higher than 72 degrees
                System.out.println(dot);
                if(dot > 0.8) {
                    break;
                    // just a guess
                }
            }
            loop++;
        }
        convertToR3(packet);
        // crappy method
        if(glm.abs(glm.length(pos.minus(packet.R3Pos))) < 0.153) {
            canJump = true;
        } else {
            canJump = false;
        }
        pos = packet.R3Pos;
    }
    
    private int jumpFrame = 0;
    private boolean canJump = false;
    /**
     * Test function
     */
    public void jump() {
        // already jumping
        if(jumpFrame != 0 || !canJump) return;
        jumpFrame = 1;
        
    }
    /**
     * Converts eSpace Coords into R3 Coords
     * @param packet 
     */
    protected void convertToR3(CollisionPacket packet) {
        packet.R3Pos = packet.basePoint.times(packet.eSpace);
        packet.R3Velocity = packet.velocity.times(packet.eSpace);
    }
    
    /**
     * Converts R3 Coords into eSpace Coords
     * @param packet 
     */
    protected void convertToESpace(CollisionPacket packet) {
        packet.basePoint = packet.R3Pos.div(packet.eSpace);
        packet.velocity = packet.R3Velocity.div(packet.eSpace);
        packet.nVelocity = packet.velocity.normalize();
    }

    @Override
    public void update() {
        if(jumpFrame > 0) {
            jumpFrame++;
            move(new Vec3(0f, 0.153f*2 * (1-jumpFrame/600), 0f));
        }
        if(jumpFrame >= 60) {
            jumpFrame = 0;
        }
    }
    
    public void updateRotation() {
        locrot = new Mat4().translate(pos).rotateXYZ(rotation).scale(scale);
    }

    @Override
    public Vec3 getESpace() {
        return eSpace;
    }
    
}

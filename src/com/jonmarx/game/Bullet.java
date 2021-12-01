/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
import com.jonmarx.core.Model;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import com.jonmarx.core.State;
import com.jonmarx.sound.SoundListener;
import com.jonmarx.sound.SoundPlayer;

/**
 *
 * @author Jon
 */
public class Bullet extends Entity {
    private int lifetime;
    private Vec3 pos;
    private float yaw;
    private float pitch;
    private float speed;

    public Bullet(float yaw, float pitch, Vec3 pos, int lifetime, float speed, Model model, String id, String shader) {
        super(new Mat4(), model, id, shader);
        this.lifetime = lifetime;
        this.yaw = yaw;
        this.pitch = pitch;
        this.pos = pos;
        this.speed = speed;
        updateRotation();
    }
    int num = 0;
    @Override
    public void update() {
        if(lifetime == 0) {
            Game game = Main.getInstance().getGame();
            game.removeEntity(id);
        }
        Vec3 direction = new Vec3();
        direction.setX(glm.cos(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        direction.setY(glm.sin(glm.radians(pitch)));
        direction.setZ(glm.sin(glm.radians(yaw)) * glm.cos(glm.radians(pitch)));
        
        pos = pos.plus(direction.normalize().times(speed));
        lifetime--;
        updateRotation();
        num++;
        if(num == 12) {
        	SoundPlayer.playSound();
        	SoundPlayer.getSource().setPosition(this.pos);
        }
    }
    
    private void updateRotation() {
        locrot = new Mat4().translate(pos).rotateXYZ(0,glm.radians(-yaw),glm.radians(pitch));
    }
    
}

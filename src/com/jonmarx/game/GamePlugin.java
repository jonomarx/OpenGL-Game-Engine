package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
import com.jonmarx.core.MemoryCache;
import com.jonmarx.core.SimpleEntity;
import com.jonmarx.plugin.Plugin;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;

public class GamePlugin implements Plugin {
	@Override
	public void onInit() {
		System.out.println("Inited!");
		Game game = Main.getInstance().getGame();
        //game.addEntity(new Gun(90f, 0.1f, new Vec3(0f), MemoryCache.getModel("gun"), "gun", "lightShader"));
        //game.addEntity(new Crewmate(90, new Vec3(0f,8f,0f), MemoryCache.getModel("amongus"), "amongus", "lightShader"));
        game.addEntity(new SimpleEntity(new Mat4(), MemoryCache.getModel("terrain"), "terrain", "lightShader"));
        //game.addEntity(new CameraController("camera-controller"));
	}
	
	private Vec3 GRAVITY = new Vec3(0f, -0.163f, 0f);
	@Override
	public void onUpdate() {
		Game game = Main.getInstance().getGame();
		
		Entity[] entityList = game.getEntities();
        for(int i = 0; i < entityList.length; i++) {
            Entity entity = entityList[i];
            if(entity instanceof CollidableEntity) {
                ((CollidableEntity) entity).move(GRAVITY);
            }
            entity.update();
        }
	}
}

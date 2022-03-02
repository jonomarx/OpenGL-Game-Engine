package com.jonmarx.game.systems;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import java.util.List;

import com.jonmarx.core.Camera;
import com.jonmarx.core.Main;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;
import com.jonmarx.game.entities.CollisionGameEntity;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

public class InputUserSystem extends ECSSystem {
	
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};
	
	ECSEntity amogus;
	Camera realCamera;

	public InputUserSystem() {
		super("inputUserSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
	}

	@Override
	protected void updateO() {
		if(amogus == null) {
			amogus = (ECSEntity) ECSStorage.getVariable("amogus");
			if(amogus == null) return;
			
			Vec3 rot = (Vec3) amogus.getField("rotation");
			Vec3 pos = (Vec3) amogus.getField("position");
			realCamera.setPos(pos);
	        realCamera.setYaw(rot.getX());
	        realCamera.setPitch(rot.getY());
		}
		
		Main main = Main.getInstance();
        Vec2 delta = main.getMouseMovement();
        float deltaX = delta.getX();
        float deltaY = delta.getY();
        
        Camera camera = new Camera();
		Vec3 pos = (Vec3) amogus.getField("position");
		camera.setPos(pos);
        camera.setYaw(realCamera.getYaw());
        camera.setPitch(realCamera.getPitch());
        
        float rotate = 0.1f;
        camera.rotateX(deltaX * rotate);
        camera.rotateY(deltaY * rotate);
        realCamera.rotateX(deltaX * rotate);
        realCamera.rotateY(deltaY * rotate);
        
        float distance = 0.3f;
        if(main.getKey(GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(distance);
        }
        if(main.getKey(GLFW_KEY_A) == GLFW_PRESS) {
        	camera.moveRight(-distance);
        }
        if(main.getKey(GLFW_KEY_S) == GLFW_PRESS) {
        	camera.moveForward(-distance);
        }
        if(main.getKey(GLFW_KEY_D) == GLFW_PRESS) {
        	camera.moveRight(distance);
        }
        
        Vec3 dist = camera.getPos().minus(realCamera.getPos());
        amogus.setField("force", ((Vec3)amogus.getField("force")).plus(dist));
	}

	@Override
	protected void init() {
		ECSStorage.putVariable("amogusCamera", new Camera());
		realCamera = (Camera) ECSStorage.getVariable("amogusCamera");
	}

}

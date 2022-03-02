package com.jonmarx.game.systems;

import java.util.List;

import glm_.vec3.Vec3;

import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;
import com.jonmarx.util.ObjectBoundingBox;
import static glm_.Java.glm;

public class MovementSystem extends ECSSystem {
	private static String[] messageFilter = new String[] {};
	private static String[] componentFilter = new String[] {};

	public MovementSystem() {
		super("movementSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		
	}
	
	@Override
	protected void updateO() {
		List<ECSEntity>entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		for(ECSEntity entity : e) {
			if(entity.containsComponent("physicsComponent")) {
				applyGravity(entity);
				
				Vec3 acceleration = ((Vec3) entity.getField("force")).div((float) entity.getField("mass"));
				entity.setField("force", new Vec3(0,0,0));
				entity.setField("velocity", ((Vec3) entity.getField("velocity")).plus(acceleration));
				if(entity.getId().getLeastSignificantBits() == 0) {
					System.out.println(entity.getField("velocity"));
				}
				
				moveObject(entity);
				
				//Vec3 newPos = ((Vec3) entity.getField("position")).plus((Vec3) entity.getField("velocity"));
				
				
				
				//entity.setField("position", newPos);
			}
		}
	}
	
	private void moveObject(ECSEntity entity) {
		List<ECSEntity> entities = ECSStorage.getEntities();
		ECSEntity[] e = entities.toArray(new ECSEntity[0]);
		
		if(!entity.containsComponent("physicsComponent")) return;
		//ObjectBoundingBox obb = generateOBB(entity);
		
		float dist = 1;
		ECSEntity collidingObject = null;
		
		for(ECSEntity collide : e) {
			if(!collide.containsComponent("physicsComponent")) continue;
			if(collide == entity) continue;
			
			//ObjectBoundingBox testObb = generateOBB(collide);
			//float testDist = testObb.sweepTest(obb, (Vec3) entity.getField("velocity"));
			
			//if(testDist < dist) {
			//	dist = testDist;
			//	collidingObject = collide;
			//}
		}
		
		//entity.setField("postition", ((Vec3) entity.getField("position")).plus(((Vec3) entity.getField("velocity")).times(dist)));
		
		if(collidingObject != null) {
			collide(entity, collidingObject);
			System.out.println("COLLIDED!");
		}
	}
	
	private final float G = (float) 6.674e-11;
	
	private void applyGravity(ECSEntity entity) {
		for(ECSEntity e : ECSStorage.getEntities()) {
			if(!e.containsComponent("physicsComponent")) continue;
			if(e == entity) continue;
			
			Vec3 dir = ((Vec3) e.getField("position")).minus((Vec3) entity.getField("position")).normalize();
			float dist = ((Vec3) e.getField("position")).minus((Vec3) entity.getField("position")).length();
			
			float m1 = (float) entity.getField("mass");
			float m2 = (float) e.getField("mass");
			
			Vec3 move = dir.times((m1 * m2) / (dist*dist) * G);
			if(move.getX().isNaN()) move.setX(0);
			if(move.getY().isNaN()) move.setY(0);
			if(move.getZ().isNaN()) move.setZ(0);
			entity.setField("force", ((Vec3) entity.getField("force")).plus(move));
		}
	}
	
	private void collide(ECSEntity object1, ECSEntity object2) {
		Vec3 v1 = (Vec3) object1.getField("velocity");
		Vec3 v2 = (Vec3) object2.getField("velocity");
		float m1 = (float) object1.getField("mass");
		float m2 = (float) object2.getField("mass");
		
		Vec3 v2p = v1.times((2 * m1) / (m1 + m2)).minus(v2.times((m1 - m2) / (m1 + m2)));
		Vec3 v1p = v1.times((m1 - m2) / (m1 + m2)).plus(v2.times((2 * m2) / (m1 + m2)));
		
		object1.setField("velocity", v1p);
		object2.setField("velocity", v2p);
	}
	
	private ObjectBoundingBox generateOBB(ECSEntity e) {
		ObjectBoundingBox box = new ObjectBoundingBox((Vec3) e.getField("position"), (Vec3) e.getField("hitbox"));
		box.setRotation((Vec3) e.getField("rotation"));
		return box;
	}
	
	@Override
	protected void init() {
		
	}
}

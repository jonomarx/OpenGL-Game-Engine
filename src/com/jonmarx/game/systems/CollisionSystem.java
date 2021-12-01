package com.jonmarx.game.systems;

import static glm_.Java.glm;

import java.util.List;

import com.jonmarx.core.Game;
import com.jonmarx.core.Main;
import com.jonmarx.game.ECSEntity;
import com.jonmarx.game.ECSEvent;
import com.jonmarx.game.ECSStorage;
import com.jonmarx.game.ECSSystem;
import com.jonmarx.game.World;
import com.jonmarx.util.CollisionPacket;
import com.jonmarx.util.Plane;

import glm_.vec3.Vec3;

public class CollisionSystem extends ECSSystem {

	private static String[] messageFilter = new String[] {"move"};
	private static String[] componentFilter = new String[] {"+collisionComponent"};

	public CollisionSystem() {
		super("collisionSystem", messageFilter, componentFilter);
	}

	@Override
	protected void updateI(List<ECSEvent> events) {
		for(ECSEvent event : events) {
			String[] data = event.getData().split("\\|");
			float x = Float.parseFloat(data[0]);
			float y = Float.parseFloat(data[1]);
			float z = Float.parseFloat(data[2]);
			
			ECSEntity entity = ECSStorage.getEntity(event.getEntity());
			Vec3 position = (Vec3) entity.getField("position");
			
			Vec3 newMovement = move(new Vec3(x,y,z), (Vec3) entity.getField("hitbox"), position);
			event.setData(newMovement.getX() + "|" + newMovement.getY() + "|" + newMovement.getZ());
		}
	}
	
	public Vec3 move(Vec3 direction, Vec3 eSpace, Vec3 inPos) {
        CollisionPacket packet = new CollisionPacket();
        packet.R3Pos = inPos;
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
            
            Game state = Main.getInstance().getGame();
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
        /*// crappy method
        if(glm.abs(glm.length(pos.minus(packet.R3Pos))) < 0.153) {
            canJump = true;
        } else {
            canJump = false;
        }*/
        Vec3 outPos = packet.R3Pos;
        
        return outPos.minus(inPos);
    }
	
    protected void convertToR3(CollisionPacket packet) {
        packet.R3Pos = packet.basePoint.times(packet.eSpace);
        packet.R3Velocity = packet.velocity.times(packet.eSpace);
    }
    
    protected void convertToESpace(CollisionPacket packet) {
        packet.basePoint = packet.R3Pos.div(packet.eSpace);
        packet.velocity = packet.R3Velocity.div(packet.eSpace);
        packet.nVelocity = packet.velocity.normalize();
    }
	
    @Override
	protected void updateO() {
		
	}
	
    @Override
	protected void init() {
		
	}

}

package com.jonmarx.util;

import glm_.vec3.Vec3;
import static glm_.Java.glm;

import glm_.mat3x3.Mat3;

public class ObjectBoundingBox {
	private Vec3[] originalVertices = new Vec3[8];
	private Vec3[] vertices = new Vec3[8];
	
	private Vec3 upAxis;
	private Vec3 forwardAxis;
	private Vec3 rightAxis;
	
	private float rotateX = 0;
	private float rotateY = 0;
	private float rotateZ = 0;
	
	private Vec3 dimensions;
	
	public ObjectBoundingBox(Vec3 center, Vec3 dimensions) {
		dimensions = dimensions.div(2); // take half-sidelength
		this.dimensions = dimensions;
		
		for(int i = -1; i < 2; i+=2) {
			for(int j = -1; j < 2; j+=2) {
				for(int k = -1; k < 2; k+=2) {
					int spot = ((i + 1) / 3 * 4) + ((j + 1) / 3 * 2) + ((k + 1) / 3);
					originalVertices[spot] = center.plus(dimensions.getX()*i, dimensions.getY()*j, dimensions.getZ()*k);
				}
			}
		}
		updateRotation();
	}
	
	private void updateRotation() {
		Vec3 up = new Vec3(0,(float)dimensions.getY(),0);
		Vec3 right = new Vec3((float)dimensions.getX(),0,0);
		Vec3 forward = new Vec3(0,0,(float)dimensions.getZ());
		
		Mat3 rot = new Mat3().rotateXYZ(glm.radians(rotateZ), glm.radians(rotateX), glm.radians(rotateY));
		
		upAxis = rot.times(up);
		rightAxis = rot.times(right);
		forwardAxis = rot.times(forward);
		
		for(int i = 0; i < 8; i++) {
			vertices[i] = rot.times(originalVertices[0]);
		}
	}
	
	public void setRotation(Vec3 rot) {
		rotateX = rot.getX();
		rotateY = rot.getY();
		rotateZ = rot.getZ();
		updateRotation();
	}
	
	public Vec3 getRotation() {
		return new Vec3(rotateX, rotateY, rotateZ);
	}
	
	public Vec3 getUp() {
		return upAxis;
	}
	
	public Vec3 getForward() {
		return forwardAxis;
	}
	
	public Vec3 getRight() {
		return rightAxis;
	}
	
	public Vec3[] getVertices() {
		return vertices;
	}
	
	public Vec3[] getOriginalVertices() {
		return originalVertices;
	}
	
	public float sweepTest(ObjectBoundingBox otherBox, Vec3 vector) {
		float distance = getSweepDistance(otherBox, new Vec3(0,0,0), upAxis, vector);
		float disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), forwardAxis, vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), rightAxis, vector);
		if(disttest < distance) distance = disttest;
		distance = getSweepDistance(otherBox, new Vec3(0,0,0), otherBox.upAxis, vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), otherBox.forwardAxis, vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), otherBox.rightAxis, vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), upAxis.cross(otherBox.upAxis), vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), forwardAxis.cross(otherBox.forwardAxis), vector);
		if(disttest < distance) distance = disttest;
		disttest = getSweepDistance(otherBox, new Vec3(0,0,0), rightAxis.cross(rightAxis), vector);
		if(disttest < distance) distance = disttest;
		return distance;
	}
	
	private float getSweepDistance(ObjectBoundingBox otherBox, Vec3 origin, Vec3 vector, Vec3 distance) {
		Float low1 = null;
		Float high1 = null;
		
		Float low2 = null;
		Float high2 = null;
		
		for(Vec3 point : vertices) {
			float proj = project(point, origin, vector);
			if(low1 == null || proj < low1) {
				low1 = proj;
			}
			if(high1 == null || proj > high1) {
				high1 = proj;
			}
		}
		for(Vec3 point : otherBox.vertices) {
			float proj = project(point, origin, vector);
			if(low2 == null || proj < low2) {
				low2 = proj;
			}
			if(high2 == null || proj > high2) {
				high2 = proj;
			}
		}
		
		// convert the movement vector into 1d
		Vec3 pointt = distance;
		Vec3 aPoint = projectv(new Vec3(0,0,0), origin, vector);
		Vec3 aaPoint = projectv(pointt, origin, vector);
		
		float APoint = project(aPoint, origin, vector);
		float AAPoint = project(aaPoint, origin, vector);
		float pDirection = APoint - AAPoint;
		
		System.out.println(APoint + " " + AAPoint);
		
		if(glm.abs(pDirection) < 0.01f) {
            return 1; // movement is almost 0, it will probably never touch
        }
		
		float dist;
		if(low1 < low2) {
			dist = low2 - high1;
		} else {
			dist = low1 - high2;
		}
		
		float time = dist / pDirection;
		
		System.out.println("Time: " + time);
		if(time > 1f) return 1;
		if(time < 0f) return 1;
		return time;
	}
	
	private Vec3 projectv(Vec3 point, Vec3 origin, Vec3 vector) {
		// formula: A + dot(AP, AB) / dot(AB, AB) * AB
		Vec3 linePoint = origin.plus(vector); // another point on line
		Vec3 AP = point.minus(origin);
		Vec3 AB = linePoint.minus(origin);
		return origin.plus(AB.times(AP.dot(AB) / AB.dot(AB)));
	}
	
	private float project(Vec3 point, Vec3 origin, Vec3 vector) {
		return vector.dot(point.minus(origin)) / (vector.dot(vector));
	}
}

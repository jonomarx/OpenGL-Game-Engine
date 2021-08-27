/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.game;

import com.jonmarx.core.Entity;
import com.jonmarx.core.Mesh;
import com.jonmarx.core.Model;
import com.jonmarx.util.CollisionPacket;
import com.jonmarx.util.Plane;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.quat.Quat;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import java.util.HashMap;
import java.util.List;

/**
 * It's a world! stored in an octree, or quadtree - idk
 * @author Jon
 */
public class World {
    private Entity testingModel;
    /**
     * Loads a "octree, can support multiple entities too!"
     * @param modelsMap 
     */
    /*public World(HashMap<String, List<Entity>> modelsMap) {
        
    }*/
    public World(Entity model) {
        testingModel = model;
    }
    
    /**
     * Tests a collision based on an object and everything else in within an area
     * @param packet output packet
     */
    public void testCollision(CollisionPacket packet) {
        packet.collided = false;
        for(Mesh mesh : testingModel.getModel().getMeshes()) {
            for(int i = 0; i < mesh.getIndices().length; i+=3) {
                testCollision(packet, getVertex(mesh, testingModel.getLocrot(), mesh.getIndices()[i]), getVertex(mesh, testingModel.getLocrot(), mesh.getIndices()[i+1]), getVertex(mesh, testingModel.getLocrot(), mesh.getIndices()[i+2]));
            }
        }
    }
    
    private Vec3 getVertex(Mesh mesh, Mat4 matrix, int offset) {
        Vec4 tempVertex = new Vec4(mesh.getVertices()[offset].getPos());
        tempVertex = matrix.times(tempVertex);
        return new Vec3(tempVertex);
        //return mesh.getVertices()[offset].getPos();
    }
    
    private void testCollision(CollisionPacket packet, Vec3 p1, Vec3 p2, Vec3 p3) {
        Vec3 intersectionPoint = new Vec3();
        boolean collided = false;
        boolean embedded = false;
        p1 = p1.div(packet.eSpace);
        p2 = p2.div(packet.eSpace);
        p3 = p3.div(packet.eSpace);
        
        Plane plane = new Plane(p1, p2, p3);
        //if(!plane.isFrontFacing(packet.nVelocity)) return;
        float signedDist = plane.distanceTo(packet.basePoint);
        float nDotVelocity = (plane.getNormal().dot(packet.velocity));
        float t0;
        float t1;
        if(nDotVelocity == 0) {
            if(glm.abs(signedDist) >= 1f) {
                return;
            }
            embedded = true;
            t0 = 0;
            t1 = 1;
        } else {
            t0 = (-1 - signedDist) / nDotVelocity;
            t1 = (1 - signedDist) / nDotVelocity;
            
            if(t0 > t1) {
                float temp = t1;
                t1 = t0;
                t0 = temp;
            }
            
            if(t0 > 1f || t1 < 0f) {
                return;
            }
            
            if(t0 < 0) t0 = 0;
            if(t0 > 1) t0 = 1;
            if(t1 < 0) t1 = 0;
            if(t1 > 0) t1 = 1;
        }
        float t = 1f;
        
        if(!embedded) {
            Vec3 planeIntersectionPoint = packet.basePoint.minus(plane.getNormal()).plus(packet.velocity.times(t0));
            if(isPointInTriangle(planeIntersectionPoint, p1, p2, p3)) {
                intersectionPoint = planeIntersectionPoint;
                collided = true;
                t = t0;
            }
        }
        
        if(!collided) {
            float velocitySquaredLength = glm.pow(glm.length(packet.velocity), 2);
            float a = velocitySquaredLength;
            
            float b = 2 * (packet.velocity.dot(packet.basePoint.minus(p1)));
            float c = glm.pow(glm.length(p1.minus(packet.basePoint)), 2) - 1;
            float result = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                t = result;
                collided = true;
                intersectionPoint = p1;
            }
            b = 2 * (packet.velocity.dot(packet.basePoint.minus(p2)));
            c = glm.pow(glm.length(p2.minus(packet.basePoint)), 2) - 1;
            result  = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                t = result;
                collided = true;
                intersectionPoint = p2;
            }
            b = 2 * (packet.velocity.dot(packet.basePoint.minus(p3)));
            c = glm.pow(glm.length(p3.minus(packet.basePoint)), 2) - 1;
            result = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                t = result;
                collided = true;
                intersectionPoint = p3;
            }
            
            Vec3 edge = p2.minus(p1);
            Vec3 baseToVertex = p1.minus(packet.basePoint);
            float edgeSquaredLength = glm.pow(glm.length(edge), 2);
            float edgeDotVelocity = edge.dot(packet.velocity);
            float edgeDotBaseToVertex = edge.dot(baseToVertex);
            
            a = edgeSquaredLength * -velocitySquaredLength + glm.pow(edgeDotVelocity, 2);
            b = edgeSquaredLength * (2 * packet.velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - glm.pow(glm.length(baseToVertex), 2)) + glm.pow(edgeDotBaseToVertex, 2);
            result = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                float f = (edgeDotVelocity * result - edgeDotBaseToVertex) / edgeSquaredLength;
                if(f >= 0 && f <= 1) {
                    t = result;
                    collided = true;
                    intersectionPoint = p1.plus(edge.times(f));
                }
            }
            
            edge = p3.minus(p2);
            baseToVertex = p2.minus(packet.basePoint);
            edgeSquaredLength = glm.pow(glm.length(edge), 2);
            edgeDotVelocity = edge.dot(packet.velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength + glm.pow(edgeDotVelocity, 2);
            b = edgeSquaredLength * (2 * packet.velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - glm.pow(glm.length(baseToVertex), 2)) + glm.pow(edgeDotBaseToVertex, 2);
            result = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                float f = (edgeDotVelocity * result - edgeDotBaseToVertex) / edgeSquaredLength;
                if(f >= 0 && f <= 1) {
                    t = result;
                    collided = true;
                    intersectionPoint = p2.plus(edge.times(f));
                }
            }
            
            edge = p1.minus(p3);
            baseToVertex = p3.minus(packet.basePoint);
            edgeSquaredLength = glm.pow(glm.length(edge), 2);
            edgeDotVelocity = edge.dot(packet.velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength + glm.pow(edgeDotVelocity, 2);
            b = edgeSquaredLength * (2 * packet.velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - glm.pow(glm.length(baseToVertex), 2)) + glm.pow(edgeDotBaseToVertex, 2);
            result = solveQuadratic(a, b, c, t);
            if(result != Float.MIN_VALUE) {
                float f = (edgeDotVelocity * result - edgeDotBaseToVertex) / edgeSquaredLength;
                if(f >= 0 && f <= 1) {
                    t = result;
                    collided = true;
                    intersectionPoint = p3.plus(edge.times(f));
                }
            }
        }
        
        if(collided) {
            float distToCollision = t * glm.length(packet.velocity);
            if(!packet.collided || distToCollision < packet.nearestDistance) {
                packet.nearestDistance = distToCollision;
                packet.intersectionPoint = intersectionPoint;
                packet.collided = true;
            }
        }
    }
    
    /*private boolean isPointInTriangle(Vec3 point, Vec3 p1, Vec3 p2, Vec3 p3) {
        // source https://gdbooks.gitbooks.io/3dcollisions/content/Chapter4/point_in_triangle.html
        Vec3 p = point;
        Vec3 a = p1;
        Vec3 b = p2;
        Vec3 c = p3;
        
        System.out.println();
        System.out.println(point + ": " + "(" + p1 + ", " + p2 + ", " + p3 + ")");
        
        a = a.minus(p);
        b = b.minus(p);
        c = c.minus(p);
        
        Vec3 u = b.cross(c);
        Vec3 v = c.cross(a);
        Vec3 w = a.cross(b);
        
        if(u.dot(v) < 0f) {
            return false;
        }
        if(u.dot(w) < 0f) {
            return false;
        }
        
        return true;
    }*/
    private boolean isPointInTriangle(Vec3 point, Vec3 p1, Vec3 p2, Vec3 p3) {
        // i stole this function from Appendix C
        // written by Keidy from Mr-Gamemaker
        Vec3 e10 = p2.minus(p1);
        Vec3 e20 = p3.minus(p1);
        
        float a = e10.dot(e10);
        float b = e10.dot(e20);
        float c = e20.dot(e20);
        float acBB = (a*c) - (b*b);
        Vec3 vp = new Vec3(point.getX() - p1.getX(), point.getY() - p1.getY(), point.getZ() - p1.getZ());
        
        float d = vp.dot(e10);
        float e = vp.dot(e20);
        float x = (d*c) - (e*b);
        float y = (e*a) - (d*b);
        float z = x+y - acBB;
        
        return ((in(z)& ~(in(x) | in(y))) & 0x80000000) > 0;
        // note: ~ is bitwise !
    }
    
    private long in(float f) {
        return (int) f & 0x00000000ffffffffL;
    }
    
    private float solveQuadratic(float a, float b, float c, float max) {
        float determinant = b * b - 4f * a * c;
        if(determinant < 0f) return Float.MIN_VALUE;
        
        float sqrtD = glm.sqrt(determinant);
        float r1 = (-b - sqrtD) / (2 * a);
        float r2 = (-b + sqrtD) / (2 * a);
        
        if(r1 > r2) {
            float temp = r2;
            r2 = r1;
            r1 = temp;
        }
        
        if(r1 > 0 && r1 < max) {
            return r1;
        }
        
        if(r2 > 0 && r2 < max) {
            return r2;
        }
        return Float.MIN_VALUE;
    }
}

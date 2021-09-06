/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.util;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

/**
 * 2D box
 * @author Jon
 */
public class ConvexCollisionQuad extends ConvexCollisionBox {
    /**
     * Point structure:
     * v1->v2
     * v2->v3
     * v3->v4
     * v4->v1
     * @param vertices
     * @param transform 
     */
    public ConvexCollisionQuad(Vec3[] vertices, Mat4 transform) {
        super(vertices, 4, transform); // 4 edges/sides on a rect
        if(vertices.length != 4) {
            throw new RuntimeException("Ouch! not 4 sides in ConvexCollisionQuad");
        }
        edgeIndices = new int[] {0,1,1,2,2,3,3,0};
    }

    @Override
    protected void recacheEdges() {
        for(int i = 0; i < 4; i++) {
            Vec3 p1 = cachedVertices[i];
            Vec3 p2 = cachedVertices[(i+1)%4];
            Vec3 edge = p1.minus(p2);
            cachedEdges[i] = new Plane(p1, edge.normalize());
        }
    }
}

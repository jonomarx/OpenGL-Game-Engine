/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.geom;

import com.jonmarx.core.Vertex;
import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Jon
 */
public class CubeMeshGenerator {
    public static int TOP = 0;
    public static int BOTTOM = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;
    public static int FORWARD = 4;
    public static int BACKWARD = 5;
    
    public static Object[] generateCubeMesh(int[][][] data, HashMap<String, Vec2> table, Vec2 distance) {
        LinkedList<Vertex> vdata = new LinkedList<>();
        LinkedList<Integer> idata = new LinkedList<>();
        
        for(int x = 0; x < data.length; x++) {
            for(int y = 0; y < data[0].length; y++) {
                for(int z = 0; z < data[0][0].length; z++) {
                    if(data[x][y][z] == 0) continue;
                    if(x == 0 || data[x-1][y][z] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " RIGHT") == null) System.out.println(data[x][y][z] + " RIGHT");
                        generateFace(vdata, idata, RIGHT, table.get(data[x][y][z] + " RIGHT"), distance, new Vec3(x,y,z));
                    }
                    if(x == 15 || data[x+1][y][z] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " LEFT") == null) System.out.println(data[x][y][z] + " LEFT");
                        generateFace(vdata, idata, LEFT, table.get(data[x][y][z] + " LEFT"), distance, new Vec3(x,y,z));
                    }
                    
                    if(y == 0 || data[x][y-1][z] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " BOTTOM") == null) System.out.println(data[x][y][z] + " BOTTOM");
                        generateFace(vdata, idata, BOTTOM, table.get(data[x][y][z] + " BOTTOM"), distance, new Vec3(x,y,z));
                    }
                    if(y == 15 || data[x][y+1][z] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " TOP") == null) System.out.println(data[x][y][z] + " TOP");
                        generateFace(vdata, idata, TOP, table.get(data[x][y][z] + " TOP"), distance, new Vec3(x,y,z));
                    }
                    
                    if(z == 0 || data[x][y][z-1] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " FORWARD") == null) System.out.println(data[x][y][z] + " FORWARD");
                        generateFace(vdata, idata, FORWARD, table.get(data[x][y][z] + " FORWARD"), distance, new Vec3(x,y,z));
                    }
                    if(z == 15 || data[x][y][z+1] == 0) { // test if edge or if edge ise
                        if(table.get(data[x][y][z] + " BACKWARD") == null) System.out.println(data[x][y][z] + " BACKWARD");
                        generateFace(vdata, idata, BACKWARD, table.get(data[x][y][z] + " BACKWARD"), distance, new Vec3(x,y,z));
                    }
                }
            }
        }
        Object[] out = new Object[] {vdata, idata};
        return out;
    }
    
    public static void generateFace(LinkedList<Vertex> vdata, LinkedList<Integer> idata, int faceID, Vec2 texID, Vec2 texOffset, Vec3 offset) {
        Vertex v1 = null;
        Vertex v2 = null;
        Vertex v3 = null;
        Vertex v4 = null;
        
        Vec3 n0 = new Vec3(0, 1, 0);
        Vec3 n1 = new Vec3(0, -1, 0);
        Vec3 n2 = new Vec3(1, 0, 0);
        Vec3 n3 = new Vec3(-1, 0, 0);
        Vec3 n4 = new Vec3(0, 0, -1);
        Vec3 n5 = new Vec3(0, 0, 1);
        
        switch(faceID) {
            case 0:
                v1 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, -0.5)), n0, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, 0.5)), n0, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(0.5, 0.5, 0.5)), n0, texID.getX(), texID.getY());
                v4 = new Vertex(offset.plus(new Vec3(0.5, 0.5, -0.5)), n0, texID.getX(), texID.getY());
                break;
            case 1:
                v4 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, -0.5)), n1, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, 0.5)), n1, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(0.5, -0.5, 0.5)), n1, texID.getX(), texID.getY());
                v1 = new Vertex(offset.plus(new Vec3(0.5, -0.5, -0.5)), n1, texID.getX(), texID.getY());
                break;
            case 3:
                v1 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, 0.5)), n3, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, 0.5)), n3, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, -0.5)), n3, texID.getX(), texID.getY());
                v4 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, -0.5)), n3, texID.getX(), texID.getY());
                break;
            case 2:
                v4 = new Vertex(offset.plus(new Vec3(0.5, -0.5, 0.5)), n2, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(0.5, 0.5, 0.5)), n2, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(0.5, 0.5, -0.5)), n2, texID.getX(), texID.getY());
                v1 = new Vertex(offset.plus(new Vec3(0.5, -0.5, -0.5)), n2, texID.getX(), texID.getY());
                break;
            case 4:
                v1 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, -0.5)), n4, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, -0.5)), n4, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(0.5, 0.5, -0.5)), n4, texID.getX(), texID.getY());
                v4 = new Vertex(offset.plus(new Vec3(0.5, -0.5, -0.5)), n4, texID.getX(), texID.getY());
                break;
            case 5:
                v4 = new Vertex(offset.plus(new Vec3(-0.5, -0.5, 0.5)), n5, texID.getX(), texID.getY());
                v3 = new Vertex(offset.plus(new Vec3(-0.5, 0.5, 0.5)), n5, texID.getX(), texID.getY());
                v2 = new Vertex(offset.plus(new Vec3(0.5, 0.5, 0.5)), n5, texID.getX(), texID.getY());
                v1 = new Vertex(offset.plus(new Vec3(0.5, -0.5, 0.5)), n5, texID.getX(), texID.getY());
                break;
        }
        
        int off = vdata.size();
        Integer[] numbers = {0+off,1+off,3+off,1+off,2+off,3+off};
        
        v1.setTx(texID.getX()+texOffset.getX());v1.setTy(texID.getY()+texOffset.getY());
        v2.setTx(texID.getX()+texOffset.getX());v2.setTy(texID.getY());
        v3.setTx(texID.getX());v3.setTy(texID.getY());
        v4.setTx(texID.getX());v4.setTy(texID.getY()+texOffset.getY());
        
        vdata.add(v1);
        vdata.add(v2);
        vdata.add(v3);
        vdata.add(v4);
        idata.addAll(Arrays.asList(numbers));
    }
}

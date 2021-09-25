/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import com.jonmarx.animation.Animator;
import com.jonmarx.animation.BoneInfo;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import glm_.vec4.Vec4i;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_DIFFUSE;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiGetMaterialTexture;
import static org.lwjgl.assimp.Assimp.aiTextureType_DIFFUSE;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

/**
 * multiple meshes, to be used in conjunction with assimp
 * CAN support animation - contains bone data
 * @author Jon
 */
public class Model {
    protected Map<String, BoneInfo> mBoneInfoMap = new HashMap<>();
    protected int boneCounter = 0;
    protected List<Mesh> meshes = new ArrayList<>();
    protected List<Texture> textures = new ArrayList<>();
    protected Animator animator;
    protected String path;
    
    public Model(AIScene scene, String fileName, String basePath) {
        path = fileName;
        
        PointerBuffer materialBuffer = scene.mMaterials();
        AIMaterial[] materials = new AIMaterial[scene.mNumMaterials()];
        for(int i = 0; i < materials.length; i++) {
            materials[i] = AIMaterial.create(materialBuffer.get(i));
            generateMaterial(materials[i], basePath);
        }
        
        for(int i = 0; i < scene.mNumMeshes(); i++) {
            meshes.add(processMesh(AIMesh.create(scene.mMeshes().get(i)), scene));
        }
    }
    
    public Map<String, BoneInfo> getBoneInfoMap() {
        return mBoneInfoMap;
    }
    
    public void setBoneCount(int boneCount) {
        boneCounter = boneCount;
    }
    
    public int getBoneCount() {
        return boneCounter;
    }
    
    private void generateMaterial(AIMaterial material, String path) {
        AIString pathh = AIString.calloc();
        aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, pathh, (IntBuffer)null, null, null, null, null, null);
        if(pathh.dataString().length() > 0) {
            textures.add(new Texture(path + pathh.dataString().replaceAll("\\\\", "")));
        } else {
            AIColor4D color = AIColor4D.create();
            aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
            BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_3BYTE_BGR);
            Graphics g = image.getGraphics();
            g.setColor(new Color(color.r(), color.g(), color.b()));
            g.fillRect(0, 0, 1, 1);
            g.dispose();
            
            textures.add(new Texture(image));
        }
    }
    
    private void extractBoneWeights(Vertex[] vertices, AIMesh mesh, AIScene scene) {
        for(int boneIndex = 0; boneIndex < mesh.mNumBones(); ++boneIndex) {
            int boneId = -1;
            String boneName = AIBone.create(mesh.mBones().get(boneIndex)).mName().dataString();
            if(mBoneInfoMap.get(boneName) == null) {
                BoneInfo newBone = new BoneInfo(boneCounter, boneName, toMatrix(AIBone.create(mesh.mBones().get(boneIndex)).mOffsetMatrix()));
                mBoneInfoMap.put(boneName, newBone);
                boneId = boneCounter;
                boneCounter++;
            } else {
                boneId = mBoneInfoMap.get(boneName).getBoneId();
            }
            AIVertexWeight.Buffer weights = AIBone.create(mesh.mBones().get(boneIndex)).mWeights();
            int numWeights = AIBone.create(mesh.mBones().get(boneIndex)).mNumWeights();
            for(int weightIndex = 0; weightIndex < numWeights; ++weightIndex) {
                int vertexId = weights.get(weightIndex).mVertexId();
                float weight = weights.get(weightIndex).mWeight();
                
                Vertex vertex = vertices[vertexId];
                for(int i = 0; i < 4; ++i) {
                    if(vertex.jointIndices.get(i) < 0) {
                        vertex.jointWeights.set(i, weight);
                        vertex.jointIndices.set(i, boneId);
                        break;
                    }
                }
            }
        }
    }
    
    private Mesh processMesh(AIMesh mesh, AIScene scene) {
        AIVector3D.Buffer vBuffer = mesh.mVertices();
        AIVector3D.Buffer nBuffer = mesh.mNormals();
        AIVector3D.Buffer tBuffer = mesh.mTextureCoords(0);
        
        Vertex[] vertices = new Vertex[mesh.mNumVertices()];
        for(int i = 0; i < vertices.length; i++) {
            AIVector3D vertex = vBuffer.get();
            AIVector3D normal = nBuffer.get();
            AIVector3D texPos = tBuffer.get();
            vertices[i] = new Vertex(new Vec3(vertex.x(), vertex.y(), vertex.z()), new Vec3(normal.x(), normal.y(), normal.z()), texPos.x(), 1f-texPos.y(), new Vec4(-1f), new Vec4i(-1f));
        }
        
        AIFace.Buffer iBuffer = mesh.mFaces();
        int[] indices = new int[mesh.mNumFaces() * 3];
        for(int i = 0; i < indices.length; i+=3) {
            IntBuffer miniBuffer = iBuffer.get().mIndices();
            if(miniBuffer.capacity() != 3) continue;
            indices[i + 0] = miniBuffer.get();
            indices[i + 1] = miniBuffer.get();
            indices[i + 2] = miniBuffer.get();
        }
        
        extractBoneWeights(vertices, mesh, scene);
        return new Mesh(vertices, indices, new Texture[] {textures.get(mesh.mMaterialIndex())}, new int[0], new float[0]);
    }
    
    private Mat4 toMatrix(AIMatrix4x4 mat) {
        Mat4 out = new Mat4();
        
        out.setA0(mat.a1());
        out.setA1(mat.a2());
        out.setA2(mat.a3());
        out.setA3(mat.a4());
        
        out.setB0(mat.b1());
        out.setB1(mat.b2());
        out.setB2(mat.b3());
        out.setB3(mat.b4());
        
        out.setC0(mat.c1());
        out.setC1(mat.c2());
        out.setC2(mat.c3());
        out.setC3(mat.c4());
        
        out.setD0(mat.d1());
        out.setD1(mat.d2());
        out.setD2(mat.d3());
        out.setD3(mat.d4());
        
        return out;
    }
    
    public Mesh[] getMeshes() {
        return meshes.toArray(new Mesh[0]);
    }
    
    public Animator getAnimator() {
        return animator;
    }
    
    public void setAnimator(Animator animator) {
        this.animator = animator;
    }
    
    public String getPath() {
        return path;
    }
}

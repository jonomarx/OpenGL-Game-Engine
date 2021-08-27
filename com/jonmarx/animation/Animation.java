/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.animation;

import com.jonmarx.core.Mesh;
import com.jonmarx.core.Model;
import java.util.List;

import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIScene;
/**
 *
 * @author Jon
 */
public class Animation {
    class NodeData {
        Mat4 transform;
        String name;
        int childrenCount;
        List<NodeData> children = new ArrayList<>();
    }
    
    private float mDuration;
    private int mTicksPerSecond;
    private List<Bone> mBones = new ArrayList<>();
    private NodeData mRootNode;
    private Map<String, BoneInfo> mBoneInfoMap = new HashMap<>();
    
    public Animation(AIScene scene, Model model, String name) {
        if(scene.mNumAnimations() == 0) return;
        AIAnimation animation = null;
        if(!name.equals("")) {
            for(int i = 0; i < scene.mNumAnimations(); i++) {
                AIAnimation temp = AIAnimation.create(scene.mAnimations().get(i));
                if(temp.mName().dataString().equals(name)) {
                    animation = temp;
                    break;
                }
            }
            if(animation == null) throw new RuntimeException("error: no animation found");
        } else {
            animation = AIAnimation.create(scene.mAnimations().get(0));
        }
        mDuration = (float) animation.mDuration();
        mTicksPerSecond = (int) animation.mTicksPerSecond();
        mRootNode = new NodeData();
        readHeirarchyData(mRootNode, scene.mRootNode());
        readMissingBones(animation, model);
    }
    
    public Bone findBone(String name) {
        for(Bone bone : mBones) {
            if(bone.getName().equals(name)) {
                return bone;
            }
        }
        return null;
    }
    
    public int getTicksPerSecond() {
        return mTicksPerSecond;
    }
    
    public float getDuration() {
        return mDuration;
    }
    
    public NodeData getRootNode() {
        return mRootNode;
    }
    
    public Map<String, BoneInfo> getBoneInfoMap() {
        return mBoneInfoMap;
    }
    
    private void readMissingBones(AIAnimation animation, Model model) {
        int size = animation.mNumChannels();
        
        Map<String, BoneInfo> boneInfoMap = model.getBoneInfoMap();
        int boneCount = model.getBoneCount();
        
        for(int i = 0; i < size; i++) {
            AINodeAnim channel = AINodeAnim.create(animation.mChannels().get(i));
            String boneName = channel.mNodeName().dataString();
            
            if(boneInfoMap.get(boneName) == null) {
                boneInfoMap.put(boneName, new BoneInfo(boneCount, boneName, new Mat4(1f)));
                model.setBoneCount(boneCount+1);
                boneCount++;
            }
            
            mBones.add(new Bone(channel.mNodeName().dataString(), boneInfoMap.get(channel.mNodeName().dataString()).getBoneId(), channel));
        }
        mBoneInfoMap = boneInfoMap;
    }
    
    private void readHeirarchyData(NodeData dest, AINode source) {
        dest.name = source.mName().dataString();
        dest.transform = toMatrix(source.mTransformation());
        dest.childrenCount = source.mNumChildren();
        
        for(int i = 0; i < source.mNumChildren(); i++) {
            NodeData newData = new NodeData();
            readHeirarchyData(newData, AINode.create(source.mChildren().get(i)));
            dest.children.add(newData);
        }
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
}

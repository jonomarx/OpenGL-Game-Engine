/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.animation;

import com.jonmarx.animation.Animation.NodeData;
import glm_.mat4x4.Mat4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jon
 */
public class Animator {
    private Mat4[] mFinalBoneMatrices = new Mat4[100];
    private Animation mCurrentAnimation;
    private float mCurrentTime;
    private float mDeltaTime;
    
    public Animator(Animation animation) {
        mCurrentTime = 0f;
        mCurrentAnimation = animation;
        
        Arrays.fill(mFinalBoneMatrices, new Mat4(1f));
    }
    
    public void playAnimation(Animation animation) {
        mCurrentAnimation = animation;
        mCurrentTime = 0f;
    }
    
    public void updateAnimation(float deltaTime) {
        mDeltaTime = deltaTime;
        if(mCurrentAnimation != null) {
            mCurrentTime += mCurrentAnimation.getTicksPerSecond() * deltaTime;
            mCurrentTime = mCurrentTime % mCurrentAnimation.getDuration();
            calculateBoneTransform(mCurrentAnimation.getRootNode(), new Mat4(1f));
        }
    }
    
    public void calculateBoneTransform(NodeData node, Mat4 parentTransform) {
        if(node == null) return;
        String nodeName = node.name;
        Mat4 nodeTransform = node.transform;
        
        Bone bone = mCurrentAnimation.findBone(nodeName);
        if(bone != null) {
            bone.update(mCurrentTime);
            nodeTransform = bone.getLocalTransform();
        }
        
        Mat4 globalTransform = parentTransform.times(nodeTransform);
        
        Map<String, BoneInfo> boneInfoMap = mCurrentAnimation.getBoneInfoMap();
        if(boneInfoMap.get(nodeName) != null) {
            int index = boneInfoMap.get(nodeName).getBoneId();
            Mat4 offset = boneInfoMap.get(nodeName).getMatrix();
            mFinalBoneMatrices[index] = globalTransform.times(offset);
        }
        
        for(int i = 0; i < node.childrenCount; i++) {
            calculateBoneTransform(node.children.get(i), globalTransform);
        }
    }
    
    public Mat4[] getMatrices() {
        return mFinalBoneMatrices;
    }
}

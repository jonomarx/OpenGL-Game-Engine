/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.animation;

import glm_.mat4x4.Mat4;
import glm_.quat.Quat;
import glm_.vec3.Vec3;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIVector3D;

import static glm_.Java.glm;

/**
 *
 * @author Jon
 */
public class Bone {
    class KeyPosition {
        Vec3 position;
        float timeStamp;
    }
    class KeyRotation {
        Quat orientation;
        float timeStamp;
    }
    class KeyScale {
        Vec3 scale;
        float timeStamp;
    }
    
    private List<KeyPosition> mPositions = new ArrayList<>();
    private List<KeyRotation> mRotations = new ArrayList<>();
    private List<KeyScale> mScales = new ArrayList<>();
    
    private int mNumPositions;
    private int mNumRotations;
    private int mNumScales;
    
    private Mat4 mLocalTransform;
    private String mName;
    private int mId;
    
    public Bone(String name, int id, AINodeAnim channel) {
        mName = name;
        mId = id;
        mLocalTransform = new Mat4(1f);
        
        mNumPositions = channel.mNumPositionKeys();
        for(int i = 0; i < mNumPositions; ++i) {
            AIVector3D aiPosition = channel.mPositionKeys().get(i).mValue();
            float timeStamp = (float) channel.mPositionKeys().get(i).mTime();
            KeyPosition data = new KeyPosition();
            data.position = toVec3(aiPosition);
            data.timeStamp = timeStamp;
            mPositions.add(data);
        }
        mNumRotations = channel.mNumRotationKeys();
        for(int i = 0; i < mNumRotations; ++i) {
            AIQuaternion aiRotation = channel.mRotationKeys().get(i).mValue();
            float timeStamp = (float) channel.mRotationKeys().get(i).mTime();
            KeyRotation data = new KeyRotation();
            data.orientation = toQuat(aiRotation);
            data.timeStamp = timeStamp;
            mRotations.add(data);
        }
        mNumScales = channel.mNumScalingKeys();
        for(int i = 0; i < mNumScales; i++) {
            AIVector3D aiScale = channel.mScalingKeys().get(i).mValue();
            float timeStamp = (float) channel.mScalingKeys().get(i).mTime();
            KeyScale data = new KeyScale();
            data.scale = toVec3(aiScale);
            data.timeStamp = timeStamp;
            mScales.add(data);
        }
    }
    
    public int getId() {
        return mId;
    }
    
    public String getName() {
        return mName;
    }
    
    public Mat4 getLocalTransform() {
        return mLocalTransform;
    }
    
    public void update(float time) {
        Mat4 translation = interpolatePosition(time);
        Mat4 rotation = interpolateRotation(time);
        Mat4 scale = interpolateScale(time);
        mLocalTransform = translation.times(rotation).times(scale);
    }
    
    public int getPositionIndex(float time) {
        for(int i = 0; i < mNumPositions - 1; ++i) {
            if(time < mPositions.get(i + 1).timeStamp) return i;
        }
        throw new RuntimeException();
    }
    
    public int getRotationIndex(float time) {
        for(int i = 0; i < mNumRotations - 1; i++) {
            if(time < mRotations.get(i + 1).timeStamp) return i;
        }
        throw new RuntimeException();
    }
    
    public int getScaleIndex(float time) {
        for(int i = 0; i < mNumScales - 1; i++) {
            if(time < mScales.get(i + 1).timeStamp) return i;
        }
        throw new RuntimeException();
    }
    
    private float getScaleFactor(float lastTime, float nextTime, float time) {
        float midway = time - lastTime;
        float frames = nextTime - lastTime;
        return midway / frames;
    }
    
    private Mat4 interpolatePosition(float time) {
        if(mNumPositions == 1) {
            return glm.translate(new Mat4(1f), mPositions.get(0).position);
        }
        
        int p0Index = getPositionIndex(time);
        int p1Index = p0Index + 1;
        float scaleFactor = getScaleFactor(mPositions.get(p0Index).timeStamp, mPositions.get(p1Index).timeStamp, time);
        Vec3 finalPos = glm.mix(mPositions.get(p0Index).position, mPositions.get(p1Index).position, scaleFactor);
        
        return glm.translate(new Mat4(1f), finalPos);
    }
    
    private Mat4 interpolateRotation(float time) {
        if(mNumRotations == 1) {
            Quat rotation = mRotations.get(0).orientation.normalize();
            return glm.toMat4(rotation);
        }
        
        int p0Index = getRotationIndex(time);
        int p1Index = p0Index + 1;
        float rotationFactor = getScaleFactor(mRotations.get(p0Index).timeStamp, mPositions.get(p1Index).timeStamp, time);
        Quat finalRotation = glm.slerp(mRotations.get(p0Index).orientation, mRotations.get(p1Index).orientation, rotationFactor);
        finalRotation = finalRotation.normalize();
        
        return glm.toMat4(finalRotation);
    }
    
    private Mat4 interpolateScale(float time) {
        if(mNumScales == 1) {
            return glm.scale(new Mat4(1f), mScales.get(0).scale);
        }
        
        int p0Index = getScaleIndex(time);
        int p1Index = p0Index + 1;
        float scaleFactor = getScaleFactor(mScales.get(p0Index).timeStamp, mScales.get(p1Index).timeStamp, time);
        Vec3 finalScale = glm.mix(mScales.get(p0Index).scale, mScales.get(p1Index).scale, scaleFactor);
        
        return glm.scale(new Mat4(1f), finalScale);
    }
    
    private Vec3 toVec3(AIVector3D vec) {
        return new Vec3(vec.x(), vec.y(), vec.z());
    }
    
    private Quat toQuat(AIQuaternion quat) {
        return new Quat(quat.w(), quat.x(), quat.y(), quat.z());
    }
}

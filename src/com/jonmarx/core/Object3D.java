package com.jonmarx.core;

import glm_.mat4x4.Mat4;

/**
 * An Object that exists in 3d space
 * does nothing on its own, i guess you could render a dot?
 * @author Jon
 */
public abstract class Object3D {
    /**
     * location and position of object
     */
    protected Mat4 locrot;
    
    public Object3D(Mat4 locrot) {
        this.locrot = locrot;
    }
    
    public void setLocrot(Mat4 locrot) {
        this.locrot = locrot;
    }
    
    public Mat4 getLocrot() {
        return locrot;
    }
}

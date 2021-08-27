/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jonmarx.core;

import com.jonmarx.animation.Animation;
import com.jonmarx.animation.Animator;
import com.jonmarx.animation.BoneInfo;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.assimp.*;
import static org.lwjgl.assimp.Assimp.*;
import org.lwjgl.PointerBuffer;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.demo.util.IOUtil.*;
import glm_.vec4.Vec4;
import glm_.vec4.Vec4i;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Jon
 */
public class MeshLoader {
    public static Model loadMesh(String filename, String path, String animationName) {
        // "I stole it" - Andy
        // https://github.com/LWJGL/lwjgl3-demos/blob/main/src/org/lwjgl/demo/opengl/assimp/WavefrontObjDemo.java
        AIFileIO fileIO = AIFileIO.create().OpenProc(new AIFileOpenProcI() {
            @Override
            public long invoke(long pFileIO, long fileName, long openMode) {
                final ByteBuffer data;
                String addr = memUTF8(fileName);
                try {
                    data = ioResourceToByteBuffer(addr, 8192);
                } catch(IOException e) {
                    System.err.println("Failed to open file: " + addr);
                    throw new RuntimeException(e);
                }
                return AIFile.create().ReadProc(new AIFileReadProcI() {
                    @Override
                    public long invoke(long pFile, long pBuffer, long size, long count) {
                        long max = Math.min(data.remaining(), size * count);
                        memCopy(memAddress(data) + data.position(), pBuffer, max);
                        return max;
                    }
                    
                }).SeekProc(new AIFileSeekI() {
                    @Override
                    public int invoke(long pFile, long offset, int origin) {
                        if(origin == aiOrigin_CUR) {
                            data.position(data.position() + (int) offset);
                        } else if(origin == aiOrigin_SET) {
                            data.position((int)offset);
                        } else if(origin == aiOrigin_END) {
                            data.position(data.limit() + (int) offset);
                        }
                        return 0;
                    }
                    
                }).FileSizeProc(new AIFileTellProcI() {
                    @Override
                    public long invoke(long pFile) {
                        return data.limit();
                    }
                    
                }).address();
            }
        }).CloseProc(new AIFileCloseProcI() {
            @Override
            public void invoke(long pFileIO, long pFile) {
                AIFile aiFile = AIFile.create(pFile);
                aiFile.ReadProc().free();
                aiFile.SeekProc().free();
                aiFile.FileSizeProc().free();
            }
        });
        
        AIScene scene = aiImportFileEx(filename, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals, fileIO);
        if(scene == null) {
            System.out.println("ERROR IMPORTING");
            return null;
        }
        Model out = new Model(scene, filename, path);
        Animation animation = new Animation(scene, out, animationName);
        out.setAnimator(new Animator(animation));
        scene.free();
        return out;
    }
    
    public static Model loadMesh(String fileName, String path) {
        return loadMesh(fileName, path, "");
    }
}

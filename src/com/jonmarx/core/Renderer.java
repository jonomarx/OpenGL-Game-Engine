package com.jonmarx.core;

import com.jonmarx.game.ECSEntity;
import com.jonmarx.gfx.PostProcessingShader;
import static glm_.Java.glm;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Jon
 */
public class Renderer {
    private static int modelVAO;
    
    private static int fbo;
    private static int fboTexture;
    private static int fboDrawTexture;
    
    private static int rbo;
    private static int screenVAO;
    private static int screenVBO;
    
    private static int shadowWidth;
    private static int shadowHeight;
    private static int depthMapFBO;
    private static int depthMap;
    private static Shader depthShader;
    
    private static Camera camera = new Camera();
    
    private static int x = 0;
    private static int y = 0;
    
    private static List<PostProcessingShader> shaders = new ArrayList<>();
    
    private static Vec3 lightPos = new Vec3();
    private static boolean forceScreenFBO = false;
    private static Texture blankTexture;
    
    public static void init(int xx, int yy, boolean forceScreenFBO) {
        x = xx;
        y = yy;
        Renderer.forceScreenFBO = forceScreenFBO;
        
        modelVAO = glGenVertexArrays();
        Cleanup.addVAO(modelVAO);
        glBindVertexArray(modelVAO);
        
        blankTexture = new Texture("/res/textures/blank.png");
        
        if(!forceScreenFBO) {
            fbo = glGenFramebuffers();
            Cleanup.addFBO(fbo);
            fboTexture = glGenTextures();
            Cleanup.addTexture(fboTexture);
            fboDrawTexture = glGenTextures();
            Cleanup.addTexture(fboDrawTexture);
            glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        
            glBindTexture(GL_TEXTURE_2D, fboDrawTexture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x, y, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        
            glBindTexture(GL_TEXTURE_2D, fboTexture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x, y, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        
            glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTexture, 0);
            glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, fboDrawTexture, 0);
            glDrawBuffer(GL_COLOR_ATTACHMENT1);
        
            rbo = glGenRenderbuffers();
            Cleanup.addRBO(rbo);
            glBindRenderbuffer(GL_RENDERBUFFER, rbo);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, x, y);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);
        
            if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
                System.out.println("framebuffer failed :/");
            }
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
            screenVAO = glGenVertexArrays();
            Cleanup.addVAO(screenVAO);
        
            screenVBO = glGenBuffers();
            Cleanup.addVBO(screenVBO);
            glBindBuffer(GL_ARRAY_BUFFER, screenVBO);
            FloatBuffer buffer = BufferUtils.createFloatBuffer(24);
            buffer.put(new float[] {-1.0f,  1.0f,  0.0f, 1.0f,-1.0f, -1.0f,  0.0f, 0.0f,1.0f, -1.0f,  1.0f, 0.0f,-1.0f,  1.0f,  0.0f, 1.0f,1.0f, -1.0f,  1.0f, 0.0f,1.0f,  1.0f,  1.0f, 1.0f});
            ((Buffer) buffer).flip();
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        
            depthMapFBO = glGenFramebuffers();
            Cleanup.addFBO(depthMapFBO);
            shadowWidth = 2048;
            shadowHeight = 2048;
            depthMap = glGenTextures();
            Cleanup.addTexture(depthMap);
            glBindTexture(GL_TEXTURE_2D, depthMap);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowWidth, shadowHeight, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
            glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
        
            glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0);
            glDrawBuffer(GL_NONE);
            glReadBuffer(GL_NONE);
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
            depthShader = new Shader("/res/shaders/shadowShader.vs", "/res/shaders/shadowShader.fs");
        }
    }
    
    /**
     * Renders a static model.
     * @param model
     * @param modelMat
     * @param shader 
     */
    public static void renderStaticModel(Mesh model, Mat4 modelMat, Shader shader) {
        int i;
        // 1280
        for(i = 0; i < model.textures.length; i++) {
            glActiveTexture(GL_TEXTURE1 + i);
            glBindTexture(GL_TEXTURE_2D, model.textures[i].id);
        }
        for(i = i; i < 31; i++) {
            glActiveTexture(GL_TEXTURE1 + i);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        
        glBindVertexArray(modelVAO);
        glUseProgram(shader.program);
        
        glBindBuffer(GL_ARRAY_BUFFER, model.vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, model.ibo);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 16 * 4, 0 * 4);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 16 * 4, 3 * 4);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 16 * 4, 6 * 4);
        glVertexAttribPointer(3, 4, GL_FLOAT, false, 16 * 4, 8 * 4);
        glVertexAttribIPointer(4, 4, GL_INT, 16 * 4, 12 * 4);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
        
        shader.setUniform("model", modelMat);
        glDrawElements(GL_TRIANGLES, model.indicies.length, GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        glUseProgram(0);
        glBindVertexArray(0);
        
        for(int j = 0; i < model.textures.length; j++) {
            glActiveTexture(GL_TEXTURE0 + j);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }
    
    /**
     * Renders a MultiEntity
     * @param entity
     * @param shader
     */
    public static void renderStaticModel(Entity entity, Shader shader) {
        if(entity.getModel() == null) return;
        if(entity.getModel().getMeshes() == null) return;
        
        for(Mesh model : entity.getModel().getMeshes()) {
            Mat4[] jointsMatrix = entity.getModel().getAnimator().getMatrices();
            glUseProgram(shader.program);
            shader.setUniform("jointsMatrix", jointsMatrix);
            renderStaticModel(model, entity.getLocrot(), shader);
        }
    }
    
    public static void renderStaticModel(ECSEntity entity, Shader shader) {
        if(entity.getField("model") == null) return;
        Model mod = MemoryCache.getModel((String) entity.getField("model"));
        if(mod == null) return;
        
        for(Mesh model : mod.getMeshes()) {
            Mat4[] jointsMatrix = mod.getAnimator().getMatrices();
            glUseProgram(shader.program);
            shader.setUniform("jointsMatrix", jointsMatrix);
            
            Vec3 pos = (Vec3) entity.getField("position");
            Vec3 rot = (Vec3) entity.getField("rotation");
            Vec3 sca = (Vec3) entity.getField("scale");
            
            Mat4 locrot = new Mat4().translate(pos).rotateXYZ(0, glm.radians(-rot.getX()), glm.radians(rot.getY())).scale(sca);
            renderStaticModel(model, locrot, shader);
        }
    }
    
    private static void setup2DRender() {
    	glViewport(0, 0, x, y);
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        
        glClearColor(0.005f,0.005f,0.005f,1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
    }
    
    /**
     * 2D rendering, simpler, probably faster.
     * @param entities 
     */
    public static void simple2DRender(Game entities) {
        setup2DRender();
        Entity[] entityList = entities.getEntities();
        for(int i = 0; i < entityList.length; i++) {
            renderStaticModel(entityList[i], MemoryCache.getShader(entities.getEntity(i).getShader()));
        }
        render();
    }
    
    /**
     * Renders from the entityList
     */
    public static void renderFromList(Game entities) {
        if(forceScreenFBO) {
            glClearColor(glm.abs(glm.pow(0.005f,1f/2.2f)),glm.abs(glm.pow(0.005f,1f/2.2f)),glm.abs(glm.pow(0.005f,1f/2.2f)),1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glBindTexture(GL_TEXTURE_2D, blankTexture.id);
            glActiveTexture(GL_TEXTURE0);
            
            lightPos = new Vec3(0f,20f,0f);
            Mat4 lightProjection = glm.ortho(-10f, 10f, -10f, 10f, 0.1f, 100f);
            Mat4 lightView = glm.lookAt(lightPos, lightPos.minus(new Vec3(-1f, 0.1f, -1f).normalize()), new Vec3(0f,1f,0f));
            Mat4 lightSpaceMatrix = lightProjection.times(lightView);
            for(Entity entity : entities.getEntities()) {
            	String shaderr = entity.getShader();
                Shader shader = MemoryCache.getShader(shaderr);
                if(shader == null) continue;
                glUseProgram(shader.getProgram());
                shader.setUniform("view", camera.getView());
                shader.setUniform("viewPos", camera.getPos());
                shader.setUniform("projection", glm.perspective(glm.radians(45.0f), ((float)x) / y, 0.01f, 1000.0f));
                shader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
                shader.setUniform("shadowTex", 0);
                shader.setUniform("lightPos", lightPos);
                shader.setUniform("dirLight.dir", lightPos.times(1).normalize());
                glUseProgram(0);
            }
            
            Entity[] entityList = entities.getEntities();
            for(int i = 0; i < entityList.length; i++) {
                renderStaticModel(entityList[i], MemoryCache.getShader(entities.getEntity(i).getShader()));
            }
        } else {
            prepareShadows();
            lightPos = new Vec3(0f,10f,-36f);
            glUseProgram(depthShader.program);
            Mat4 lightProjection = glm.ortho(-10f, 10f, -10f, 10f, 0.1f, 100f);
            Mat4 lightView = glm.lookAt(lightPos, lightPos.minus(new Vec3(0f, 1f, -2f).normalize()), new Vec3(0f,1f,0f));
            Mat4 lightSpaceMatrix = lightProjection.times(lightView);
            depthShader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
            depthShader.setUniform("view", camera.getView());
            glUseProgram(0);
            
            Entity[] entityList = entities.getEntities();
            for(int i = 0; i < entityList.length; i++) {
                renderStaticModel(entityList[i], depthShader);
            }
            prepare();
            for(Entity entity : entityList) {
            	String shaderr = entity.getShader();
                Shader shader = MemoryCache.getShader(shaderr);
                if(shader == null) continue;
                glUseProgram(shader.getProgram());
                shader.setUniform("view", camera.getView());
                shader.setUniform("viewPos", camera.getPos());
                shader.setUniform("projection", glm.perspective(glm.radians(45.0f), ((float)x) / y, 0.01f, 1000.0f));
                shader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
                shader.setUniform("shadowTex", 0);
                shader.setUniform("lightPos", lightPos);
                shader.setUniform("dirLight.dir", lightPos.times(1).normalize());
                glUseProgram(0);
            }
            
            for(int i = 0; i < entityList.length; i++) {
                renderStaticModel(entityList[i], MemoryCache.getShader(entities.getEntity(i).getShader()));
            }
            render();
        }
    }
    
    public static void renderFromList(List<ECSEntity> entities) {
        if(forceScreenFBO) {
            glClearColor(glm.abs(glm.pow(0.005f,1f/2.2f)),glm.abs(glm.pow(0.005f,1f/2.2f)),glm.abs(glm.pow(0.005f,1f/2.2f)),1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glBindTexture(GL_TEXTURE_2D, blankTexture.id);
            glActiveTexture(GL_TEXTURE0);
            
            lightPos = new Vec3(0f,20f,0f);
            Mat4 lightProjection = glm.ortho(-10f, 10f, -10f, 10f, 0.1f, 100f);
            Mat4 lightView = glm.lookAt(lightPos, lightPos.minus(new Vec3(-1f, 0.1f, -1f).normalize()), new Vec3(0f,1f,0f));
            Mat4 lightSpaceMatrix = lightProjection.times(lightView);
            for(ECSEntity entity : entities) {
            	String shaderr = (String) entity.getField("shader");
                Shader shader = MemoryCache.getShader(shaderr);
                if(shader == null) continue;
                glUseProgram(shader.getProgram());
                shader.setUniform("view", camera.getView());
                shader.setUniform("viewPos", camera.getPos());
                shader.setUniform("projection", glm.perspective(glm.radians(45.0f), ((float)x) / y, 0.01f, 1000.0f));
                shader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
                shader.setUniform("shadowTex", 0);
                shader.setUniform("lightPos", lightPos);
                shader.setUniform("dirLight.dir", lightPos.times(1).normalize());
                glUseProgram(0);
            }
            
            for(int i = 0; i < entities.size(); i++) {
                renderStaticModel(entities.get(i), MemoryCache.getShader((String) entities.get(i).getField("shader")));
            }
        } else {
            prepareShadows();
            lightPos = new Vec3(0f,10f,-36f);
            glUseProgram(depthShader.program);
            Mat4 lightProjection = glm.ortho(-10f, 10f, -10f, 10f, 0.1f, 100f);
            Mat4 lightView = glm.lookAt(lightPos, lightPos.minus(new Vec3(0f, 1f, -2f).normalize()), new Vec3(0f,1f,0f));
            Mat4 lightSpaceMatrix = lightProjection.times(lightView);
            depthShader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
            depthShader.setUniform("view", camera.getView());
            glUseProgram(0);
            
            for(int i = 0; i < entities.size(); i++) {
                renderStaticModel(entities.get(i), MemoryCache.getShader((String) entities.get(i).getField("shader")));
            }
            prepare();
            for(ECSEntity entity : entities) {
            	String shaderr = (String) entity.getField("shader");
                Shader shader = MemoryCache.getShader(shaderr);
                if(shader == null) continue;
                glUseProgram(shader.getProgram());
                shader.setUniform("view", camera.getView());
                shader.setUniform("viewPos", camera.getPos());
                shader.setUniform("projection", glm.perspective(glm.radians(45.0f), ((float)x) / y, 0.01f, 1000.0f));
                shader.setUniform("lightSpaceMatrix", lightSpaceMatrix);
                shader.setUniform("shadowTex", 0);
                shader.setUniform("lightPos", lightPos);
                shader.setUniform("dirLight.dir", lightPos.times(1).normalize());
                glUseProgram(0);
            }
            
            for(int i = 0; i < entities.size(); i++) {
                renderStaticModel(entities.get(i), MemoryCache.getShader((String) entities.get(i).getField("shader")));
            }
            render();
        }
    }
    
    /**
     * Preps shadows
     */
    public static void prepareShadows() {
        glViewport(0, 0, shadowWidth, shadowHeight);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glEnable(GL_DEPTH_TEST);
        glClear(GL_DEPTH_BUFFER_BIT);
    }
    
    /**
     * Sets up Framebuffer stuff and for ACTUAL drawing
     */
    public static void prepare() {
        glViewport(0, 0, x, y);
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        
        glClearColor(0.005f,0.005f,0.005f,1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, depthMap);
    }
    
    /**
     * Adds shader to the BEGINNING of the list
     * @param shader 
     */
    public static void addPostShader(PostProcessingShader shader) {
        shaders.add(0, shader);
    }
    
    
    /**
     * Adds shader to i in the list
     * @param shader 
     * @param i
     */
    public static void addPostShader(PostProcessingShader shader, int i) {
        shaders.add(i, shader);
    }
    
    /**
     * Returns the shader at i
     * @param i
     * @return 
     */
    public static PostProcessingShader getPostShader(int i) {
        return shaders.get(i);
    }
    
    /**
     * Deletes the shader at i and shifts everything in front of it down one
     * @param i 
     */
    public static void deletePostShader(int i) {
        shaders.remove(i);
    }
    
    public static Camera getCamera() {
        return camera;
    }
    
    /**
     * Renders Framebuffer to the screen
     */
    public static void render() {
        copyData();
        for(int i = 0; i < shaders.size()-1; i++) {
            shaders.get(i).applyUniforms();
            renderToFramebuffer(shaders.get(i));
            copyData();
        }
        if(shaders.size() > 0) {
            shaders.get(shaders.size()-1).applyUniforms();
            renderToScreen(shaders.get(shaders.size()-1));
        }
        else System.out.println("Error: no PPS Shaders");
    }
    
    /**
     * copies data from fboDrawTexture to fboTexture
     */
    private static void copyData() {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTexture, 0);
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, fboDrawTexture, 0);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        glReadBuffer(GL_COLOR_ATTACHMENT1);
        glBlitFramebuffer(0, 0, x, y, 0, 0, x, y, GL_COLOR_BUFFER_BIT, GL_NEAREST);
        
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTexture, 0);
        glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, fboDrawTexture, 0);
        glDrawBuffer(GL_COLOR_ATTACHMENT1);
        glReadBuffer(GL_COLOR_ATTACHMENT0);
    }
    
    /**
     * Applys post-processing effect to Framebuffer
     * @param shader 
     */
    private static void renderToFramebuffer(PostProcessingShader shader) {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        
        glClearColor(1,1,1,1);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, fboTexture);
        glBindVertexArray(screenVAO);
        glUseProgram(shader.getShader().program);
        postRender();
    }
    
    /**
     * Applys the LAST post-processing affect and renders to the SCREEN
     * @param shader 
     */
    private static void renderToScreen(PostProcessingShader shader) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        glClearColor(1,1,1,1);
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, fboTexture);
        glBindVertexArray(screenVAO);
        glUseProgram(shader.getShader().program);
        postRender();
    }
    
    /**
     * Post-processing render code so i can reuse
     */
    private static void postRender() {
        glBindBuffer(GL_ARRAY_BUFFER, screenVBO);
        
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 8);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glEnable(GL_TEXTURE_2D);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisable(GL_TEXTURE_2D);
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
    glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glUseProgram(0);
        glBindVertexArray(0);
        
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    /**
     * Shut down OpenGL
     */
    public static void cleanup() {
        glDeleteVertexArrays(modelVAO);
        glDeleteFramebuffers(fbo);
    }
}

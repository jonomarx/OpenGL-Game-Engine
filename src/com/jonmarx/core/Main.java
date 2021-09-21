package com.jonmarx.core;

import com.jonmarx.discord.RichTextManager;
import com.jonmarx.discord.RichTextObject;
import com.jonmarx.game.CameraController;
import com.jonmarx.game.Crewmate;
import com.jonmarx.game.Game;
import com.jonmarx.game.Gun;
import com.jonmarx.geom.CubeMeshGenerator;
import com.jonmarx.gfx.GammaPostProcessingShader;
import com.jonmarx.gfx.KernelPostProcessingShader;
import com.jonmarx.util.CollisionChecker;
import com.jonmarx.util.BoundingBox2D;
import com.jonmarx.util.BoundingBox3D;
import com.jonmarx.util.BoundingBoxPlane;
import com.jonmarx.util.StripGenerator;
import glm_.mat4x4.Mat4;
import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Jon
 */
public class Main {
    private static Main instance;
    
    private GLFWErrorCallback errorCallback;
    private long window;
    private Shader lightShader;
    
    private volatile float deltaX;
    private volatile float deltaY;
    
    private Vec3 lightPos = new Vec3(8f, 20f, 8f);
    
    private boolean suspend = false;
    private boolean p = false;
    
    private int x = 640;
    private int y = 480;
    
    private Game game;
    
    public static void main(String[] args) {
        RichTextManager text = RichTextManager.get();
        RichTextObject obj = new RichTextObject();
        obj.details = "<None>";
        obj.name = "Game Thing";
        obj.state = "Startup State";
        obj.type = "type";
        obj.instance = "f";
        text.updateRichText(obj);
        System.out.println("Attempted to update rich text.");
        
        Main main = new Main();
        boolean forceScreenFBO = false;
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("-forceScreenFBO")) forceScreenFBO = true;
        }
        main.run(forceScreenFBO);
    }
    
    public Main() {
        if(instance != null) System.exit(1); // holup
        instance = this;
    }
    
    private void loadShaders() {
        lightShader = new Shader("/res/shaders/ContainerShader.vs", "/res/shaders/ContainerShader.fs");
        
        glUseProgram(lightShader.program);
        
        lightShader.setUniform("material.shininess", 64f);
        lightShader.setUniform("material.diffuse", 1);
        lightShader.setUniform("material.specular", 2);
        
        lightShader.setUniform("dirLight.dir", new Vec3(0.1f, 1f, 1f));
        lightShader.setUniform("dirLight.ambient", new Vec3(0.02f, 0.02f, 0.02f));
        lightShader.setUniform("dirLight.diffuse", new Vec3(0.02f, 0.02f, 0.02f));
        lightShader.setUniform("dirLight.specular", new Vec3(0.1f, 0.1f, 0.1f));
        
        lightShader.setUniform("pointLights[0].position", lightPos);
        lightShader.setUniform("pointLights[0].constant", 1.0f);
        lightShader.setUniform("pointLights[0].linear", 0.07f);
        lightShader.setUniform("pointLights[0].quadratic", 0.017f);
        lightShader.setUniform("pointLights[0].ambient", new Vec3(0.2f, 0.2f, 0.2f));
        lightShader.setUniform("pointLights[0].diffuse", new Vec3(0.4f, 0.4f, 0.4f));
        lightShader.setUniform("pointLights[0].specular", new Vec3(0.6f, 0.6f, 0.6f));
        lightShader.setUniform("pointLights[0].enabled", 1);
        glUseProgram(0);
    }
    
    public void run(boolean forceScreenFBO) {
        errorCallback = GLFWErrorCallback.createPrint(System.err).set();
        glfwInit();
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        window = glfwCreateWindow(x, y, "Hello, World!", NULL, NULL);
        
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPos(window, 640/2, 480/2);
        
        Renderer.init(x, y, forceScreenFBO);
        if(!forceScreenFBO) {
            //Shader kernelShader = new Shader("/res/shaders/kernelShader.vs", "/res/shaders/kernelShader.fs");
            Shader gammaShader = new Shader("/res/shaders/gammaShader.vs", "/res/shaders/gammaShader.fs");
            //Renderer.addPostShader(new KernelPostProcessingShader(kernelShader, new float[] {2,2,2,2,-16,2,2,2,2}));
            Renderer.addPostShader(new GammaPostProcessingShader(gammaShader, 2.2f));
        }
        loadShaders();
        
        //Renderer.addModel(MeshLoader.loadMesh("/res/models/dancing_vampire.dae", "/res/models/"));
        Renderer.addModel(MeshLoader.loadMesh("/res/models/gun.obj", "/res/models/"));
        Renderer.addModel(MeshLoader.loadMesh("/res/models/amongus.obj", "/res/models/", "Armature|Walk Cycle"));
        Renderer.addModel(MeshLoader.loadMesh("/res/models/bullet.obj", "/res/models"));
        Renderer.addModel(MeshLoader.loadMesh("/res/models/terrainTest.obj", "/res/models"));
        Renderer.addModel(MeshLoader.loadMesh("/res/models/area.obj", "/res/models"));
        
        //Renderer.addEntity(new SimpleEntity(new Mat4(), Renderer.getModel("/res/models/dancing_vampire.dae"), "lol"), lightShader);
        Renderer.addEntity(new Gun(90f, 0.1f, new Vec3(0f), Renderer.getModel("/res/models/gun.obj"), "gun"), lightShader);
        Renderer.addEntity(new Crewmate(90, new Vec3(0f,8f,0f), Renderer.getModel("/res/models/amongus.obj"), "amongus"), lightShader);
        //Renderer.addEntity(new SimpleEntity(new Mat4(), Renderer.getModel("/res/models/terrainTest.obj"), "terrain"), lightShader);
        Renderer.addEntity(new SimpleEntity(new Mat4(), Renderer.getModel("/res/models/area.obj"), "terrain"), lightShader);
        Renderer.addEntity(new CameraController("camera-controller"), null);
        game = new Game(Renderer.getEntity("terrain"));
        
        BoundingBox2D box1 = new BoundingBox2D(new Vec2(1,1),new Vec2(5,3),new Vec2(3,7),new Vec2(-1,5));
        BoundingBox2D box2 = new BoundingBox2D(new Vec2(0,0-6),new Vec2(0,4-6),new Vec2(2,4-6),new Vec2(2,0-6));
        System.out.println(box1.sweepBox(box2, new Vec2(0,-1)));
        //BoundingBox3D box1 = generateRectPrism(new Vec3(0,0,0), 5, 5, 5);
        //BoundingBox3D box2 = generateRectPrism(new Vec3(1,-1,1), 2, 2f, 2);
        //System.out.println(box1.testBox(box2) || box2.testBox(box1));
        
        // i literally copied this from stackoverflow lol
        // i literally copied this from c code lol
        double limitFPS = 1.0 / 60.0;

        double lastTime = glfwGetTime(), timer = lastTime;
        double deltaTime = 0, nowTime = 0;
        int frames = 0, updates = 0;
        while(!glfwWindowShouldClose(window)) {
            // - Measure time
            nowTime = glfwGetTime();
            deltaTime += (nowTime - lastTime) / limitFPS;
            lastTime = nowTime;

            // - Only update at 60 frames / s
            while (deltaTime >= 1.0){
                update();   // - Update function
                updates++;
                deltaTime--;
            }
            // - Render at maximum possible frames
            render(); // - Render function
            frames++;

            // - Reset after one second
            if (glfwGetTime() - timer > 1.0) {
                timer++;
                String text = "FPS: " + frames + " UPS: " + updates;
                glfwSetWindowTitle(window, text);
                updates = 0; frames = 0;
            }
        }
    
        cleanup();
    }
    
    public void render() {
        if(suspend) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Renderer.renderFromList();
        glfwSwapBuffers(window);
    }
    
    double prevX = 640/2;
    double prevY = 480/2;
    double rotation = 0;
    
    boolean shooting = false;
    
    int tick = 0;
    
    public void update() {
        RichTextManager text = RichTextManager.get();
        text.tick();
        glfwPollEvents();
        tick++;
        
        if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            if(!p) {
                p = true;
                suspend = !suspend;
                if(suspend) {
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else {
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    glfwSetCursorPos(window, 640/2, 480/2);
                }
            }
        } else {
            p = false;
        }
        
        if(tick % 900 == 0) {
            RichTextObject obj = new RichTextObject();
            Crewmate entity = (Crewmate) Renderer.getEntity("amongus");
            obj.details = "Position: " + entity.getPos();
            obj.name = "Jonathan's Game Thing";
            obj.state = suspend ? "Paused" : "Running";
            obj.type = "p";
            obj.instance = "f";
            text.updateRichText(obj);
        }
        
        if(suspend) return;
        
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window, x, y);
        
        deltaX = (float) (x[0] - prevX);
        deltaY = (float) -(y[0] - prevY);
        
        glfwSetCursorPos(window, 640/2, 480/2);
        
        rotation += 1;
        
        if(glfwGetKey(window, GLFW_KEY_M) == GLFW_PRESS) {
            shooting = true;
        }
        
        //Renderer.getEntity("lol").getModel().getAnimator().updateAnimation(1f/60f);
        Renderer.update();
    }
    
    public void cleanup() {
        Cleanup.cleanup();
        glfwTerminate();
    }
    
    /**
     * Axis aligned
     * @return 
     */
    private BoundingBox3D generateRectPrism(Vec3 origin, float x, float y, float z) {
        BoundingBoxPlane[] faces = new BoundingBoxPlane[6];
        
        faces[0] = new BoundingBoxPlane(origin.plus(0,0,0), origin.plus(x,0,0), origin.plus(x,y,0), origin.plus(0,y,0));
        faces[1] = new BoundingBoxPlane(origin.plus(x,0,0), origin.plus(x,y,0), origin.plus(x,y,z), origin.plus(x,0,z));
        faces[2] = new BoundingBoxPlane(origin.plus(x,0,z), origin.plus(x,y,z), origin.plus(0,y,z), origin.plus(0,0,z));
        faces[3] = new BoundingBoxPlane(origin.plus(0,0,z), origin.plus(0,y,z), origin.plus(0,y,0), origin.plus(0,0,0));
        faces[4] = new BoundingBoxPlane(origin.plus(0,y,0), origin.plus(0,y,z), origin.plus(x,y,z), origin.plus(x,y,0));
        faces[5] = new BoundingBoxPlane(origin.plus(0,0,0), origin.plus(0,0,z), origin.plus(x,0,z), origin.plus(x,0,0));
        
        return new BoundingBox3D(faces);
    }
    
    /**
     * Wrapper for glfwGetKey();
     * @param key
     * @return 
     */
    public int getKey(int key) {
        return glfwGetKey(window, key);
    }
    
    public Vec2 getMouseMovement() {
        return new Vec2(deltaX, deltaY);
    }
    
    public int getMouseClicked(int button) {
        return glfwGetMouseButton(window, button);
    }
    
    public Game getGame() {
        return game;
    }
    
    public static Main getInstance() {
        return instance;
    }
}

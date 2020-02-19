package main;

import engine.graphics.*;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector2f;
import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Main implements Runnable {
    public Thread game;
    public Window window;
    public Renderer renderer;
    public Shader shader;
    public final int WIDTH = 1280, HEIGHT = 800;

    public Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new Vector3f(-0.5f, 0.5f, 0), new Vector3f(1.0f, 0f, 0f), new Vector2f(0.0f,0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, 0), new Vector3f(0f,1.0f, 0f), new Vector2f(0.0f,1.0f)),
            new Vertex(new Vector3f(0.5f, -0.5f, 0), new Vector3f(0f, 0f, 1.0f), new Vector2f(1.0f,1.0f)),
            new Vertex(new Vector3f(0.5f, 0.5f, 0), new Vector3f(0.5f, 0.7f, 0f), new Vector2f(1.0f,0.0f))
    }, new int[] {
            0,1,2,
            0,3,2
    }, new Material("D:\\GameDev\\lwjgl_engine\\beautiful.png"));

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        System.out.println("Initializing Game!");
        window = new Window(WIDTH, HEIGHT, "Game");
        shader = new Shader("/shaders/Vertex.glsl", "/shaders/Fragment.glsl");
        renderer = new Renderer(shader);
        window.setBackgroundColor(1, 0, 0);
        window.create();
        mesh.create();
        shader.create();
    }

    public void run() {
        init();
        while(!window.close() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            update();
            render();
            if(Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.getFullscreen());
        }
        close();
    }

    private void update() {
        //System.out.println("Updating Game!");
        window.update();
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("X: " + Input.getMouseX() + " | Y: " + Input.getMouseY());
    }

    private void render() {
        //System.out.println("Rendering Game!");
        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    private void close() {
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}

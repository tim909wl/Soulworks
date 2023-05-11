package de.soulworks;

import de.soulworks.debug.Debug;
import de.soulworks.error.SoulworksError;
import de.soulworks.graphic.Shader;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

public class Soulworks implements Runnable {
    private boolean isRunning;
    private long window;
    private Shader shader;

    public void run() {
        init();

        while (isRunning) {
            loop();
        }

        cleanup();
    }

    private void init() {
        System.out.println("Soulworks Game Starting...");

        // Check LWJGL version
        System.out.println("LWJGL Version: " + Version.getVersion());

        // Debugging code goes here
        Debug.setItem("fps", 0);
        Debug.setItem("ups", 0);
        Debug.setItem("memoryUsage", 0);
        Debug.setItem("memoryReserved", 0);
        Debug.setItem("availableProcessors", 0);

        // Initialize GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Create window
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        window = GLFW.glfwCreateWindow(800, 600, "Soulworks Game", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities(); // Initialize OpenGL context
        GLFW.glfwSwapInterval(1); // Enable v-sync
        GLFW.glfwShowWindow(window);

        // Shader
        shader = new Shader();
        shader.createVertexShader("void main() {\n" +
                "    gl_Position = vec4(0.0, 0.0, 0.0, 1.0);\n" +
                "}");
        // make color red
        shader.createFragmentShader("void main() {\n" +
                "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                "}");
        shader.link();

        // Additional game initialization code goes here
        isRunning = true;
    }

    private void loop() {
        int targetFPS = 60;
        int targetUPS = 20;

        double targetFrameTime = 1.0 / targetFPS;
        double targetTickTime = 1.0 / targetUPS;

        double lastFrameTime = GLFW.glfwGetTime();
        double lastTickTime = lastFrameTime;
        double lastTime = lastFrameTime; // Initialize lastTime

        int frames = 0;
        int ticks = 0;

        double currentTime;
        double frameTime;
        double tickTime;

        while (isRunning) {
            currentTime = GLFW.glfwGetTime();
            frameTime = currentTime - lastFrameTime;
            tickTime = currentTime - lastTickTime;

            if (frameTime >= targetFrameTime) {
                // Render game graphics
                render();

                lastFrameTime += targetFrameTime; // Increment lastFrameTime by targetFrameTime
                frames++;
            }

            if (tickTime >= targetTickTime) {
                // Update game logic
                update();

                lastTickTime += targetTickTime; // Increment lastTickTime by targetTickTime
                ticks++;
            }

            // Poll for events
            GLFW.glfwPollEvents();

            // Check if the window should close
            if (GLFW.glfwWindowShouldClose(window)) {
                isRunning = false;
            }

            // Every second loop, update the debug items
            if (currentTime - lastTime >= 1.0) {
                // get memory usage
                long memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                long memoryReserved = Runtime.getRuntime().totalMemory();

                // get cpu usage
                long availableProcessors = Runtime.getRuntime().availableProcessors();

                // Update debug items directly instead of catching exception
                try {
                    Debug.updateItem("fps", frames);
                    Debug.updateItem("ups", ticks);
                    Debug.updateItem("memoryUsage", memoryUsage);
                    Debug.updateItem("memoryReserved", memoryReserved);
                    Debug.updateItem("availableProcessors", availableProcessors);
                } catch (SoulworksError e) {
                    throw new RuntimeException(e);
                }

                frames = 0;
                ticks = 0;
                lastTime += 1.0; // Increment lastTime by 1.0
            }

            sync(); // Synchronize the game loop
        }
    }

    private void update() {
        // Update game logic here
    }

    private void render() {
        GL20.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the framebuffer

        // Render game graphics here
        // ...
        // render a triangle
        shader.bind();
        GL20.glDrawArrays(GL20.GL_TRIANGLES, 0, 3);

        // Swap buffers
        GLFW.glfwSwapBuffers(window);
    }

    private void cleanup() {
        // Additional cleanup code goes here

        // Cleanup shader
        shader.cleanup();

        // Terminate GLFW
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        System.out.println("Soulworks Game Exiting...");
    }

    private void sync() {
        double loopSlot = 1.0 / 60.0; // Target 60 FPS
        double endTime = GLFW.glfwGetTime() + loopSlot;

        // Sleep the thread to synchronize with the target frame rate
        try {
            Thread.sleep((long) ((endTime - GLFW.glfwGetTime()) * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
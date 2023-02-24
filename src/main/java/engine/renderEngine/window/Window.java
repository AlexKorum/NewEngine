package engine.renderEngine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private long windowId;


    private long lastTime;
    private long time;

    public void create() {
        boolean initState = GLFW.glfwInit();

        if (!initState) {
            throw new IllegalStateException("Could not create GLFW!");
        }
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        windowId = GLFW.glfwCreateWindow(WindowSetting.getWidth(), WindowSetting.getHeight(), WindowSetting.getTitle(), 0, 0);
        if (windowId == MemoryUtil.NULL) {
            throw new IllegalStateException("Can't make window!");
        }
        WindowSetting.setWindowId(windowId);

        // Калбек на изменение размера окна (меняет область отображения на полный размер окна)
        GLFW.glfwSetWindowSizeCallback(windowId, (windowId, width, height) -> {
            GL11.glViewport(0, 0, width, height);
            WindowSetting.setWidth(width);
            WindowSetting.setHeight(height);
        });

        GLFW.glfwMakeContextCurrent(windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowId);
        GL.createCapabilities();
    }

    public void update() {
        time = System.nanoTime();
        GLFW.glfwSetWindowTitle(windowId, WindowSetting.getTitle() + " FPS: " + 1000000000.0f / (time - lastTime));
        lastTime = time;

        GLFW.glfwSwapBuffers(windowId);
        GLFW.glfwSwapInterval(1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void close() {
        GLFW.glfwDestroyWindow(windowId);
        GLFW.glfwTerminate();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowId);
    }

    public void setWindowTitle(String title) {
        GLFW.glfwSetWindowTitle(this.windowId, title);
    }
}

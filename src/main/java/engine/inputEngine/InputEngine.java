package engine.inputEngine;

import engine.renderEngine.window.WindowSetting;
import interfaces.EngineInterfaces;
import org.lwjgl.glfw.GLFW;
import scenes.Scene;
import scenes.entites.cameras.Camera;

public class InputEngine implements EngineInterfaces {

    private Camera camera;
    private float angleSpeed = 0.1f;
    private boolean flag = true;
    double[] mx;
    double[] my;

    @Override
    public void init() {
        camera = Scene.getInstance().getCamera();
        mx = new double[1];
        my = new double[1];
        GLFW.glfwSetCursorPos(WindowSetting.getWindowId(), WindowSetting.getWidth()/2, WindowSetting.getHeight()/2);
    }

    @Override
    public void update() {
        GLFW.glfwPollEvents();

        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.moveToForward();
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.moveToBack();
        }

        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.moveToRight();
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.moveToLeft();
        }

        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            camera.moveToUp();
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            camera.moveToDown();
        }

        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            camera.rotateHorizontal(angleSpeed);
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            camera.rotateHorizontal(-angleSpeed);
        }

        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            camera.rotateVertical(angleSpeed);
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            camera.rotateVertical(-angleSpeed);
        }


//        if (flag) {
//            GLFW.glfwGetCursorPos(WindowSetting.getWindowId(), mx, my);
//            mx[0] = mx[0] - WindowSetting.getWidth() / 2;
//            my[0] = WindowSetting.getHeight() / 2 - my[0];
//            camera.rotateHorizontal((float) (angleSpeed * mx[0]));
//            camera.rotateVertical((float) (angleSpeed * my[0]));
//            GLFW.glfwSetCursorPos(WindowSetting.getWindowId(), WindowSetting.getWidth()/2, WindowSetting.getHeight()/2);
//            GLFW.glfwSetInputMode(WindowSetting.getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
//        }else {
//            GLFW.glfwSetInputMode(WindowSetting.getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
//        }
//
//        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
//            flag = !flag;
//        }
    }
}

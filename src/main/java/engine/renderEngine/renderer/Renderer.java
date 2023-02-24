package engine.renderEngine.renderer;

import engine.renderEngine.fbo.FBO;
import engine.renderEngine.modeles.Model;
import engine.renderEngine.shader.ScreenShader;
import engine.renderEngine.shader.TextureShader;
import engine.renderEngine.window.WindowSetting;
import org.lwjgl.glfw.GLFW;
import scenes.Scene;
import scenes.SceneData;

import java.lang.management.ManagementFactory;

public class Renderer {
    private Model screen;
    private Model screenTexture;
    private ScreenShader screenShader;
    private TextureShader textureShader;
    private TextureShader[] textureShaders;

    private boolean flag;
    private FBO[][] fbos;
    private FBO[] fbo;

    private SceneData sceneData;

    public void init() {
        screen = new Model();
        screenTexture = new Model();
        sceneData = new SceneData();

        textureShaders = new TextureShader[]{
                new TextureShader(
                        "src\\main\\resources\\shaders\\RayCasting\\TextureVertexShader.glsl",
                        "src\\main\\resources\\shaders\\RayCasting\\TextureFragmentShader.glsl"
                ),
                new TextureShader(
                        "src\\main\\resources\\shaders\\RayMarching\\TextureVertexShader.glsl",
                        "src\\main\\resources\\shaders\\RayMarching\\TextureFragmentShader.glsl"
                ),
                new TextureShader(
                        "src\\main\\resources\\shaders\\RayTracing\\TextureVertexShader.glsl",
                        "src\\main\\resources\\shaders\\RayTracing\\TextureFragmentShader.glsl"
                )
        };

        screenShader = new ScreenShader(
                "src\\main\\resources\\shaders\\Screen\\ScreenVertexShader.glsl",
                "src\\main\\resources\\shaders\\Screen\\ScreenFragmentShader.glsl"
        );

        textureShader = textureShaders[0];

        fbos = new FBO[][]{
                new FBO[]{
                        new FBO(FBO.DEPTH_RENDER_BUFFER),
                        new FBO(FBO.DEPTH_RENDER_BUFFER)
                },
                new FBO[]{
                        new FBO(FBO.DEPTH_RENDER_BUFFER),
                        new FBO(FBO.DEPTH_RENDER_BUFFER)
                },
                new FBO[]{
                        new FBO(FBO.DEPTH_RENDER_BUFFER),
                        new FBO(FBO.DEPTH_RENDER_BUFFER)
                }
        };
        fbo = fbos[0];

        flag = true;
    }

    public void update() {
        sceneData.update();
        Scene.getInstance().getCamera().addOneSamplePart();
        this.setCurrentShadersAndFBO();

        // Свап текстур
        if (flag) {
            screenTexture.setTextureId(fbo[1].getColorTexture());
            fbo[0].bind();
            textureShader.start();
            this.loadUniforms();
            screenTexture.draw();
            textureShader.stop();
            fbo[0].unbind();
            screen.setTextureId(fbo[0].getColorTexture());
        } else {
            screenTexture.setTextureId(fbo[0].getColorTexture());
            fbo[1].bind();
            textureShader.start();
            this.loadUniforms();
            screenTexture.draw();
            textureShader.stop();
            fbo[1].unbind();
            screen.setTextureId(fbo[1].getColorTexture());
        }
        flag = !flag;

        // Рендер на экран
        screenShader.start();
        screen.draw();
        screenShader.stop();
    }

    public void close() {
        textureShader.cleanUp();
        screenShader.cleanUp();

        for (FBO fbo : fbo) {
            fbo.cleanUp();
        }

        screen.cleanUp();
        screenTexture.cleanUp();

    }

    private void loadUniforms() {
        textureShader.loadBackgroundColor(sceneData.getBackgroundColor());
        textureShader.loadResolution(WindowSetting.getWidth(), WindowSetting.getHeight());

        textureShader.loadCameraPosition(sceneData.getCameraPosition());
        textureShader.loadCameraDirection(sceneData.getCameraDirection());
        textureShader.loadCountObject(sceneData.getCountObjects());
        textureShader.loadTags(sceneData.getTags());
        textureShader.loadPositions(sceneData.getPositions());
        textureShader.loadRotations(sceneData.getRotations());
        textureShader.loadScales(sceneData.getScales());
        textureShader.loadColors(sceneData.getColors());
        textureShader.loadRoughness(sceneData.getRoughness());
        textureShader.loadGlowing(sceneData.getGlowing());

        textureShader.loadTime(ManagementFactory.getRuntimeMXBean().getUptime());
        textureShader.loadLightPosition(sceneData.getLightPosition());

        textureShader.loadSamplePart(Scene.getInstance().getCamera().getSamplePart());

        textureShader.loadSeed();
    }

    private void setCurrentShadersAndFBO() {
        if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS) {
            textureShader = textureShaders[0];
            fbo = fbos[0];
            flag = true;
            Scene.getInstance().getCamera().resetSamplePart();
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS) {
            textureShader = textureShaders[1];
            fbo = fbos[1];
            flag = true;
            Scene.getInstance().getCamera().resetSamplePart();
        } else if (GLFW.glfwGetKey(WindowSetting.getWindowId(), GLFW.GLFW_KEY_3) == GLFW.GLFW_PRESS) {
            textureShader = textureShaders[2];
            fbo = fbos[2];
            flag = true;
            Scene.getInstance().getCamera().resetSamplePart();
        }
    }
}

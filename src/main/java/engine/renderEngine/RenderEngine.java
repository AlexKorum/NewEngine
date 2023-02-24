package engine.renderEngine;

import engine.renderEngine.renderer.Renderer;
import engine.renderEngine.window.Window;
import interfaces.EngineInterfaces;

public class RenderEngine implements EngineInterfaces {
    private Renderer renderer;
    private Window window;
    @Override
    public void init() {
        window = new Window();
        window.create();

        renderer = new Renderer();
        renderer.init();
    }

    @Override
    public void update() {
        renderer.update();
        window.update();
    }

    public boolean shouldCloseEngine() {
        return window.shouldClose();
    }

    public void closeEngine() {
        renderer.close();
        window.close();
    }
}

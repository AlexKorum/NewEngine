package engine;


import engine.inputEngine.InputEngine;
import engine.physicsEngine.PhysicsEngine;
import engine.renderEngine.RenderEngine;
import scenes.Scene;
import scenes.entites.primitives.Cube;
import scenes.entites.primitives.Ellipse;
import scenes.entites.primitives.Plane;

public class Engine {
    private InputEngine inputEngine;
    private PhysicsEngine physicsEngine;
    private RenderEngine renderEngine;

    public Engine() {
        inputEngine = new InputEngine();
        physicsEngine = new PhysicsEngine();
        renderEngine = new RenderEngine();
    }

    public void run() {
        this.init();
        this.update();
    }

    private void init() {
        // Запускаем движок графики
        renderEngine.init();

        // Запускаем движок физики
        physicsEngine.init();

        // Запускаем движок ввода
        inputEngine.init();
    }

    private void update() {
        while (!renderEngine.shouldCloseEngine()) {
            inputEngine.update();
            physicsEngine.update();
            renderEngine.update();
        }
        renderEngine.closeEngine();
    }
}

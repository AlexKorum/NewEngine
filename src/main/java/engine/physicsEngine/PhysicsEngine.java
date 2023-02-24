package engine.physicsEngine;


import interfaces.EngineInterfaces;
import scenes.Scene;
import scenes.entites.primitives.Cube;
import scenes.entites.primitives.Ellipse;
import utilities.Vector3f;

import javax.swing.*;

public class PhysicsEngine implements EngineInterfaces {
    private Scene scene;

    @Override
    public void init() {
        scene = Scene.getInstance();

//        plane();
        cornellBox();
    }

    @Override
    public void update() {
//        scene.getObject("rotation").getTransform().addRotationY(0.05f);
//        scene.getObject("scale").getTransform().addRotationY(-0.05f);
    }

    private void plane() {
        scene.getCamera().setPosition(0, 5, -10);
        scene.getCamera().setDirection(0, -5, 10);

        Cube ground = new Cube("Box");
        ground.getTransform().setScale(10, 1, 10);
        ground.getMaterial().setColor(0.5f, 0.5f, 0.5f);
        scene.addObject(ground);

        Ellipse rotation = new Ellipse("rotation");
        rotation.getTransform().setPosition(-2,2,-5);
        rotation.getTransform().setRotation(0,(float) Math.PI/8, 0);
        rotation.getTransform().setScale(2,1,1);
        rotation.getMaterial().setColor(0.9f, 0.5f, 0.5f);
        scene.addObject(rotation);

        Cube scale = new Cube("scale");
        scale.getTransform().setPosition(2,2,-5);
//        scale.getTransform().setScale(0.5f, 2, 4);
        scale.getMaterial().setColor(0.9f, 0.1f, 0.7f);
        scene.addObject(scale);
    }

    private void cornellBox() {

        Ellipse ellipse0 = new Ellipse("Ellipse");
        ellipse0.getTransform().setPosition(-1, 2.5f, 0);
        ellipse0.getTransform().setScale(1.5f, 1.5f, 1.5f);
        ellipse0.getMaterial().setColor(0.5f, 0.5f, 0.5f);
        ellipse0.getMaterial().setRoughness(0.1f);
        scene.addObject(ellipse0);

        Cube cube = new Cube("Cube");
        cube.getTransform().setPosition(2, 2, 2);
        cube.getTransform().setScale(1, 2, 1);
        cube.getMaterial().setColor(0.9f, 0.1f, 0.5f);
        cube.getMaterial().setRoughness(0.5f);
        scene.addObject(cube);

        Ellipse ellipse1 = new Ellipse("Ellipse");
        ellipse1.getTransform().setPosition(2, 5, 2);
        ellipse1.getMaterial().setColor(0.1f, 0.9f, 0.5f);
        ellipse1.getMaterial().setRoughness(0.9f);
        scene.addObject(ellipse1);

        // ------------------------------------------------------------------------------------------------------------
        // Создание коробки
        float roughness = 0.8f;
        Vector3f colorWall = new Vector3f(0.9f, 0.9f, 0.9f);

        // Пол и потолок
        Cube ground = new Cube("Ground");
        ground.getTransform().setPosition(0, 0, 0);
        ground.getTransform().setScale(5, 1, 5);
        ground.getMaterial().setColor(colorWall);
        ground.getMaterial().setRoughness(roughness);
        scene.addObject(ground);

        Cube ceiling = new Cube("ceiling");
        ceiling.getTransform().setPosition(0, 10, 0);
        ceiling.getTransform().setScale(5, 1, 5);
        ceiling.getMaterial().setColor(colorWall);
        ceiling.getMaterial().setRoughness(roughness);
        scene.addObject(ceiling);

        // Боковые стены
        Cube frontWall = new Cube("frontWall");
        frontWall.getTransform().setPosition(0, 5, 5);
        frontWall.getTransform().setScale(5, 5, 1);
        frontWall.getMaterial().setColor(colorWall);
        frontWall.getMaterial().setRoughness(roughness);
        scene.addObject(frontWall);

        Cube backWall = new Cube("backWall");
        backWall.getTransform().setPosition(0, 5, -5);
        backWall.getTransform().setScale(5, 5, 1);
        backWall.getMaterial().setColor(colorWall);
        backWall.getMaterial().setRoughness(roughness);
        scene.addObject(backWall);

        Cube redWall = new Cube("redWall");
        redWall.getTransform().setPosition(-5, 5, 0);
        redWall.getTransform().setScale(1, 5, 5);
        redWall.getMaterial().setColor(1, 0, 0);
        redWall.getMaterial().setRoughness(roughness);
        scene.addObject(redWall);

        Cube greenWall = new Cube("blueWall");
        greenWall.getTransform().setPosition(5, 5, 0);
        greenWall.getTransform().setScale(1, 5, 5);
        greenWall.getMaterial().setColor(0, 1, 0);
        greenWall.getMaterial().setRoughness(roughness);
        scene.addObject(greenWall);


        // Источник света
        Cube light = new Cube("light");
        light.getTransform().setPosition(0, 9f, 0);
        light.getTransform().setScale(1, 0.125f, 1);
        light.getMaterial().setColor(1, 1, 1);
        light.setLight(true);
        scene.addObject(light);
        scene.getLightPosition().set(0, 8f, 0);

    }
}

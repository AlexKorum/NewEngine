package scenes;


import scenes.entites.cameras.Camera;
import scenes.entites.primitives.Object;
import utilities.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
    private static Scene instance;

    public static Scene getInstance() {
        if (instance == null) {
            instance = new Scene();
        }
        return instance;
    }

    public static final int MAX_OBJECTS = 64;

    private Vector3f backgroundColor;

    private Camera camera;
    private Vector3f lightPosition;
    private Map<String, Object> objects;

    public Scene() {
        backgroundColor = new Vector3f(77, 143, 223).divisionOnScalarReturn(255);
        objects = new HashMap<>();
        camera = new Camera();
        lightPosition = new Vector3f(10, 30, -20);
    }

    // Добавление и удаление объектов
    public void addObject(Object object) {
        if (objects.size() >= MAX_OBJECTS) {
            System.out.println("Максимальное количество объектов!");
            return;
        }

        if (!objects.containsKey(object.getName())) {
            objects.put(object.getName(), object);
        } else {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (!objects.containsKey(object.getName() + i)) {
                    object.setName(object.getName() + i);
                    objects.put(object.getName(), object);
                    return;
                }
            }
            System.out.println("Количество одинаковых имен превышено!");
        }
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    public void removeObject(Object object) {
        for (Object o : objects.values()) {
            if (object.equals(o)) {
                objects.remove(o.getName());
                return;
            }
        }
    }

    // Получение данных об объектах
    public int getMaxObjects() {
        return MAX_OBJECTS;
    }

    public int getCountObjects() {
        return objects.size();
    }

    public Camera getCamera() {
        return camera;
    }

    public Vector3f getLightPosition() {
        return lightPosition;
    }

    public Object getObject(String name) {
        return objects.get(name);
    }

    public List<Object> getObjects() {
        return objects.values().stream().toList();
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    // Выставление данных
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setLightPosition(Vector3f src) {
        this.lightPosition.set(src);
    }

    public void setLightPosition(float x, float y, float z) {
        this.lightPosition.set(x, y, z);
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    public void setBackgroundColor(float r, float g, float b) {
        this.backgroundColor.set(r, g, b);
    }
}

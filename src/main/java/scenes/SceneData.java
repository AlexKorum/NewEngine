package scenes;

import scenes.entites.primitives.Object;
import utilities.Vector3f;

import java.util.List;

public class SceneData {
    private Scene scene;
    private float[] tags;
    private float[] position;
    private float[] rotation;
    private float[] scales;
    private float[] colors;
    private float[] roughness;
    private float[] glowing;

    public SceneData() {
        scene = Scene.getInstance();
        int maxObjects = scene.getMaxObjects();
        tags = new float[maxObjects];
        position = new float[maxObjects * 3];
        rotation = new float[maxObjects * 3];
        scales = new float[maxObjects * 3];
        colors = new float[maxObjects * 3];
        roughness = new float[maxObjects];
        glowing = new float[maxObjects];
    }

    public void update() {
        Object object;
        List<Object> objects = scene.getObjects();
        for (int i = 0; i < scene.getCountObjects(); i++) {
            object = objects.get(i);

            // Сохранить тег
            tags[i] = object.getTag();

            // Сохранить позицию
            position[3 * i] = object.getTransform().getPositionX();
            position[3 * i + 1] = object.getTransform().getPositionY();
            position[3 * i + 2] = object.getTransform().getPositionZ();

            // Сохранить поворот
            rotation[3 * i] = object.getTransform().getRotationX();
            rotation[3 * i + 1] = object.getTransform().getRotationY();
            rotation[3 * i + 2] = object.getTransform().getRotationZ();

            // Сохранить маштаб
            scales[3 * i] = object.getTransform().getScaleX();
            scales[3 * i + 1] = object.getTransform().getScaleY();
            scales[3 * i + 2] = object.getTransform().getScaleZ();

            // Сохранить цвет
            colors[3 * i] = object.getMaterial().getColorR();
            colors[3 * i + 1] = object.getMaterial().getColorG();
            colors[3 * i + 2] = object.getMaterial().getColorB();

            // Сохранить коэффициент шероховтости
            roughness[i] = object.getMaterial().getRoughness();

            // Сохранить светится ли объект
            glowing[i] = object.isLight() ? 1 : 0;
        }
    }

    public int getCountObjects() {
        return scene.getCountObjects();
    }

    public float[] getTags() {
        return tags;
    }

    public float[] getPositions() {
        return position;
    }

    public float[] getRotations() {
        return rotation;
    }

    public float[] getScales() {
        return scales;
    }

    public float[] getColors() {
        return colors;
    }

    public float[] getRoughness() {
        return roughness;
    }

    public float[] getGlowing() {
        return glowing;
    }

    public Vector3f getCameraPosition() {
        return scene.getCamera().getPosition();
    }

    public Vector3f getCameraDirection() {
        return scene.getCamera().getDirection();
    }

    public Vector3f getLightPosition() {
        return scene.getLightPosition();
    }

    public Vector3f getBackgroundColor() {
        return scene.getBackgroundColor();
    }
}

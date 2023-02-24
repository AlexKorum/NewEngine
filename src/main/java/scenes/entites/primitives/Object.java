package scenes.entites.primitives;

import scenes.entites.components.Material;
import scenes.entites.components.Transform;

public abstract class Object {
    private float tag;
    private String name;

    protected Transform transform;
    protected Material material;

    protected boolean isLight;

    public Object(float tag, String name) {
        this.tag = tag;
        this.name = name;
        isLight = false;
    }

    public float getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Transform getTransform() {
        return transform;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean flag) {
        this.isLight = flag;
    }
}

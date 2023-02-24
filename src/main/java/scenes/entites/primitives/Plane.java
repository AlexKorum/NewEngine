package scenes.entites.primitives;

import scenes.entites.components.Material;
import scenes.entites.components.Transform;

public class Plane extends Object{
    public Plane(String name) {
        super(2, name);
        this.transform = new Transform();
        this.material = new Material();
    }
}

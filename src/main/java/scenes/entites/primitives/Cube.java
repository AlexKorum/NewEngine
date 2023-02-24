package scenes.entites.primitives;

import scenes.entites.components.Material;
import scenes.entites.components.Transform;

public class Cube extends Object{
    public Cube(String name) {
        super(1, name);
        this.transform = new Transform();
        this.material = new Material();
    }
}

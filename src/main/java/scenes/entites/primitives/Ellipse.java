package scenes.entites.primitives;

import scenes.entites.components.Material;
import scenes.entites.components.Transform;

public class Ellipse extends Object {
    public Ellipse(String name) {
        super(0, name);
        this.transform = new Transform();
        this.material = new Material();
    }
}

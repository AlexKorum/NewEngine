package scenes.entites.primitives;

import scenes.entites.components.Material;
import scenes.entites.components.Transform;

public class Fractal extends Object {
    public Fractal(String name) {
        super(3, name);
        this.transform = new Transform();
        this.material = new Material();
    }
}

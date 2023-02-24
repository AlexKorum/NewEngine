package scenes.entites.components;

import utilities.Vector3f;

public class Material extends Component {
    private Vector3f color;
    private float roughness;

    public Material() {
        super(1, "Material");
        color = new Vector3f(0);
        roughness = 0;
    }

    // Шероховатость
    // ----------------------------------------------------------------------------------------------------------------
    public float getRoughness() {
        return roughness;
    }

    public void setRoughness(float roughness) {
        this.roughness = roughness;
    }
    // ----------------------------------------------------------------------------------------------------------------


    // Получение цвета
    // ----------------------------------------------------------------------------------------------------------------

    // Get
    public Vector3f getColor() {
        return color;
    }

    public float getColorR() {
        return color.getX();
    }

    public float getColorG() {
        return color.getY();
    }

    public float getColorB() {
        return color.getZ();
    }

    // Set
    public void setColor(Vector3f src) {
        color.set(src);
    }

    public void setColor(float r, float g, float b) {
        color.set(r, g, b);
    }

    public void setColorR(float r) {
        color.setX(r);
    }

    public void setColorG(float g) {
        color.setY(g);
    }

    public void setColorB(float b) {
        color.setZ(b);
    }

    // Add
    public void addColor(Vector3f src) {
        color.add(src);
    }

    public void addColor(float dr, float dg, float db) {
        color.add(dr, dg, db);
    }

    public void addColorR(float dr) {
        color.add(dr, 0, 0);
    }

    public void addColorG(float dg) {
        color.add(0, dg, 0);
    }

    public void addColorB(float db) {
        color.add(0, 0, db);
    }

    // ----------------------------------------------------------------------------------------------------------------
}

package scenes.entites.components;

import utilities.Vector3f;

public class Transform extends Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
        super(0, "Transform");
        position = new Vector3f(0);
        rotation = new Vector3f(0);
        scale = new Vector3f(1);
    }

    // Получение и изменение позиций
    // ----------------------------------------------------------------------------------------------------------------

    // Get
    public Vector3f getPosition() {
        return position;
    }

    public float getPositionX() {
        return position.getX();
    }

    public float getPositionY() {
        return position.getY();
    }

    public float getPositionZ() {
        return position.getZ();
    }

    // Set
    public void setPosition(Vector3f src) {
        position.set(src);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setPositionX(float x) {
        position.setX(x);
    }

    public void setPositionY(float y) {
        position.setY(y);
    }

    public void setPositionZ(float z) {
        position.setZ(z);
    }

    // Add
    public void addPosition(Vector3f src) {
        position.add(src);
    }

    public void addPosition(float dx, float dy, float dz) {
        position.add(dx, dy, dz);
    }

    public void addPositionX(float dx) {
        position.add(dx, 0, 0);
    }

    public void addPositionY(float dy) {
        position.add(0, dy, 0);
    }

    public void addPositionZ(float dz) {
        position.add(0, 0, dz);
    }

    // ----------------------------------------------------------------------------------------------------------------



    // Получение и изменение поворота
    // ----------------------------------------------------------------------------------------------------------------

    // Get
    public Vector3f getRotation() {
        return rotation;
    }

    public float getRotationX() {
        return rotation.getX();
    }

    public float getRotationY() {
        return rotation.getY();
    }

    public float getRotationZ() {
        return rotation.getZ();
    }

    // Set
    public void setRotation(Vector3f src) {
        rotation.set(src);
    }

    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
    }

    public void setRotationX(float x) {
        rotation.setX(x);
    }

    public void setRotationY(float y) {
        rotation.setY(y);
    }

    public void setRotationZ(float z) {
        rotation.setZ(z);
    }

    // Add
    public void addRotation(Vector3f src) {
        rotation.add(src);
    }

    public void addRotation(float dx, float dy, float dz) {
        rotation.add(dx, dy, dz);
    }

    public void addRotationX(float dx) {
        rotation.add(dx, 0, 0);
    }

    public void addRotationY(float dy) {
        rotation.add(0, dy, 0);
    }

    public void addRotationZ(float dz) {
        rotation.add(0, 0, dz);
    }

    // ----------------------------------------------------------------------------------------------------------------



    // Получение и изменение маштаба
    // ----------------------------------------------------------------------------------------------------------------

    // Get
    public Vector3f getScale() {
        return scale;
    }

    public float getScaleX() {
        return scale.getX();
    }

    public float getScaleY() {
        return scale.getY();
    }

    public float getScaleZ() {
        return scale.getZ();
    }

    // Set
    public void setScale(Vector3f src) {
        scale.set(src);
    }

    public void setScale(float x, float y, float z) {
        scale.set(x, y, z);
    }

    public void setScaleX(float x) {
        scale.setX(x);
    }

    public void setScaleY(float y) {
        scale.setY(y);
    }

    public void setScaleZ(float z) {
        scale.setZ(z);
    }

    // Add
    public void addScale(Vector3f src) {
        scale.add(src);
    }

    public void addScale(float dx, float dy, float dz) {
        scale.add(dx, dy, dz);
    }

    public void addScaleX(float dx) {
        scale.add(dx, 0, 0);
    }

    public void addScaleY(float dy) {
        scale.add(0, dy, 0);
    }

    public void addScaleZ(float dz) {
        scale.add(0, 0, dz);
    }

    // ----------------------------------------------------------------------------------------------------------------
}

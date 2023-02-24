package scenes.entites.cameras;

import scenes.entites.components.Transform;
import utilities.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f direction;
    private Vector3f right;

    private float speed;

    private float samplePart;

    public Camera() {
        position = new Transform().getPosition();
        position.set(0, 5, -5);
        direction = new Vector3f(0, 0, 1);
        right = direction.vectorMultiplyOnVectorReturn(0, 1, 0);
        speed = 0.5f;
        samplePart = 1;
    }

    // Set
    // ----------------------------------------------------------------------------------------------------------------
    // Установка скорости
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // Установка позиций
    public void setPosition(Vector3f src) {
        position.set(src);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    // Установка точки направления
    public void setDirection(Vector3f src) {
        direction.set(src);
        direction.normalize();
    }

    public void setDirection(float x, float y, float z) {
        direction.set(x, y, z);
        direction.normalize();
    }
    // ----------------------------------------------------------------------------------------------------------------

    // Get
    // ----------------------------------------------------------------------------------------------------------------

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSamplePart() {
        return samplePart;
    }

    // ----------------------------------------------------------------------------------------------------------------


    // Add
    // ----------------------------------------------------------------------------------------------------------------

    public void addOneSamplePart() {
        samplePart++;
    }

    // ----------------------------------------------------------------------------------------------------------------


    // Move
    // ----------------------------------------------------------------------------------------------------------------
    public void moveToForward() {
        direction.multiplyOnScalar(speed);
        position.add(direction);
        direction.normalize();
        samplePart = 1;
    }

    public void moveToBack() {
        direction.multiplyOnScalar(-speed);
        position.add(direction);
        direction.multiplyOnScalar(-1);
        direction.normalize();
        samplePart = 1;
    }

    public void moveToRight() {
        right.set(0, 1, 0);
        right.vectorMultiplyOnVector(direction.getX(), 0, direction.getZ());
        right.normalize();
        right.multiplyOnScalar(speed);
        position.add(right);
        samplePart = 1;
    }

    public void moveToLeft() {
        right.set(0, 1, 0);
        right.vectorMultiplyOnVector(direction.getX(), 0, direction.getZ());
        right.normalize();
        right.multiplyOnScalar(-speed);
        position.add(right);
        samplePart = 1;
    }

    public void moveToUp() {
        position.add(0, speed, 0);
        samplePart = 1;
    }

    public void moveToDown() {
        position.add(0, -speed, 0);
        samplePart = 1;
    }
    // ----------------------------------------------------------------------------------------------------------------


    // Rotation
    // ----------------------------------------------------------------------------------------------------------------
    public void rotateHorizontal(float angle) {
        float x = (float) (direction.getX() * Math.cos(angle) + direction.getZ() * Math.sin(angle));
        float z = (float) (-direction.getX() * Math.sin(angle) + direction.getZ() * Math.cos(angle));

        direction.set(x, direction.getY(), z);
        direction.normalize();
        samplePart = 1;
    }

    public void rotateVertical(float angle) {
        float xz = (float) Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ());
        float y = (float) (xz * Math.sin(angle) + direction.getY() * Math.cos(angle));
        if (Math.abs(y) > 0.95) {
            return;
        }
        direction.set(direction.getX(), y, direction.getZ());
        direction.normalize();
        samplePart = 1;
    }
    // ----------------------------------------------------------------------------------------------------------------

    public void resetSamplePart(){
        samplePart = 1;
    }
}

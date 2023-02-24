package engine.renderEngine.shader;

import utilities.Vector3f;

import java.util.Random;

public class TextureShader extends Shader {

    // Цвет щаднего фона
    // ----------------------------------------------------------------------------------------------------------------

    private int backgroundColorLocation;

    // ----------------------------------------------------------------------------------------------------------------


    // Размер экрана
    // ----------------------------------------------------------------------------------------------------------------

    private int resolutionLocation;

    // ----------------------------------------------------------------------------------------------------------------


    // Информация об объекте
    // ----------------------------------------------------------------------------------------------------------------

    // Количество объектов на сцене
    private int countObjectLocation;

    // Список тэгов
    private int tagsLocation;

    // Список позиций объектов
    private int positionsLocation;

    // Список поворотов объектов
    private int rotationsLocation;

    // Список маштаба объектов
    private int scalesLocation;

    // Список цветов объектов
    private int colorsLocation;

    // Список коэффицентов шероховатости
    private int roughnessLocation;

    // Список информаций об светящихся объектов
    private int glowingLocation;

    private int seed1Location;
    private int seed2Location;

    // ----------------------------------------------------------------------------------------------------------------


    // Информация о камере
    // ----------------------------------------------------------------------------------------------------------------

    // Позиция камеры
    private int cameraPositionLocation;

    // Направление камеры
    private int cameraDirectionLocation;

    private int samplePartLocation;

    // ----------------------------------------------------------------------------------------------------------------


    // Дополнительная информация
    // ----------------------------------------------------------------------------------------------------------------

    // Информация о времени с момента запуска программы
    private int timeLocation;

    // Случайное число для рандома по RayTracing
    private int randomSeedLocation;

    // ----------------------------------------------------------------------------------------------------------------


    // Позиция света
    // ----------------------------------------------------------------------------------------------------------------

    private int lightPositionLocation;

    // ----------------------------------------------------------------------------------------------------------------

    // Рандомы
    // ----------------------------------------------------------------------------------------------------------------
    Random random;
    public TextureShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
        random = new Random();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocation() {
        backgroundColorLocation = super.getUniformLocation("u_background");

        resolutionLocation = super.getUniformLocation("u_resolution");

        cameraPositionLocation = super.getUniformLocation("u_camera_pos");
        cameraDirectionLocation = super.getUniformLocation("u_camera_dir");

        countObjectLocation = super.getUniformLocation("u_count_object");
        tagsLocation = super.getUniformLocation("u_tags");
        positionsLocation = super.getUniformLocation("u_positions");
        roughnessLocation = super.getUniformLocation("u_rotations");
        scalesLocation = super.getUniformLocation("u_scales");
        colorsLocation = super.getUniformLocation("u_colors");
        roughnessLocation = super.getUniformLocation("u_roughness");
        glowingLocation = super.getUniformLocation("u_glowing");

        timeLocation = super.getUniformLocation("u_time");

        lightPositionLocation = super.getUniformLocation("u_light_pos");

        samplePartLocation = super.getUniformLocation("u_sample_part");

        seed1Location = super.getUniformLocation("u_seed1");
        seed2Location = super.getUniformLocation("u_seed2");

    }

    public void loadBackgroundColor(float r, float g, float b) {
        super.loadVector3f(backgroundColorLocation, r, g, b);
    }

    public void loadBackgroundColor(Vector3f src) {
        super.loadVector3f(backgroundColorLocation, src);
    }

    public void loadResolution(float width, float height) {
        super.loadVector2f(resolutionLocation, width, height);
    }

    public void loadCountObject(int countObject) {
        super.loadFloat(countObjectLocation, countObject);
    }

    public void loadTags(float[] tags) {
        super.loadArrayFloats(tagsLocation, tags);
    }

    public void loadPositions(float[] positions) {
        super.loadArrayFloats(positionsLocation, positions);
    }

    public void loadRotations(float[] rotations) {
        super.loadArrayFloats(rotationsLocation, rotations);
    }

    public void loadScales(float[] scales) {
        super.loadArrayFloats(scalesLocation, scales);
    }

    public void loadColors(float[] colors) {
        super.loadArrayFloats(colorsLocation, colors);
    }

    public void loadRoughness(float[] roughness) {
        super.loadArrayFloats(roughnessLocation, roughness);
    }

    public void loadGlowing(float[] glowing) {
        super.loadArrayFloats(glowingLocation, glowing);
    }

    public void loadCameraPosition(Vector3f cameraPosition) {
        super.loadVector3f(cameraPositionLocation, cameraPosition);
    }

    public void loadCameraDirection(Vector3f cameraDirection) {
        super.loadVector3f(cameraDirectionLocation, cameraDirection);
    }

    public void loadTime(long time) {
        super.loadFloat(timeLocation, time);
    }

    public void loadLightPosition(Vector3f lightPosition) {
        super.loadVector3f(lightPositionLocation, lightPosition);
    }

    public void loadSamplePart(float samplePart) {
        super.loadFloat(samplePartLocation, samplePart);
    }

    public void loadSeed(){
        random.setSeed(System.nanoTime());
        super.loadVector2f(seed1Location, random.nextFloat(), random.nextFloat()*999.0f);
        random.setSeed(System.nanoTime()*999);
        super.loadVector2f(seed2Location, random.nextFloat(), random.nextFloat()*999.0f);
    }
}

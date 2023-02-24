package engine.renderEngine.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import scenes.Scene;
import utilities.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Shader {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public Shader(String vertexFile, String fragmentFile) {
        this.vertexShaderId = loadShader(vertexFile, GL30.GL_VERTEX_SHADER);
        this.fragmentShaderId = loadShader(fragmentFile, GL30.GL_FRAGMENT_SHADER);


        this.programId = GL30.glCreateProgram();
        GL30.glAttachShader(this.programId, this.vertexShaderId);
        GL30.glAttachShader(this.programId, this.fragmentShaderId);
        bindAttributes();
        GL30.glLinkProgram(this.programId);
        GL30.glValidateProgram(this.programId);
        getAllUniformLocation();
    }

    public void start() {
        GL20.glUseProgram(this.programId);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(this.programId, this.vertexShaderId);
        GL20.glDetachShader(this.programId, this.fragmentShaderId);
        GL20.glDeleteShader(this.vertexShaderId);
        GL20.glDeleteShader(this.fragmentShaderId);
        GL20.glDeleteProgram(this.programId);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(this.programId, attribute, variableName);
    }

    protected abstract void getAllUniformLocation();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadVector2f(int location, float x, float y) {
        GL20.glUniform2f(location, x, y);
    }

    protected void loadVector3f(int location, float x, float y, float z) {
        GL20.glUniform3f(location, x, y, z);
    }

    protected void loadVector3f(int location, Vector3f vector3f) {
        GL20.glUniform3f(location, vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    protected void loadBoolean(int location, boolean bool) {
        float toLoad = 0;
        if (bool) {
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    protected void loadArrayFloats(int location, float[] floats) {
        GL30.glUniform1fv(location, floats);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("#include hg_sdf.glsl")) {
                    BufferedReader readLinkFile = new BufferedReader(new FileReader("src\\main\\resources\\shaders\\hg_sdf.glsl"));
                    String dopLine;
                    while ((dopLine = readLinkFile.readLine()) != null) {
                        shaderSource.append(dopLine).append("//\n");
                    }
                    continue;
                }
                if (line.equals("const int MAX_OBJECTS = 256;")) {
                    shaderSource.append("const int MAX_OBJECTS = " + Scene.getInstance().getMaxObjects() + ";").append("//\n");
                    continue;
                }
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
}

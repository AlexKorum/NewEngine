package engine.renderEngine.modeles;

import engine.renderEngine.modeles.settingModels.MeshModel;
import engine.renderEngine.modeles.settingModels.TextureModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Model {
    private ArrayList<Integer> vbos;
    private int vaoId;
    private MeshModel mesh;
    private TextureModel texture;

    public Model() {
        vbos = new ArrayList<>();
        mesh = new MeshModel(
                new int[]{
                        0, 1, 2,
                        2, 1, 3
                },
                new float[]{
                        -1f, 1f, 0f,
                        -1f, -1f, 0f,
                        1f, 1f, 0f,
                        1f, -1f, 0f
                }
        );

        texture = new TextureModel(0);

        createModel();
    }

    public void draw() {
        GL30.glBindVertexArray(this.vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexesCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void cleanUp() {
        GL30.glDeleteVertexArrays(vaoId);
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        vbos.clear();
    }

    public void setTextureId(int textureId) {
        this.texture.setTextureId(textureId);
    }

    public int getTextureId() {
        return texture.getTextureId();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void createModel() {
        this.vaoId = createVAO();
        bindIndicesBuffer(mesh.getIndices());
        storeDataInAttributeList(0, 3, mesh.getVertexes());
        storeDataInAttributeList(1, 2, texture.getTextureCoordinate());
        GL30.glBindVertexArray(0);
    }

    private int createVAO() {
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        return vaoId;
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber, int sizeData, float[] data) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, sizeData, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }


    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}

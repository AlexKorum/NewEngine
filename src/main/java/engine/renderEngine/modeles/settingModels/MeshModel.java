package engine.renderEngine.modeles.settingModels;

public class MeshModel {
    int[] indices;
    float[] vertexes;

    public MeshModel() {
        this.indices = new int[]{
                0, 1, 2,
                2, 1, 3
        };
        this.vertexes = new float[]{
                -1f, 1f, 0f,
                -1f, -1f, 0f,
                1f, 1f, 0f,
                1f, -1f, 0f
        };
    }

    public MeshModel(int[] indices, float[] vertexes) {
        this.indices = indices;
        this.vertexes = vertexes;
    }

    public void setMeshModel(int[] indices, float[] vertexes) {
        this.indices = indices;
        this.vertexes = vertexes;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getVertexes() {
        return vertexes;
    }

    public int getVertexesCount() {
        return this.indices.length;
    }
}

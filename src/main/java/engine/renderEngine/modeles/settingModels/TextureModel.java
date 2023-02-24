package engine.renderEngine.modeles.settingModels;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL30;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TextureModel {
    private int textureId;
    private float[] textureCoordinate;

    public TextureModel(int textureId) {
        this.textureId = textureId;
        createTextureCoordinate();
    }

    public TextureModel(String src) {
        PNGDecoder decoder;
        ByteBuffer buffer;
        try {
            decoder = new PNGDecoder(new FileInputStream(src));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try {
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.flip();

        int id = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
        GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
        GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameterf(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);

        this.textureId = id;
        createTextureCoordinate();
    }

    private void createTextureCoordinate() {
        textureCoordinate = new float[]{
                0, 0,
                0, 1,
                1, 0,
                1, 1,
        };
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return textureId;
    }

    public float[] getTextureCoordinate() {
        return textureCoordinate;
    }
}

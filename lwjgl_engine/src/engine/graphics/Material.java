package engine.graphics;


import engine.utils.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryStack;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Material {
    private String path;
    private Texture texture;
    private int width, height;
    private int textureID;
    ByteBuffer buf = null;

    public Material(String path) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(path, w, h, channels, 4);
            if(buf == null) {
                throw new Exception("Img file [" + path + "] not loaded: " + stbi_failure_reason());
            }

            this.width = w.get();
            this.height = h.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void create() {
        this.textureID = createTexture(buf);
    }
    private int createTexture(ByteBuffer buf) {
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, 0);

        return id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        unbind();
        glDeleteTextures(textureID);
    }

    public int getTextrueID() {
        return textureID;
    }
}

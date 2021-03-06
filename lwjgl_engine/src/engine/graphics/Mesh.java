package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private Vertex[] vertices;
    private int[] indices;
    private Material material;
    private int vao, pbo, ibo, cbo, tbo;

    public Mesh(Vertex[] vertices, int[] indices, Material material) {
        this.vertices = vertices;
        this.indices = indices;
        this.material = material;
    }

    public void create() {

        FloatBuffer positionBuffer = null;
        FloatBuffer colorBuffer = null;
        FloatBuffer textureBuffer = null;
        IntBuffer indiciesBuffer = null;


        try {

            material.create();
            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);

            positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
            float[] posData = new float[vertices.length*3];
            for(int i = 0; i < vertices.length; i++) {
                posData[i * 3] = vertices[i].getPosition().getX();
                posData[i * 3 + 1] = vertices[i].getPosition().getY();
                posData[i * 3 + 2] = vertices[i].getPosition().getZ();
            }
            positionBuffer.put(posData).flip();

            pbo = storeData(positionBuffer, 0, 3);

            colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
            float[] colorData = new float[vertices.length*3];
            for(int i = 0; i < vertices.length; i++) {
                colorData[i * 3] = vertices[i].getColor().getX();
                colorData[i * 3 + 1] = vertices[i].getColor().getY();
                colorData[i * 3 + 2] = vertices[i].getColor().getZ();
            }
            colorBuffer.put(colorData).flip();

            cbo = storeData(colorBuffer, 1,3);

            textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
            float[] textureData = new float[vertices.length* 2];
            for(int i = 0; i < vertices.length; i++) {
                textureData[i * 2] = vertices[i].getTextureCoord().getX();
                textureData[i * 2 + 1] = vertices[i].getTextureCoord().getY();
            }
            textureBuffer.put(textureData).flip();

            tbo = storeData(textureBuffer, 2,2);

            indiciesBuffer = MemoryUtil.memAllocInt(indices.length);
            indiciesBuffer.put(indices).flip();

            ibo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        } finally {
            if(positionBuffer != null) MemoryUtil.memFree(positionBuffer);
            if(colorBuffer != null) MemoryUtil.memFree(colorBuffer);
            if(textureBuffer != null) MemoryUtil.memFree(textureBuffer);
            if(indiciesBuffer != null) MemoryUtil.memFree(indiciesBuffer);
        }
    }

    private int storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    public void destroy() {
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(cbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);

        GL30.glDeleteVertexArrays(vao);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVAO() {
        return vao;
    }

    public int getPBO() {
        return pbo;
    }

    public int getIBO() {
        return ibo;
    }

    public int getCBO() {
        return cbo;
    }

    public int getTBO() {
        return tbo;
    }

    public Material getMaterial() {
        return material;
    }
}

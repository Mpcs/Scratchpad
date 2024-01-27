package com.mpcs.scratchpad.core.resources;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Image {

    private final BufferedImage contents;
    private final byte[] rgba;

    private int glId;

    public Image(BufferedImage bufferedImage) {
        this.contents = bufferedImage;
        int[] argb = contents.getRGB(0, 0, contents.getWidth(), contents.getHeight(), null, 0, contents.getWidth());
        rgba = intARGBtoByteRGBA(argb);
    }

    private static byte[] intARGBtoByteRGB(int[] argb) {
        byte[] rgb = new byte[argb.length * 4];

        for (int i = 0; i < argb.length; i++) {
            rgb[3 * i    ] = (byte) ((argb[i] >> 16) & 0xff); // R
            rgb[3 * i + 1] = (byte) ((argb[i] >>  8) & 0xff); // G
            rgb[3 * i + 2] = (byte) ((argb[i]      ) & 0xff); // B
        }

        return rgb;
    }

    private static byte[] intARGBtoByteRGBA(int[] argb) {
        byte[] rgba = new byte[argb.length * 4];

        for (int i = 0; i < argb.length; i++) {
            rgba[4 * i    ] = (byte) ((argb[i] >> 16) & 0xff); // R
            rgba[4 * i + 1] = (byte) ((argb[i] >>  8) & 0xff); // G
            rgba[4 * i + 2] = (byte) ((argb[i]      ) & 0xff); // B
            rgba[4 * i + 3] = (byte) ((argb[i] >> 24) & 0xff); // A
        }

        return rgba;
    }

    public byte[] getRGBA() {
        return rgba;
    }

    public int getWidth() {
        return contents.getWidth();
    }

    public int getHeight() {
        return contents.getHeight();
    }


    public int compile(GL gl) {
        IntBuffer textureBuffer = IntBuffer.allocate(1);
        gl.glGenTextures(1, textureBuffer);
        glId = textureBuffer.get();
        gl.glBindTexture(GL.GL_TEXTURE_2D, glId);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        ByteBuffer buffer = ByteBuffer.wrap(this.getRGBA());

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, this.getWidth(), this.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,buffer );
        gl.glGenerateMipmap(GL.GL_TEXTURE_2D);

        return glId;
    }

    public void bind(GL3 gl) {
        if (glId == 0) {
            compile(gl);
        }
        gl.glBindTexture(GL.GL_TEXTURE_2D, this.glId);
    }
}

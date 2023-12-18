package com.mpcs.scratchpad.engine.rendering;

import com.jogamp.opengl.*;
import com.jogamp.opengl.math.Matrix4;
import com.mpcs.logging.Logger;
import com.mpcs.math.Vector3;
import com.mpcs.scratchpad.engine.Context;
import com.mpcs.scratchpad.engine.Engine;
import com.mpcs.scratchpad.engine.scene.nodes.Node;
import com.mpcs.scratchpad.engine.scene.nodes.Model3DNode;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Locale;

public class Renderer implements GLEventListener {


    String vertexShaderCode = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            //layout (location = 1) in vec3 aColor;
            layout (location = 1) in vec2 aTexCoord;
            
            uniform vec3 offset;
            uniform mat4 model;
            uniform mat4 view;
            uniform mat4 projection;
            
            //out vec3 urColor;
            out vec2 TexCoord;
                        
            void main()
            {
                gl_Position = projection * view * model * vec4(aPos, 1.0);
                //urColor = aColor;
                TexCoord = aTexCoord;
            }
            """;

    String fragmentShaderCode = """
            #version 330 core
            out vec4 FragColor;
            //in vec3 urColor;
            in vec2 TexCoord;
            uniform vec4 ourColor;
            uniform float mixVal;
            
            uniform sampler2D texture1;
            uniform sampler2D texture2;
            
            void main()
            {
                FragColor = mix(texture(texture1, TexCoord), texture(texture2, vec2(TexCoord.x, TexCoord.y)), mixVal);// * vec4(urColor.xyz, 1.0f);
            }
            """;

    String fragmentShaderCode2 = """
            #version 330 core
            out vec4 FragColor;
                        
            in vec4 vertexColor;
            
            void main()
            {
                FragColor = vec4(0.0f, 0.5f, 0.2f, 1.0f);
            }
            """;

    int VAO;
    int VBO;
    int EBO;
    int texture;
    int texture2;
    ShaderProgram shaderProgram1;
    ShaderProgram shaderProgram2;
    private final Context context;
    private final Engine engine;

    private Matrix4f trans;

    public Renderer(Context context) {
        this.context = context;
        this.engine = context.getEngine();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        Thread.currentThread().setName("EngineRenderThread(Window)");
        GL3 gl = drawable.getGL().getGL3();

        Shader vertexShader = Shader.createVertexShader(vertexShaderCode);
        Shader fragmentShader1 = Shader.createFragmentShader(fragmentShaderCode);
        //Shader fragmentShader2 = Shader.createFragmentShader(fragmentShaderCode2);
        try {
            vertexShader.compile(gl);
            fragmentShader1.compile(gl);
            //fragmentShader2.compile(gl);
        } catch (ShaderCompileException e) {
            throw new RuntimeException(e);
        }


        shaderProgram1 = new ShaderProgram();
        shaderProgram1.addShader(vertexShader);
        shaderProgram1.addShader(fragmentShader1);


        //shaderProgram2 = new ShaderProgram();
        //shaderProgram2.addShader(vertexShader);
        //shaderProgram2.addShader(fragmentShader2);

        try {
            shaderProgram1.link(gl);
            //shaderProgram2.link(gl);
        } catch (ShaderProgramLinkException e) {
            throw new RuntimeException(e);
        }

        shaderProgram1.setUniform1i(gl, "texture1", 0);
        shaderProgram1.setUniform1i(gl, "texture2", 1);

        vertexShader.delete(gl);
        fragmentShader1.delete(gl);
        //fragmentShader2.delete(gl);
        texture = loadTexture(gl, "D:\\Pliki\\Hobby\\Programowanie\\Scratchpad\\Engine\\src\\main\\resources\\container.jpg", GL.GL_RGB);
        texture2 = loadTexture(gl, "D:\\Pliki\\Hobby\\Programowanie\\Scratchpad\\Engine\\src\\main\\resources\\awesomeface.png", GL.GL_RGBA);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        gl.glClearColor(0.2f, 0.3f, 0.5f, 1.0f);


        Vector4f vector4f = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

        gl.glEnable(GL.GL_DEPTH_TEST);

    }

    private static int loadTexture(GL gl, String path, int type) {
        BufferedImage image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] rgb;
        try {
            image = ImageIO.read(new File(path));
            int[] argb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            if (type == GL.GL_RGB) {
                rgb = intARGBtoByteRGB(argb);
            } else {
                rgb = intARGBtoByteRGBA(argb);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntBuffer textureBuffer = IntBuffer.allocate(1);
        gl.glGenTextures(1, textureBuffer);
        int textureID = textureBuffer.get();
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        ByteBuffer buffer = ByteBuffer.wrap(rgb);

        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, image.getWidth(), image.getHeight(), 0, type, GL.GL_UNSIGNED_BYTE,buffer );
        gl.glGenerateMipmap(GL.GL_TEXTURE_2D);
        return textureID;
    };

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

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if(!engine.isRunning()) { // dirty fix for the render thread not stopping for one frame on javaFX window closing.
            return;
        }
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture2);
        float secondsSinceStart = (float) drawable.getAnimator().getTotalFPSDuration() /1000;
        float greenValue = (float) (Math.sin(secondsSinceStart) / 2.0f + 0.5f);



        shaderProgram1.setUniform4f(gl, "ourColor", 0.0f, greenValue, 0.0f, 1.0f);
        shaderProgram1.setUniform3f(gl, "offset", (float) Math.sin(0), 0.4f, 0.0f);
        shaderProgram1.setUniform1f(gl, "mixVal", (float) Math.sin(0));//context.getInputManager().displ);


        //vector4f.mul(trans);
        //Logger.log("\n" + trans.toString());

        float radius = 10f;
        float camX = (float) (Math.sin(secondsSinceStart) * radius);
        float camZ = (float) (Math.cos(secondsSinceStart) *radius);

        Camera camera = context.getSimulation().getScene().camera;

        //Vector3f camPosition = new Vector3f(camX, 0.0f, camZ);
        //Vector3f targetPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        //Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.lookAt(camera.position, camera.position.add(camera.front, new Vector3f()), camera.up);
        //viewMatrix.translate(new Vector3f(0.0f, 0.0f, -10.0f));

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.perspective((float) Math.toRadians(45), (float) context.getEngine().getGlWindow().getWidth() /context.getEngine().getGlWindow().getHeight(), 0.1f, 100.0f);

        List<Node> elements = context.getSimulation().getScene().getAllNodes();

        shaderProgram1.setUniformMatrix4fv(gl, "view", viewMatrix);
        shaderProgram1.setUniformMatrix4fv(gl, "projection", projectionMatrix);

        for(Node node : elements) {
            if (node instanceof Model3DNode model3DNode) {
                trans = new Matrix4f();
                Matrix4f modelMatrix = new Matrix4f();
                modelMatrix.translate(new Vector3f(node.getAbsolutePosition().toArray()));
                modelMatrix.rotate((float) Math.toRadians(model3DNode.rotation), new Vector3f(0.5f, 1.0f, 0.0f));
                shaderProgram1.setUniformMatrix4fv(gl, "model", modelMatrix);

                //trans.identity();
                //trans.translate(new Vector3f(node.getAbsolutePosition().toArray()));
                //trans.rotate(model3DNode.rotation, 1.0f, 0.0f, 0.0f);
                //trans.scale(model3DNode.scale);

                //shaderProgram1.setUniformMatrix4fv(gl, "transform", trans);


                model3DNode.render(gl, shaderProgram1);
            }
        }



        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_LINE);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}

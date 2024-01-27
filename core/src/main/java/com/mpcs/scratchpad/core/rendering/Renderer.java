package com.mpcs.scratchpad.core.rendering;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.mpcs.scratchpad.core.Context;
import com.mpcs.scratchpad.core.Engine;
import com.mpcs.scratchpad.core.rendering.shader.Shader;
import com.mpcs.scratchpad.core.rendering.shader.ShaderCompileException;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgram;
import com.mpcs.scratchpad.core.rendering.shader.ShaderProgramLinkException;
import com.mpcs.scratchpad.core.scene.nodes.Model3DNode;
import com.mpcs.scratchpad.core.scene.nodes.Node;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

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
            //uniform sampler2D texture2;
            
            void main()
            {
                FragColor = texture(texture1, TexCoord);// * vec4(urColor.xyz, 1.0f);
            }
            """;

    int texture;
    int texture2;
    ShaderProgram shaderProgram1;
    public Context context;
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
        try {
            vertexShader.compile(gl);
            fragmentShader1.compile(gl);
        } catch (ShaderCompileException e) {
            throw new RuntimeException(e);
        }

        shaderProgram1 = new ShaderProgram();
        shaderProgram1.addShader(vertexShader);
        shaderProgram1.addShader(fragmentShader1);

        try {
            shaderProgram1.link(gl);
        } catch (ShaderProgramLinkException e) {
            throw new RuntimeException(e);
        }

        shaderProgram1.setUniform1i(gl, "texture", 0);
        //shaderProgram1.setUniform1i(gl, "texture2", 1);

        vertexShader.delete(gl);
        fragmentShader1.delete(gl);

        gl.glClearColor(0.2f, 0.3f, 0.5f, 1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
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
        float secondsSinceStart = (float) drawable.getAnimator().getTotalFPSDuration() /1000;
        float greenValue = (float) (Math.sin(secondsSinceStart) / 2.0f + 0.5f);

        shaderProgram1.setUniform4f(gl, "ourColor", 0.0f, greenValue, 0.0f, 1.0f);
        shaderProgram1.setUniform3f(gl, "offset", (float) Math.sin(0), 0.4f, 0.0f);
        shaderProgram1.setUniform1f(gl, "mixVal", (float) Math.sin(0));

        Camera camera = context.getSimulation().getScene().camera;

        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.lookAt(camera.position, camera.position.add(camera.front, new Vector3f()), camera.up);

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.perspective((float) Math.toRadians(45), (float) context.getEngine().getGlWindow().getWidth() /context.getEngine().getGlWindow().getHeight(), 0.1f, 100.0f);

        List<Node> elements = context.getSimulation().getScene().getAllNodes();

        shaderProgram1.setUniformMatrix4fv(gl, "view", viewMatrix);
        shaderProgram1.setUniformMatrix4fv(gl, "projection", projectionMatrix);

        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        for(Node node : elements) {
            if (node instanceof Model3DNode model3DNode) {
                trans = new Matrix4f();
                Matrix4f modelMatrix = new Matrix4f();
                modelMatrix.translate(node.getAbsolutePosition());
                modelMatrix.rotate((float) Math.toRadians(model3DNode.rotation), new Vector3f(0.5f, 1.0f, 0.0f));
                modelMatrix.scale(model3DNode.scale);
                shaderProgram1.setUniformMatrix4fv(gl, "model", modelMatrix);
                model3DNode.render(gl, shaderProgram1);
            }
        }

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }
}

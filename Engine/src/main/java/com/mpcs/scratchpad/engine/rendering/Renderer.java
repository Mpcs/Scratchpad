package com.mpcs.scratchpad.engine.rendering;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.mpcs.logging.Logger;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Renderer implements GLEventListener {

    float[] vertices1 = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f  // bottom left

    };
    float[] vertices2 = {
            -0.5f,  0.5f, 0.0f,   // top left
            0.5f,  0.5f, 0.0f,  // top right
            -0.5f, -0.5f, 0.0f  // bottom left
    };


    String vertexShaderCode = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            out vec4 vertexColor;
                        
            void main()
            {
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0f);
                vertexColor = vec4(0.5, 0.0, aPos.z, 1.0);
            }
            """;

    String fragmentShaderCode = """
            #version 330 core
            out vec4 FragColor;
                        
            in vec4 vertexColor;
            void main()
            {
                FragColor = vertexColor;//vec4(1.0f, 0.5f, 0.2f, 1.0f);
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

    int VAO1;
    int VAO2;
    int EBO;
    ShaderProgram shaderProgram1;
    ShaderProgram shaderProgram2;

    @Override
    public void init(GLAutoDrawable drawable) {
        Thread.currentThread().setName("EngineRenderThread(Window)");
        GL3 gl = drawable.getGL().getGL3();

        Shader vertexShader = Shader.createVertexShader(vertexShaderCode);
        Shader fragmentShader1 = Shader.createFragmentShader(fragmentShaderCode);
        Shader fragmentShader2 = Shader.createFragmentShader(fragmentShaderCode2);
        try {
            vertexShader.compile(gl);
            fragmentShader1.compile(gl);
            fragmentShader2.compile(gl);
        } catch (ShaderCompileException e) {
            throw new RuntimeException(e);
        }


        shaderProgram1 = new ShaderProgram();
        shaderProgram1.addShader(vertexShader);
        shaderProgram1.addShader(fragmentShader1);

        shaderProgram2 = new ShaderProgram();
        shaderProgram2.addShader(vertexShader);
        shaderProgram2.addShader(fragmentShader2);

        try {
            shaderProgram1.link(gl);
            shaderProgram2.link(gl);
        } catch (ShaderProgramLinkException e) {
            throw new RuntimeException(e);
        }


        vertexShader.delete(gl);
        fragmentShader1.delete(gl);
        fragmentShader2.delete(gl);

        gl.glClearColor(0.2f, 0.3f, 0.5f, 1.0f);


        IntBuffer vaoBuffer = IntBuffer.allocate(1);
        IntBuffer vboBuffer = IntBuffer.allocate(1);
        IntBuffer eboBuffer = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, vaoBuffer);
        gl.glGenBuffers(1, vboBuffer);
        gl.glGenBuffers(1, eboBuffer);

        VAO1 = vaoBuffer.get();
        int VBO1 = vboBuffer.get();

        gl.glBindVertexArray(VAO1);

        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices1);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO1);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices1.length*4/*4 bytes per float*/, fbVertices, GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);


        IntBuffer vao2Buff = IntBuffer.allocate(1);
        IntBuffer vbo2Buff = IntBuffer.allocate(1);

        gl.glGenBuffers(1, vbo2Buff);
        gl.glGenVertexArrays(1, vao2Buff);
        VAO2 = vao2Buff.get();
        int VBO2 = vbo2Buff.get();

        gl.glBindVertexArray(VAO2);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO2);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices2.length*4, Buffers.newDirectFloatBuffer(vertices2), GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        Logger.log("A");

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        //OneTri.render( drawable.getGL().getGL2(), drawable.getSurfaceWidth(), drawable.getSurfaceHeight() );
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glUseProgram(shaderProgram1.getID());
        gl.glBindVertexArray(VAO1);

        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_LINE);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);

        gl.glUseProgram(shaderProgram2.getID());
        gl.glBindVertexArray(VAO2);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);


        //gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO);
        //gl.glDrawElements(GL.GL_TRIANGLES, 6, GL.GL_UNSIGNED_INT, 0);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_FILL);
        //gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);
        //gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);


    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        //this already happens behind the scenes, but leave it for now for learning purposes
        //gl.glViewport(0,0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        //OneTri.setup( drawable.getGL().getGL2(), width, height );
    }
}

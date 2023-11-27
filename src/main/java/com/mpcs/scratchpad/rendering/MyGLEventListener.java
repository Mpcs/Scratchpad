package com.mpcs.scratchpad.rendering;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.mpcs.logging.Logger;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MyGLEventListener implements GLEventListener {

    float[] vertices = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
    };

    int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    String vertexShader = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
                        
            void main()
            {
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0f);
            }
            """;

    String fragmentShader = """
            #version 330 core
            out vec4 FragColor;
                        
            void main()
            {
                FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
            }
            """;

    int VAO;
    int VBO;
    int EBO;
    int shaderProgram;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        //compile shaders
        int vertexShaderId = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
        gl.glShaderSource(vertexShaderId, 1, new String[]{vertexShader}, null);
        gl.glCompileShader(vertexShaderId);
        IntBuffer code = IntBuffer.allocate(1);
        gl.glGetShaderiv(vertexShaderId, GL4.GL_COMPILE_STATUS, code);
        if (code.get() != 1)
            Logger.log("Błąd kompilacji vertex shader: " + code.get());

        int fragmentShaderId = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShaderId, 1, new String[]{fragmentShader}, null);
        gl.glCompileShader(fragmentShaderId);
        IntBuffer code2 = IntBuffer.allocate(1);
        gl.glGetShaderiv(fragmentShaderId, GL4.GL_COMPILE_STATUS, code2);
        if (code2.get() != 1)
            Logger.log("Błąd kompilacji fragment shader: " + code.get());

        shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram, vertexShaderId);
        gl.glAttachShader(shaderProgram, fragmentShaderId);
        gl.glLinkProgram(shaderProgram);
        IntBuffer code3 = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderProgram, GL3.GL_LINK_STATUS, code3);
        if (code3.get() != 1)
            Logger.log("Błąd linkowania programu shaderowego: " + code.get());


        gl.glDeleteShader(vertexShaderId);
        gl.glDeleteShader(fragmentShaderId);

        gl.glClearColor(0.2f, 0.3f, 0.5f, 1.0f);


        IntBuffer vaoBuffer = IntBuffer.allocate(1);
        IntBuffer vboBuffer = IntBuffer.allocate(1);
        IntBuffer eboBuffer = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, vaoBuffer);
        gl.glGenBuffers(1, vboBuffer);
        gl.glGenBuffers(1, eboBuffer);

        VAO = vaoBuffer.get();
        VBO = vboBuffer.get();
        EBO = eboBuffer.get();

        gl.glBindVertexArray(VAO);

        FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length*4/*4 bytes per float*/, fbVertices, GL.GL_STATIC_DRAW);

        IntBuffer fbIndices = Buffers.newDirectIntBuffer(indices);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length*(Integer.BYTES), fbIndices, GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);

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

        gl.glUseProgram(shaderProgram);
        gl.glBindVertexArray(VAO);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_LINE);
        //gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO);
        gl.glDrawElements(GL.GL_TRIANGLES, 6, GL.GL_UNSIGNED_INT, 0);
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

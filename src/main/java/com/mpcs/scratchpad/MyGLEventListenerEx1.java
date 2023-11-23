package com.mpcs.scratchpad;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.mpcs.logging.Logger;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;

public class MyGLEventListenerEx1 implements GLEventListener {

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


    String vertexShader = """
            #version 330 core
            layout (location = 0) in vec3 aPos;
            out vec4 vertexColor;
                        
            void main()
            {
                gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0f);
                vertexColor = vec4(0.5, 0.0, aPos.z, 1.0);
            }
            """;

    String fragmentShader = """
            #version 330 core
            out vec4 FragColor;
                        
            in vec4 vertexColor;
            void main()
            {
            
                FragColor = vertexColor;//vec4(1.0f, 0.5f, 0.2f, 1.0f);
            }
            """;

    String fragmentShader2 = """
            #version 330 core
            out vec4 FragColor;
                        
            in vec4 vertexColor;
            void main()
            {
                FragColor = vertexColor;//vec4(0.0f, 0.5f, 0.2f, 1.0f);
            }
            """;

    int VAO1;
    int VAO2;
    int EBO;
    int shaderProgram1;
    int shaderProgram2;

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        //compile shaders
        int vertexShaderId = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
        gl.glShaderSource(vertexShaderId, 1, new String[]{vertexShader}, null);
        gl.glCompileShader(vertexShaderId);
        IntBuffer code = IntBuffer.allocate(1);
        gl.glGetShaderiv(vertexShaderId, GL4.GL_COMPILE_STATUS, code);
        int codee = code.get();
        if (codee != 1) {
            IntBuffer len = IntBuffer.allocate(1);
            ByteBuffer bybuff = ByteBuffer.allocate(496);
            gl.glGetShaderInfoLog(vertexShaderId, 496, len, bybuff);
            Logger.log("Błąd kompilacji vertex shader: " + StandardCharsets.UTF_8.decode(bybuff));
        }

        int fragmentShaderId1 = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShaderId1, 1, new String[]{fragmentShader}, null);
        gl.glCompileShader(fragmentShaderId1);
        IntBuffer code2 = IntBuffer.allocate(1);
        gl.glGetShaderiv(fragmentShaderId1, GL4.GL_COMPILE_STATUS, code2);
        if (code2.get() != 1) {
            IntBuffer len = IntBuffer.allocate(1);
            ByteBuffer bybuff = ByteBuffer.allocate(512);
            gl.glGetShaderInfoLog(fragmentShaderId1, 512, len, bybuff);
            Logger.log("Błąd kompilacji fragment shader: " + bybuff.toString());
        }


        int fragmentShaderId2 = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentShaderId2, 1, new String[]{fragmentShader2}, null);
        gl.glCompileShader(fragmentShaderId2);

        shaderProgram1 = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram1, vertexShaderId);
        gl.glAttachShader(shaderProgram1, fragmentShaderId1);
        gl.glLinkProgram(shaderProgram1);
        IntBuffer code3 = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderProgram1, GL3.GL_LINK_STATUS, code3);
        if (code3.get() != 1)
            Logger.log("Błąd linkowania programu shaderowego: " + code.get());

        shaderProgram2 = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram2, vertexShaderId);
        gl.glAttachShader(shaderProgram2, fragmentShaderId2);
        gl.glLinkProgram(shaderProgram2);

        gl.glDeleteShader(vertexShaderId);
        gl.glDeleteShader(fragmentShaderId1);
        gl.glDeleteShader(fragmentShaderId2);

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

        gl.glUseProgram(shaderProgram1);
        gl.glBindVertexArray(VAO1);

        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL3.GL_LINE);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 3);

        gl.glUseProgram(shaderProgram2);
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

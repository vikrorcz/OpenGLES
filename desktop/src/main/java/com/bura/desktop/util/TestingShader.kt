package com.bura.desktop.util

import org.lwjgl.opengles.GLES20

class TestingShader {
    companion object {
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        fun linkNoColor(vertexShader: Int, fragmentShader: Int): Int {
            // create empty OpenGL ES Program
            val program = GLES20.glCreateProgram().also {

                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)

                val linkStatus = IntArray(1)
                GLES20.glGetProgramiv(it,GLES20.GL_LINK_STATUS, linkStatus)
            }
            return program
        }

        fun linkColor(vertexShader: Int, fragmentShader: Int): Int {
            val program = GLES20.glCreateProgram().also {
                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)

                GLES20.glBindAttribLocation(it, 0, "a_Position")

                GLES20.glLinkProgram(it)
                val linkStatus = IntArray(1)
                GLES20.glGetProgramiv(it, GLES20.GL_LINK_STATUS, linkStatus)

                if (linkStatus[0] == 0) {
                    GLES20.glDeleteProgram(it)
                    return 0
                }
            }
            return program
        }
    }
}
package com.bura.desktop.util

import org.lwjgl.opengles.GLES20

class ShaderUtil {
    companion object {
        val fragmentShader = "precision mediump float;\n" +
                "\n" +
                "uniform vec4 u_Color;\n" +
                "void main()\n" +
                "{\n" +
                "    gl_FragColor = u_Color;\n" +
                "} "

        val vertexShader = "precision mediump float;\n" +
                "\n" +
                "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = a_Position;// gl_Position = u_Matrix * a_Position;\n" +
                "    gl_PointSize = 10.0;\n" +
                "}"

        fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)//  return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
        }

        fun compileFragmentShader(shaderCode: String): Int {
            return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)//  return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
        }

        private fun compileShader(type: Int, shaderCode: String): Int {
            val shaderObjectId = GLES20.glCreateShader(type)

            if (shaderObjectId == 0) {
                if (Constants.LOGGER_ON) {
                    println( "Could not create new shader.")
                }
                return 0
            }

            GLES20.glShaderSource(shaderObjectId, shaderCode)
            GLES20.glCompileShader(shaderObjectId)

            val compileStatus = IntArray(1)
            //GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus)
            //GLES20.glGetProgramiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus)

            if (Constants.LOGGER_ON) {
                // Print the shader info log to the Android log output.
                println( "Results of compiling source:  $shaderCode:${GLES20.glGetShaderInfoLog(shaderObjectId)}".trimIndent())
            }

            if (compileStatus[0] == 0) {
                // If it failed, delete the shader object.
                //GLES20.glDeleteShader(shaderObjectId)
                if (Constants.LOGGER_ON) {
                    println( "Compilation of shader failed.")
                }
                return 0
            }
            return shaderObjectId
        }

        fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
            /*
            val programObjectId = GLES20.glCreateProgram()
            if (programObjectId == 0) {
                if (Constants.LOGGER_ON) {
                   println("Could not create new program")
                }
                return 0
            }
            GLES20.glAttachShader(programObjectId, vertexShaderId)
            GLES20.glAttachShader(programObjectId, fragmentShaderId)
            GLES20.glLinkProgram(programObjectId)
            GLES20.glLinkProgram(programObjectId)

            */
            val programObjectId = GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShaderId)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShaderId)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)
            }
            val linkStatus = IntArray(1)
           // GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            /*
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus)


            if (Constants.LOGGER_ON) {
                // Print the program info log to the Android log output.
                println("Results of linking program: ${GLES20.glGetProgramInfoLog(programObjectId)}".trimIndent())
            }
            if (linkStatus[0] == 0) {
                // If it failed, delete the program object.
                GLES20.glDeleteProgram(programObjectId)
                if (Constants.LOGGER_ON) {
                   println("Linking of program failed.")
                }
                return 0
            }

             */

            return programObjectId
        }

        fun validateProgram(programObjectId: Int): Boolean {
            GLES20.glValidateProgram(programObjectId)
            val validateStatus = IntArray(1)
            //GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus)
            println( "Results of validating program: ${validateStatus[0]} Log:${GLES20.glGetProgramInfoLog(programObjectId)}")
            return validateStatus[0] != 0
        }
    }
}
package com.bura.opengles.util

import android.opengl.GLES20
import android.util.Log

class ShaderUtil {
    companion object {
        private const val TAG = "ShaderUtil"
        fun compileVertexShader(shaderCode: String): Int {
            return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
        }

        fun compileFragmentShader(shaderCode: String): Int {
            return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
        }

        private fun compileShader(type: Int, shaderCode: String): Int {
            val shaderObjectId = GLES20.glCreateShader(type)
            if (shaderObjectId == 0) {
                if (Constants.LOGGER_ON) {
                    Log.w(TAG, "Could not create new shader.")
                }
                return 0
            }
            GLES20.glShaderSource(shaderObjectId, shaderCode)
            GLES20.glCompileShader(shaderObjectId)
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (Constants.LOGGER_ON) {
                // Print the shader info log to the Android log output.
                Log.v(TAG, "Results of compiling source:  $shaderCode:${GLES20.glGetShaderInfoLog(shaderObjectId)}".trimIndent())
            }
            if (compileStatus[0] == 0) {
                // If it failed, delete the shader object.
                GLES20.glDeleteShader(shaderObjectId)
                if (Constants.LOGGER_ON) {
                    Log.w(TAG, "Compilation of shader failed.")
                }
                return 0
            }
            return shaderObjectId
        }

        fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
            val programObjectId = GLES20.glCreateProgram()
            if (programObjectId == 0) {
                if (Constants.LOGGER_ON) {
                    Log.w(TAG, "Could not create new program")
                }
                return 0
            }
            GLES20.glAttachShader(programObjectId, vertexShaderId)
            GLES20.glAttachShader(programObjectId, fragmentShaderId)
            GLES20.glLinkProgram(programObjectId)
            GLES20.glLinkProgram(programObjectId)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (Constants.LOGGER_ON) {
                // Print the program info log to the Android log output.
                Log.v(TAG, "Results of linking program: ${GLES20.glGetProgramInfoLog(programObjectId)}".trimIndent())
            }
            if (linkStatus[0] == 0) {
                // If it failed, delete the program object.
                GLES20.glDeleteProgram(programObjectId)
                if (Constants.LOGGER_ON) {
                    Log.w(TAG, "Linking of program failed.")
                }
                return 0
            }
            return programObjectId
        }

        fun validateProgram(programObjectId: Int): Boolean {
            GLES20.glValidateProgram(programObjectId)
            val validateStatus = IntArray(1)
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
            Log.v(
                TAG, "Results of validating program: ${validateStatus[0]} Log:${GLES20.glGetProgramInfoLog(programObjectId)}".trimIndent())
            return validateStatus[0] != 0
        }
    }
}
package com.bura.desktop.util

import com.bura.common.engine.Engine
import org.lwjgl.opengles.GLES20

class ShaderUtil(private val engine: Engine) {
    fun createProgram() {
        val program = GLES20.glCreateProgram()
        val vshader: Int =
            DemoUtils.createShader("foo/simple_vertex_shader.glsl", GLES20.GL_VERTEX_SHADER)
        val fshader: Int =
            DemoUtils.createShader("foo/simple_fragment_shader.glsl", GLES20.GL_FRAGMENT_SHADER)
        GLES20.glAttachShader(program, vshader)
        GLES20.glAttachShader(program, fshader)
        GLES20.glLinkProgram(program)
        val linked = GLES20.glGetProgrami(program, GLES20.GL_LINK_STATUS)
        val programLog = GLES20.glGetProgramInfoLog(program)
        if (programLog.trim { it <= ' ' }.isNotEmpty()) System.err.println(programLog)
        if (linked == 0) throw AssertionError("Could not link program")
        GLES20.glUseProgram(program)
        val texLocation = GLES20.glGetUniformLocation(program, "tex")
        GLES20.glUniform1i(texLocation, 0)
        //quadProgram_inputPosition = GLES20.glGetAttribLocation(program, "position")
        //quadProgram_inputTextureCoords = GLES20.glGetAttribLocation(program, "texCoords")
        GLES20.glUseProgram(0)
        engine.program = program
    }

    fun createTextureProgram() {
        val program = GLES20.glCreateProgram()
        val vshader: Int =
            DemoUtils.createShader("foo/texturedQuad.vs", GLES20.GL_VERTEX_SHADER)
        val fshader: Int =
            DemoUtils.createShader("foo/texturedQuad.fs", GLES20.GL_FRAGMENT_SHADER)
        GLES20.glAttachShader(program, vshader)
        GLES20.glAttachShader(program, fshader)
        GLES20.glLinkProgram(program)
        val linked = GLES20.glGetProgrami(program, GLES20.GL_LINK_STATUS)
        val programLog = GLES20.glGetProgramInfoLog(program)
        if (programLog.trim { it <= ' ' }.isNotEmpty()) System.err.println(programLog)
        if (linked == 0) throw AssertionError("Could not link program")
        GLES20.glUseProgram(program)
        val texLocation = GLES20.glGetUniformLocation(program, "tex")
        GLES20.glUniform1i(texLocation, 0)
        //quadProgram_inputPosition = GLES20.glGetAttribLocation(program, "position")
        //quadProgram_inputTextureCoords = GLES20.glGetAttribLocation(program, "texCoords")
        GLES20.glUseProgram(0)
        engine.textureProgram = program
    }
}
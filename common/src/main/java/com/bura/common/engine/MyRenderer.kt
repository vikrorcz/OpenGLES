package com.bura.common.engine

import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.util.GLES20
import com.bura.common.util.Matrix4f

class MyRenderer(private val engine: Engine) {
    /**
     * Main draw loop, that is shared between all platforms
     */
    fun draw() {
        gles20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix4f.setLookAt(engine.viewMatrix, 0f,0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix4f.multiply(engine.vPMatrix, engine.projectionMatrix, engine.viewMatrix)
        Matrix4f.copy(engine.scratch, engine.vPMatrix)

        gles20.glClearColor(0f, 0f, 0f, 0f)

        engine.texture.draw()

        engine.joystickLeft.draw()

    }
}
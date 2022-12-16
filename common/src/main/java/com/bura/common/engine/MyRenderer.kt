package com.bura.common.engine

import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.gameobject.Player
import com.bura.common.util.GLES20
import com.bura.common.util.MathUtil
import com.bura.common.util.Matrix4f
import com.bura.common.util.MatrixUtil
import kotlin.coroutines.coroutineContext

class MyRenderer(private val engine: Engine) {
    /**
     * Main draw loop, that is shared between all platforms
     */

    fun draw() {
        engine.fpsCounter.logFrame()
        gles20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //Do not move camera with player
        //Matrix4f.setLookAt(engine.viewMatrix, 0f,0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //Matrix4f.multiply(engine.vPMatrix, engine.projectionMatrix, engine.viewMatrix)

        //Move camera with player
        Matrix4f.setLookAt(engine.viewMatrix, 0f,0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix4f.translateM(engine.viewMatrix, 0, -engine.player.centerX, -engine.player.centerY, 0f)
        Matrix4f.multiply(engine.vPMatrix, engine.projectionMatrix, engine.viewMatrix)

        for (gameObject in engine.gameObjectArrayList) {
            gameObject.draw()
        }

        val time = System.currentTimeMillis() % 4000L
        val angle = 0.090f * time.toInt()
        engine.matrixUtil.updateMatrix(engine.texture2, angle)
        engine.texture2.draw()
        engine.matrixUtil.restoreMatrix()

        engine.triangle.centerX = engine.joystickLeft.centerX
        engine.triangle.centerY = engine.joystickLeft.centerY
        engine.matrixUtil.updateMatrix(engine.triangle)
        engine.triangle.draw()
        engine.matrixUtil.restoreMatrix()

        if (engine.endPoint == Engine.DeviceType.ANDROID) {
            engine.joystickLeft.draw()
        }
    }
}
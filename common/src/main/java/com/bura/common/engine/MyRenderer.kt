package com.bura.common.engine

import com.bura.common.engine.Engine.Companion.gles20
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
        Matrix4f.setLookAt(engine.viewMatrix, 0f,0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix4f.multiply(engine.vPMatrix, engine.projectionMatrix, engine.viewMatrix)

        //inputUpdate()
        gles20.glClearColor(0f, 0f, 0f, 0f)

        engine.matrixUtil.updateMatrix(engine.texture, Math.toDegrees(
            MathUtil.getAngle(engine.texture.centerX, engine.texture.centerY, engine.screenTouchX, engine.screenTouchY)
        ).toFloat() + 90)
        engine.texture.draw()
        engine.matrixUtil.restoreMatrix()

        engine.matrixUtil.updateMatrix(engine.texture2, 180f)
        engine.texture2.draw()
        engine.matrixUtil.restoreMatrix()

        engine.triangle.draw()

        if (engine.endPoint == Engine.DeviceType.ANDROID) {
            engine.joystickLeft.draw()
        }
    }
}
package com.bura.common.objects

import com.bura.common.engine.Engine
import com.bura.common.util.MathUtil
import com.bura.common.util.Matrix4f
import java.nio.FloatBuffer
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater


abstract class Shape(
    protected open val engine: Engine,
    open var centerX: Float,
    open var centerY: Float
) {
    open var x = 0f
    open var y = 0f
    open var width = 0f
    open var height = 0f
    protected open var vertexData: FloatBuffer? = null
    protected open var vertexCount = 0
    open var color: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    open var rotationMatrix = FloatArray(16)
    open var mModelMatrix = FloatArray(16)
    open var mTempMatrix = FloatArray(16)

    abstract fun draw()

    protected fun updateTranslationAndRotationMatrix() {
        Matrix4f.setIdentityM(engine.texture.mModelMatrix, 0)
        Matrix4f.translateM(engine.texture.mModelMatrix, 0, engine.texture.centerX, engine.texture.centerY, 0f)

        Matrix4f.setRotateM(
            engine.texture.rotationMatrix, 0,
            Math.toDegrees(
                MathUtil.getAngle(engine.texture.centerX, engine.texture.centerY, engine.screenTouchX, engine.screenTouchY)
            ).toFloat() + 90,
            0f,0f,1f
        )

        engine.texture.mTempMatrix = engine.texture.mModelMatrix.clone()
        Matrix4f.multiply(engine.texture.mModelMatrix, engine.texture.mTempMatrix, engine.texture.rotationMatrix)

        engine.texture.mTempMatrix = engine.vPMatrix.clone()
        Matrix4f.multiply(engine.vPMatrix, engine.texture.mTempMatrix, engine.texture.mModelMatrix)
    }
}
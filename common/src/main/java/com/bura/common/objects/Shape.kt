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
}
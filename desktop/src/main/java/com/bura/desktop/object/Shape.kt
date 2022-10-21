package com.bura.desktop.`object`

import com.bura.desktop.engine.Engine
import java.nio.FloatBuffer


abstract class Shape(
    protected open val engine: Engine,
    open var centerX: Float,
    open var centerY: Float
) {

    protected open var width = 0f
    protected open var height = 0f
    protected open lateinit var vertexData: FloatBuffer
    protected open var vertexCount = 0
    open var color: FloatArray = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    abstract fun draw()
}
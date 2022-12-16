package com.bura.common.gameobject

import com.bura.common.engine.Engine
import com.bura.common.objects.Shape

abstract class GameObject(protected open val engine: Engine, open var x: Float, var y: Float) {
    open var centerX = 0f
    open var centerY = 0f
    open var width = 0f
    open var height = 0f

    abstract fun draw()
}
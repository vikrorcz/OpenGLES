package com.bura.common.objects

import com.bura.common.engine.Engine
import com.bura.common.util.Matrix4f
import com.bura.common.util.MatrixUtil
import com.bura.common.util.TextureUtil

class Joystick(val engine: Engine, val id: Int) {
    var angle = 0f
    var isTouched = false


    private val innerWidth = 0.8f
    private val innerHeight = 0.8f
    val outerWidth = 1.5f
    val outerHeight = 1.5f

    var outerX = -1.25f
    var outerY = 0.05f

    val centerX = outerX - 0.2f
    val centerY = outerY - 0.2f
    var actuatorX = centerX
    var actuatorY = centerY

    private val outerTexture = Texture(engine, outerX, outerY, outerWidth, outerHeight, TextureUtil.joystickInnerId)
    private val innerTexture = Texture(engine, 0f,0f, innerWidth, innerHeight, TextureUtil.joystickOuterId)

    fun draw() {
        if (!isTouched) {
            actuatorX = 0f
            actuatorY = 0f
        }

        engine.matrixUtil.updateMatrix(outerTexture)
        outerTexture.draw()
        engine.matrixUtil.restoreMatrix()

        innerTexture.centerX = actuatorX - 1.25f
        innerTexture.centerY = actuatorY
        engine.matrixUtil.updateMatrix(innerTexture)
        innerTexture.draw()
        engine.matrixUtil.restoreMatrix()
    }
}
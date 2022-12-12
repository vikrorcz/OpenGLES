package com.bura.common.objects

import com.bura.common.engine.Engine
import com.bura.common.util.Matrix4f
import com.bura.common.util.MatrixUtil
import com.bura.common.util.TextureUtil

class Joystick(val engine: Engine, val id: Int) {
    var angle = 0f
    var isTouched = false
    val centerX = -1.5f
    val centerY = -0.5f
    var actuatorX = centerX;
    var actuatorY = centerY;

    private val innerWidth = 0.8f
    private val innerHeight = 0.8f
    val outerWidth = 1.5f
    private val outerHeight = 1.5f

    private val outerTexture = Texture(engine, -0.14f,0.14f, outerWidth, outerHeight, TextureUtil.joystickInnerId)
    private val innerTexture = Texture(engine, 0f,0f, innerWidth, innerHeight, TextureUtil.joystickOuterId)

    fun draw() {
        if (!isTouched) {
            actuatorX = 0f
            actuatorY = 0f
        }

        engine.matrixUtil.translate(centerX, centerY)
        //engine.scratch = Matrix4f.translate(centerX, centerY, 0f)
        outerTexture.draw()
        //engine.scratch = Matrix4f.translate(actuatorX, actuatorY, 0f)
        engine.matrixUtil.translate(actuatorX, actuatorY)
        innerTexture.draw()
    }
}
package com.bura.opengles.`object`

import com.bura.opengles.engine.Engine
import com.bura.opengles.util.TextureUtil

class Joystick(val engine: Engine, val id: Int) {
    var angle = 0f
    var isTouched = false
    val centerX = -2.5f
    val centerY = -1f
    var actuatorX = centerX;
    var actuatorY = centerY;

    private val innerWidth = 1f
    private val innerHeight = 1f
    private val outerWidth = 2f
    private val outerHeight = 2f

    private val outerTexture = Texture(engine, -0.2f,0.2f, outerWidth, outerHeight, TextureUtil.joystickOuterTextureLocation);
    private val innerTexture = Texture(engine, 0f,0f, innerWidth, innerHeight, TextureUtil.joystickInnerTextureLocation);


    fun draw() {
        if (!isTouched) {
            actuatorX = 0f
            actuatorY = 0f
        }

        engine.matrixUtil.translate(centerX, centerY)
        outerTexture.draw()
        engine.matrixUtil.translate(actuatorX, actuatorY)
        innerTexture.draw()
    }
}
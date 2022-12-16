package com.bura.common.gameobject

import com.bura.common.engine.Engine
import com.bura.common.objects.Texture
import com.bura.common.util.MathUtil
import com.bura.common.util.TextureUtil
import kotlin.math.cos
import kotlin.math.sin

class Player(engine: Engine, centerX: Float, centerY: Float) : Entity(engine, centerX, centerY) {
    private val texture = Texture(engine, centerX, centerY, 1f,1f, TextureUtil.playerTextureId)

    //Android
    var velocityX = 0f
    var velocityY = 0f
    var speed = 0.01f

    override fun draw() {
        val angle = if (engine.endPoint == Engine.DeviceType.DESKTOP) MathUtil.getAngle(texture.centerX, texture.centerY, engine.screenTouchX, engine.screenTouchY) else {
            MathUtil.getAngle(engine.joystickLeft.centerX, engine.joystickLeft.centerY, engine.joystickLeft.actuatorX, engine.joystickLeft.actuatorY)
        }

        if (engine.endPoint == Engine.DeviceType.ANDROID) {
            if (engine.joystickLeft.isTouched) {
                texture.centerX -= cos(angle.toFloat()) * speed
                texture.centerY -= sin(angle.toFloat()) * speed
            }
        } else {
            texture.centerX = centerX
            texture.centerY = centerY
        }

        engine.matrixUtil.updateMatrix(texture, Math.toDegrees(angle).toFloat() + 90)
        texture.draw()
        engine.matrixUtil.restoreMatrix()
    }
}
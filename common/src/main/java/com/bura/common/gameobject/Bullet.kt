package com.bura.common.gameobject

import com.bura.common.engine.Engine
import com.bura.common.objects.Texture
import com.bura.common.util.Matrix4f
import com.bura.common.util.TextureUtil
import kotlin.math.cos
import kotlin.math.sin

class Bullet(engine: Engine, centerX: Float, centerY: Float) : Entity(engine, centerX, centerY) {
    private val texture = Texture(engine, centerX, centerY,0.07f,0.14f,TextureUtil.machineGunShotId)

    var angle = 0f
    var speed = 0.05f

    override fun draw() {
        texture.centerX -= cos(angle) * speed
        texture.centerY -= sin(angle) * speed

        Matrix4f.setIdentityM(texture.mModelMatrix, 0)
        Matrix4f.translateM(texture.mModelMatrix, 0, texture.centerX, texture.centerY - 0.05f, 0f)
        Matrix4f.setIdentityM(texture.rotationMatrix, 0)
        Matrix4f.setRotateM(
            texture.rotationMatrix, 0,
            Math.toDegrees(angle.toDouble()).toFloat() + 90,
            0f,0f,1f
        )
        Matrix4f.translateM(texture.rotationMatrix, 0,-texture.x - texture.width / 5f, -texture.y - texture.height / 5f, 0f)
        texture. mTempMatrix = texture.mModelMatrix.clone()
        Matrix4f.multiply(texture.mModelMatrix, texture.mTempMatrix, texture.rotationMatrix)
        texture.mTempMatrix = engine.vPMatrix.clone()
        Matrix4f.multiply(engine.vPMatrix, texture.mTempMatrix, texture.mModelMatrix)
        texture.draw()
        engine.matrixUtil.restoreMatrix()
    }
}
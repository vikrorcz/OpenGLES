package com.bura.common.util

import com.bura.common.engine.Engine
import kotlin.math.cos
import kotlin.math.sin

class MatrixUtil(private val engine: Engine) {
    fun cameraTranslate(posX: Float, posY: Float) {
        //Matrix.translateM(engine.viewMatrix, 0, posX, posY, 0f)
        //Matrix.multiplyMM(engine.vPMatrix, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)

    }

    private var velocityX = 0f
    private var velocityY = 0f

    private val seekerVelocityX = 0f
    private val seekerVelocityY = 0f

    fun cameraTranslate(directionAngle: Double) {
        /*
        if (engine.joystickLeft.isTouched) {
            velocityX += cos(directionAngle + Math.PI * 2).toFloat() * 0.02f
            velocityY += sin(directionAngle + Math.PI * 2).toFloat() * 0.02f
        }
        Matrix.translateM(engine.viewMatrix, 0, velocityX, velocityY, 0f)
        engine.cameraCenterX = -velocityX
        engine.cameraCenterY = -velocityY
        Matrix.multiplyMM(engine.vPMatrix, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)
        */
    }

    fun withCameraTranslate() {
        //Matrix.translateM(engine.viewMatrix, 0, engine.cameraCenterX, engine.cameraCenterY, 0f)
        //Matrix.multiplyMM(engine.scratch, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)

    }

    fun translate(posX: Float, posY: Float) {
        //version 1
        //Matrix.setIdentityM(com.bura.common.engine.translationMatrix,0);
        //Matrix.translateM(com.bura.common.engine.translationMatrix, 0, posX, posY, 0);
        //Matrix.multiplyMM(com.bura.common.engine.scratch, 0, com.bura.common.engine.scratch,0, com.bura.common.engine.translationMatrix, 0);

        //version 2
        Matrix4f.translateM(engine.scratch, 0, posX, posY, 0f)
        //engine.scratch = Matrix4f.translate(posX,posY,0f)
    }

    private var velSeekerX = 0f
    private var velSeekerY = 0f

    fun translateByAngle(angle: Float) {
        velSeekerX -= cos(angle).toFloat() * 0.01f
        velSeekerY -= sin(angle).toFloat() * 0.01f
        ///Matrix.translateM(engine.scratch, 0, velSeekerX, velSeekerY, 0f)
    }

    fun rotate(angle: Float, centerX: Float, centerY: Float) {
        //Matrix.setIdentityM(engine.rotationMatrix, 0)
        ////Matrix.scaleM(com.bura.common.engine.rotationMatrix, 0, 0.5f,0.5f,0);//Fix scaling fixme
        //Matrix.translateM(engine.rotationMatrix, 0, centerX, centerY, 0f)
        //Matrix.rotateM(engine.rotationMatrix, 0, angle, 0f, 0f, 1f)
        //Matrix.translateM(engine.rotationMatrix, 0, -centerX, -centerY, 0f)
        //Matrix.multiplyMM(engine.scratch, 0, engine.vPMatrix, 0, engine.rotationMatrix, 0)
        ////Matrix.scaleM(com.bura.common.engine.rotationMatrix, 0, 1f,1f,0);//Fix scaling fixme

    }

    fun translateAndRotate(angle: Float, centerX: Float, centerY: Float, posX: Float, posY: Float) {
        //Matrix.setIdentityM(engine.rotationMatrix, 0)
        //Matrix.translateM(engine.rotationMatrix, 0, posX, posY, 0f)
        //Matrix.rotateM(engine.rotationMatrix, 0, angle, 0f, 0f, 1f)
        //Matrix.translateM(engine.rotationMatrix, 0, -centerX, -centerY, 0f)
        //Matrix.multiplyMM(engine.scratch, 0, engine.vPMatrix, 0, engine.rotationMatrix, 0)
    }

    fun restore() {
        //Stop any transformation for further com.bura.common.objects to take effect
        //Matrix.multiplyMM(engine.scratch, 0, engine.vPMatrix, 0, engine.viewMatrix, 0)
        //engine.scratch.multiply(engine.vPMatrix)
    }

    fun cameraRestore() {
        //Matrix.translateM(com.bura.common.engine.viewMatrix, 0, 0, 0, 0);
        //Matrix.multiplyMM(com.bura.common.engine.scratch, 0, com.bura.common.engine.projectionMatrix, 0, com.bura.common.engine.viewMatrix, 0);
    }
}
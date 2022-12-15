package com.bura.common.util

import com.bura.common.engine.Engine
import com.bura.common.objects.Shape
import kotlin.math.cos
import kotlin.math.sin

class MatrixUtil(private val engine: Engine) {
    fun translate(posX: Float, posY: Float) {
        //version 1
        //Matrix.setIdentityM(com.bura.common.engine.translationMatrix,0);
        //Matrix.translateM(com.bura.common.engine.translationMatrix, 0, posX, posY, 0);
        //Matrix.multiplyMM(com.bura.common.engine.scratch, 0, com.bura.common.engine.scratch,0, com.bura.common.engine.translationMatrix, 0);

        //version 2
        //Matrix4f.translateM(engine.scratch, 0, posX, posY, 0f)
        //engine.scratch = Matrix4f.translate(posX,posY,0f)
    }

    private var velSeekerX = 0f
    private var velSeekerY = 0f

    fun translateByAngle(angle: Float) {
        velSeekerX -= cos(angle).toFloat() * 0.01f
        velSeekerY -= sin(angle).toFloat() * 0.01f
        ///Matrix.translateM(engine.scratch, 0, velSeekerX, velSeekerY, 0f)
    }


    fun updateMatrix(shape: Shape, angle: Float) {
        Matrix4f.setIdentityM(shape.mModelMatrix, 0)
        Matrix4f.translateM(shape.mModelMatrix, 0, shape.centerX, shape.centerY, 0f)

        Matrix4f.setRotateM(
            shape.rotationMatrix, 0,
            angle,
            0f,0f,1f
        )
        shape.mTempMatrix = shape.mModelMatrix.clone()
        Matrix4f.multiply(shape.mModelMatrix, shape.mTempMatrix, shape.rotationMatrix)

        shape.mTempMatrix = engine.vPMatrix.clone()
        Matrix4f.multiply(engine.vPMatrix, shape.mTempMatrix, shape.mModelMatrix)
    }

    /*
     fun updateMatrix(shape: Shape, angle: Float) {
        Matrix4f.setIdentityM(shape.mModelMatrix, 0)
        Matrix4f.translateM(shape.mModelMatrix, 0, shape.centerX, shape.centerY, 0f)

        Matrix4f.setRotateM(
            shape.rotationMatrix, 0,
            angle,
            0f,0f,1f
        )
        shape.mTempMatrix = shape.mModelMatrix.clone()
        Matrix4f.multiply(shape.mModelMatrix, shape.mTempMatrix, shape.rotationMatrix)

        shape.mTempMatrix = engine.vPMatrix.clone()
        Matrix4f.multiply(engine.vPMatrix, shape.mTempMatrix, shape.mModelMatrix)
    }
     */

    /**
     * Stop further objects from taking effect of already applied matrix changes
     */
    fun restoreMatrix() {
        Matrix4f.multiply(engine.vPMatrix, engine.projectionMatrix, engine.viewMatrix)
    }
}
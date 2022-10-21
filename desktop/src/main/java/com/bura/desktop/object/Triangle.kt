package com.bura.desktop.`object`

import com.bura.desktop.engine.Engine
import com.bura.desktop.util.Constants
import org.lwjgl.opengles.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(
    engine: Engine,
    centerX: Float, centerY: Float,
    size: Float
) : Shape(engine, centerX, centerY) {

    var triangleVertices = floatArrayOf(
        centerX, centerY + 0.2f * size, 0.0f,  // top
        centerX - 0.2f * size, centerY - 0.1f * size, 0.0f,  // bottom left
        centerX + 0.2f * size, centerY - 0.1f * size, 0.0f // bottom right
    )

    override var color: FloatArray = floatArrayOf(0f, 0f, 0f)

    override var vertexCount = triangleVertices.size / Constants.COORDS_PER_VERTEX
    override var vertexData: FloatBuffer = ByteBuffer
        .allocateDirect(triangleVertices.size * Constants.BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(triangleVertices)
            position(0)
        }

    override fun draw() {
        GLES20.glUseProgram(engine.program)

        engine.aPositionLocation = GLES20.glGetAttribLocation(engine.program, Constants.A_POSITION).also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
               it, Constants.COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false, Constants.STRIDE, vertexData
            )

            engine.uColorLocation = GLES20.glGetUniformLocation(engine.program, Constants.U_COLOR).also { colorLocation ->
                GLES20.glUniform4fv(colorLocation, color)
            }
            //GLES20.glUniform4fv(engine.uColorLocation, color)

            //engine.uMatrixLocation = GLES20.glGetUniformLocation(engine.program, Constants.U_MATRIX)
            //GLES20.glUniformMatrix4fv(engine.uMatrixLocation,  false, engine.vPMatrix.toArray())

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            GLES20.glDisableVertexAttribArray(it)
        }
    }
}
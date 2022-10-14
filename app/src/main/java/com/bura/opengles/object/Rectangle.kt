package com.bura.opengles.`object`

import android.opengl.GLES20
import com.bura.opengles.engine.Engine
import com.bura.opengles.util.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Rectangle(engine: Engine, centerX: Float, centerY: Float, size: Float) : Shape(engine, centerX, centerY) {


    //square
    private val rectangleVertices = floatArrayOf(
        centerX - 0.2f * size, centerY + 0.2f * size, 0.0f,  // top left
        centerX - 0.2f * size, centerY - 0.2f * size, 0.0f,  // bottom left
        centerX + 0.2f * size, centerY - 0.2f * size, 0.0f,  // bottom right
        centerX + 0.2f * size, centerY + 0.2f * size, 0.0f // top right
    )

    override var vertexCount = rectangleVertices.size / Constants.COORDS_PER_VERTEX
    override var vertexData: FloatBuffer? = ByteBuffer
    .allocateDirect(rectangleVertices.size * Constants.BYTES_PER_FLOAT)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer().apply {
            put(rectangleVertices)
            position(0)
    }

    var drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    var drawListData: ShortBuffer? = ByteBuffer
        .allocateDirect(drawOrder.size * 2)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer().apply {
            put(drawOrder);
            position(0);
        }

    override fun draw() {
        GLES20.glUseProgram(engine.program)

        engine.aPositionLocation = GLES20.glGetAttribLocation(engine.program, Constants.A_POSITION)
        GLES20.glEnableVertexAttribArray(engine.aPositionLocation)
        GLES20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX, GLES20.GL_FLOAT,
            false, Constants.STRIDE, vertexData
        )

        engine.uColorLocation = GLES20.glGetUniformLocation(engine.program, Constants.U_COLOR)
        GLES20.glUniform4fv(engine.uColorLocation, 1, color, 0)

        engine.uMatrixLocation = GLES20.glGetUniformLocation(engine.program, Constants.U_MATRIX)
        GLES20.glUniformMatrix4fv(engine.uMatrixLocation, 1, false, engine.scratch, 0)

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListData
        )

        GLES20.glDisableVertexAttribArray(engine.aPositionLocation)
    }
}
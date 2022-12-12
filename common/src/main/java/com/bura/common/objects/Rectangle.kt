package com.bura.common.objects
import com.bura.common.engine.Engine
import com.bura.common.util.Constants
import com.bura.common.util.GLES20 as GLES20
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
        centerX + 0.2f * size, centerY + 0.2f * size, 0.0f   // top right
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
        Engine.gles20.glUseProgram(engine.program)

        engine.aPositionLocation = Engine.gles20.glGetAttribLocation(engine.program, Constants.A_POSITION)
        Engine.gles20.glEnableVertexAttribArray(engine.aPositionLocation)
        Engine.gles20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX, GLES20.GL_FLOAT,
            false, Constants.STRIDE, vertexData!!
        )

        engine.uColorLocation = Engine.gles20.glGetUniformLocation(engine.program, Constants.U_COLOR)
        Engine.gles20.glUniform4fv(engine.uColorLocation, color)

        engine.uMatrixLocation = Engine.gles20.glGetUniformLocation(engine.program, Constants.U_MATRIX)
        Engine.gles20.glUniformMatrix4fv(engine.uMatrixLocation,  false, engine.vPMatrix)

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        Engine.gles20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListData!!
        )

        Engine.gles20.glDisableVertexAttribArray(engine.aPositionLocation)
    }
}
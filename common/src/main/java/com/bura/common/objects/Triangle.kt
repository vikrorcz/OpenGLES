package com.bura.common.objects


import com.bura.common.util.Constants
import com.bura.common.util.GLES20
import com.bura.common.engine.Engine
import com.bura.common.engine.Engine.Companion.gles20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(
    var engine: Engine,
    centerX: Float, centerY: Float,
    size: Float
) {

    var triangleVertices = floatArrayOf(
        centerX, centerY + 0.2f * size, 0.0f,  // top
        centerX - 0.2f * size, centerY - 0.1f * size, 0.0f,  // bottom left
        centerX + 0.2f * size, centerY - 0.1f * size, 0.0f // bottom right
        //-0.5f, -0.5f, 0.0f,
        //0.5f, -0.5f, 0.0f,
        //0.0f,  0.5f, 0.0f,
        //+0.0f, +0.8f,    // Top coordinate
        //-0.8f, -0.8f,    // Bottom-left coordinate
        //+0.8f, -0.8f     // Bottom-right coordinate
    )



    var color: FloatArray = floatArrayOf(0f, 0f, 1f, 1f)

    var vertexCount = triangleVertices.size / Constants.COORDS_PER_VERTEX

    private var vertexData: FloatBuffer? = ByteBuffer
        .allocateDirect(triangleVertices.size * Constants.BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(triangleVertices)
            position(0)
        }


    fun draw() {

        gles20.glUseProgram(engine.program)

        engine.aPositionLocation = gles20.glGetAttribLocation(engine.program, "a_Position")

        gles20.glEnableVertexAttribArray(engine.aPositionLocation)
        gles20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX,  GLES20.GL_FLOAT,
            false, Constants.STRIDE, vertexData!!
        )

        engine.uColorLocation = gles20.glGetUniformLocation(engine.program, "u_Color")
        gles20.glUniform4fv(engine.uColorLocation, color)

        engine.uMatrixLocation = gles20.glGetUniformLocation(engine.program, Constants.U_MATRIX)
        gles20.glUniformMatrix4fv(engine.uMatrixLocation, false, engine.scratch)

        gles20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        gles20.glDisableVertexAttribArray(engine.aPositionLocation)

    }
}
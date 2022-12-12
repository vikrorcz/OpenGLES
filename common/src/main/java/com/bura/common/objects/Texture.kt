package com.bura.common.objects

import com.bura.common.engine.Engine
import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.util.Constants
import com.bura.common.util.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Texture(
    engine: Engine,
    x: Float, y: Float,
    width: Float, height: Float,
    private val resourceId: Int
) : Shape(engine, x, y) {

    private val rectangleVertices = floatArrayOf(
        x, y, 0.0f,  // top left
        x, y - 0.4f * height, 0.0f,  // bottom left
        x + 0.4f * width, y - 0.4f * height, 0.0f,  // bottom right
        x + 0.4f * width, y, 0.0f //top right
    )

    private val textureVertices = floatArrayOf(
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 0.0f
    )

    override var vertexData: FloatBuffer? =
       ByteBuffer
            .allocateDirect(rectangleVertices.size * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
            put(rectangleVertices)
            position(0)
    }

    private var textureData: FloatBuffer? =
        ByteBuffer
            .allocateDirect(textureVertices.size * Constants.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(textureVertices)
                position(0)
    }

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    private val drawListData: ShortBuffer? = ByteBuffer
    .allocateDirect(drawOrder.size * 2)
    .order(ByteOrder.nativeOrder())
    .asShortBuffer().apply {
            put(drawOrder)
            position(0)
    }

    override fun draw() {
        gles20.glUseProgram(engine.textureProgram)

        engine.uTextureLocation =
            gles20.glGetUniformLocation(engine.textureProgram, Constants.U_TEXTURE)
        engine.aPositionLocation =
            gles20.glGetAttribLocation(engine.textureProgram, Constants.A_POSITION)
        engine.aTextureLocation =
            gles20.glGetAttribLocation(engine.textureProgram, Constants.A_TEXTURE)
        engine.uMatrixLocation =
            gles20.glGetUniformLocation(engine.textureProgram, Constants.U_MATRIX)

        gles20.glUniform1i(engine.uTextureLocation, resourceId)

        gles20.glUniformMatrix4fv(engine.uMatrixLocation,false, engine.scratch)

        gles20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, Constants.STRIDE, vertexData!!
        )

        gles20.glVertexAttribPointer(
            engine.aTextureLocation, Constants.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, Constants.STRIDE, textureData!!
        )

        gles20.glEnableVertexAttribArray(engine.aPositionLocation)
        gles20.glEnableVertexAttribArray(engine.aTextureLocation)

        drawListData?.let {
            gles20.glDrawElements(
                GLES20.GL_TRIANGLES,
                drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT,
                it
            )
        }

        gles20.glDisableVertexAttribArray(engine.aPositionLocation)
        gles20.glDisableVertexAttribArray(engine.aTextureLocation)
    }
}
package com.bura.opengles.`object`

import android.opengl.GLES20
import com.bura.opengles.engine.Engine
import com.bura.opengles.util.Constants
import com.bura.opengles.util.TextureUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Texture(
    engine: Engine,
    x: Float, y: Float,
    width: Float, height: Float,
    resourceId: Int
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
            .order(java.nio.ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
            put(rectangleVertices)
            position(0)
    }

    private var textureData: FloatBuffer? =
        ByteBuffer
            .allocateDirect(textureVertices.size * Constants.BYTES_PER_FLOAT)
            .order(java.nio.ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(textureVertices)
                position(0)
    }

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    private val drawListData: ShortBuffer? = ByteBuffer
    .allocateDirect(drawOrder.size * 2)
    .order(java.nio.ByteOrder.nativeOrder())
    .asShortBuffer().apply {
            put(drawOrder)
            position(0)
    }

    private val glTextureId = TextureUtil.getGLTextureFromResourceId(resourceId)

    override fun draw() {
        GLES20.glUseProgram(engine.textureProgram)

        engine.uTextureLocation =
            GLES20.glGetUniformLocation(engine.textureProgram, Constants.U_TEXTURE)
        engine.aPositionLocation =
            GLES20.glGetAttribLocation(engine.textureProgram, Constants.A_POSITION)
        engine.aTextureLocation =
            GLES20.glGetAttribLocation(engine.textureProgram, Constants.A_TEXTURE)
        engine.uMatrixLocation =
            GLES20.glGetUniformLocation(engine.textureProgram, Constants.U_MATRIX)

        GLES20.glUniform1i(engine.uTextureLocation, glTextureId)

        GLES20.glUniformMatrix4fv(engine.uMatrixLocation, 1, false, engine.scratch, 0)

        GLES20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, Constants.STRIDE, vertexData
        )

        GLES20.glVertexAttribPointer(
            engine.aTextureLocation, Constants.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false, Constants.STRIDE, textureData
        )

        GLES20.glEnableVertexAttribArray(engine.aPositionLocation)
        GLES20.glEnableVertexAttribArray(engine.aTextureLocation)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListData
        )

        GLES20.glDisableVertexAttribArray(engine.aPositionLocation)
        GLES20.glDisableVertexAttribArray(engine.aTextureLocation)
    }
}
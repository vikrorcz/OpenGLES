package com.bura.desktop.util

import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.util.GLES20
import com.bura.common.util.TextureUtil
import com.bura.common.util.TextureUtil.Companion.textureHandle
import com.bura.desktop.util.IOUtils.Companion.ioResourceToByteBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage
import java.nio.IntBuffer


class LwjglTextureUtil: TextureUtil {
    override fun createTextures() {
        textureHandle[0] = loadTexture(TextureUtil.playerTextureLocation)
        textureHandle[1] = loadTexture(TextureUtil.joystickInnerTextureLocation)
        textureHandle[2] = loadTexture(TextureUtil.joystickOuterTextureLocation)

        gles20.glActiveTexture(GLES20.GL_TEXTURE0)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        gles20.glActiveTexture(GLES20.GL_TEXTURE1)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1])
        gles20.glActiveTexture(GLES20.GL_TEXTURE2)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[2])
    }


    override fun loadTexture(resourceLocation: String): Int {
        val textureHandle = IntArray(1)
        gles20.glGenTextures(1, textureHandle)

        if (textureHandle[0] != 0) {
            val width: IntBuffer = BufferUtils.createIntBuffer(1)
            val height: IntBuffer = BufferUtils.createIntBuffer(1)
            val components: IntBuffer = BufferUtils.createIntBuffer(1)
            val data = STBImage.stbi_load_from_memory(
                ioResourceToByteBuffer(
                    resourceLocation,
                    1024
                ), width, height, components, 4
            )

            gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

            gles20.glEnable(GLES20.GL_BLEND)
            gles20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

            gles20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
            gles20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            if (data != null) {
                gles20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA,
                    width.get(),
                    height.get(),
                    0,
                    GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,
                    data.asFloatBuffer()
                )
                STBImage.stbi_image_free(data)
            }
        }
        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }
        println("textureHandle" + textureHandle[0])
        return textureHandle[0]
    }
}
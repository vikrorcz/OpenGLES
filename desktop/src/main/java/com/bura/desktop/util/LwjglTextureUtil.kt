package com.bura.desktop.util

import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.util.GLES20
import com.bura.common.util.TextureUtil
import com.bura.common.util.TextureUtil.Companion.textureHandle
import com.bura.desktop.util.IOUtils.Companion.ioResourceToByteBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengles.GLES20.*
import org.lwjgl.stb.STBImage
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.IntBuffer
import javax.imageio.ImageIO


class LwjglTextureUtil: TextureUtil {
    override fun createTextures() {
        textureHandle[0] = loadTexture(TextureUtil.playerTextureLocation)
        textureHandle[1] = loadTexture(TextureUtil.joystickOuterTextureLocation)
        textureHandle[2] = loadTexture(TextureUtil.joystickInnerTextureLocation)

        gles20.glActiveTexture(GLES20.GL_TEXTURE0)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        gles20.glActiveTexture(GLES20.GL_TEXTURE1)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1])
        gles20.glActiveTexture(GLES20.GL_TEXTURE2)
        gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[2])
    }


    override fun loadTexture(resourceLocation: String): Int {
        val width: IntBuffer = BufferUtils.createIntBuffer(1)
        val height: IntBuffer = BufferUtils.createIntBuffer(1)
        val components: IntBuffer = BufferUtils.createIntBuffer(1)
        val data = STBImage.stbi_load_from_memory(
            ioResourceToByteBuffer(
                resourceLocation,
                1024
            ), width, height, components, 4
        )
        val textureHandle = IntArray(1)
        glGenTextures(textureHandle)
        if (textureHandle[0] != 0) {
            glBindTexture(GL_TEXTURE_2D, textureHandle[0])

            glEnable(GLES20.GL_BLEND)
            glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

            glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER,
                GL_LINEAR
            )
            glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER,
                GL_LINEAR
            )
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                width.get(),
                height.get(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            )
            if (data != null) {
                STBImage.stbi_image_free(data)
            }
        }

        return textureHandle[0]
    }

    private fun loadImage(loc: String): BufferedImage? {
        try {
            return ImageIO.read(javaClass.getResource(loc))
        } catch (e: IOException) {
            //Error Handling Here
        }
        return null
    }

}
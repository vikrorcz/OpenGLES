package com.bura.opengles.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLUtils
import androidx.core.graphics.get
import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.util.GLES20
import com.bura.common.util.TextureUtil
import com.bura.common.util.TextureUtil.Companion.joystickInnerTextureLocation
import com.bura.common.util.TextureUtil.Companion.joystickOuterTextureLocation
import com.bura.common.util.TextureUtil.Companion.playerTextureLocation
import com.bura.common.util.TextureUtil.Companion.textureHandle
import com.bura.opengles.R
import java.io.File
import java.nio.Buffer
import java.nio.IntBuffer


class AndroidTextureUtil(private val context: Context) : TextureUtil {
    override fun createTextures() {
        textureHandle[0] = loadTexture(R.drawable.player.toString())
        textureHandle[1] = loadTexture(R.drawable.joystick_outer.toString())
        textureHandle[2] = loadTexture(R.drawable.joystick_inner.toString())

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
            val options = BitmapFactory.Options()
            options.inScaled = false

            val bitmap = BitmapFactory.decodeResource(
                context.resources,
                resourceLocation.toInt(),
                options
            ) ?: return 0

            gles20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
            gles20.glEnable(GLES20.GL_BLEND)
            gles20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            gles20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
            )
            gles20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST
            )

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }
        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }
        return textureHandle[0]
    }
}
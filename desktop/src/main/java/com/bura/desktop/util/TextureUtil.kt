package com.bura.desktop.util

/*
import com.bura.desktop.engine.Engine
import org.lwjgl.opengles.GLES20

class TextureUtil(private val engine: Engine) {

    private val textureHandle = IntArray(3)

    fun createTextures() {
        //textureHandle[0] = loadTexture(R.drawable.player)
        //textureHandle[1] = loadTexture(R.drawable.joystick_outer)
        //textureHandle[2] = loadTexture(R.drawable.joystick_inner)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[2])
    }*/

    /*
    private fun loadTexture(resourceId: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
            )
            GLES20.glTexParameteri(
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

     */
/*
    companion object {
        //fun getGLTextureFromResourceId(resourceId: Int): Int {
        //    when (resourceId) {
        //        playerTextureLocation -> return 0
        //        joystickInnerTextureLocation -> return 1
        //        joystickOuterTextureLocation -> return 2
        //    }
        //    return -1
        //}

        const val path = "src\\main\\resources"
        const val playerTextureLocation = "$path\\player"
        const val joystickOuterTextureLocation = "$path\\player"
        const val joystickInnerTextureLocation = "$path\\player"
    }
}*/
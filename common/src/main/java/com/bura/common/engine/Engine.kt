package com.bura.common.engine

import com.bura.common.objects.Joystick
import com.bura.common.objects.Texture
import com.bura.common.objects.Triangle
import com.bura.common.util.*

class Engine {

    companion object {
        lateinit var gles20: GLES20
    }

    enum class DeviceType {
        ANDROID,
        DESKTOP
    }

    lateinit var endPoint: DeviceType

    lateinit var textureUtil: TextureUtil
    var matrixUtil = MatrixUtil(this)

    var program = -1
    var textureProgram = 1

    var aPositionLocation: Int = -1
    var uColorLocation = -1
    var uMatrixLocation = -1
    var uTextureLocation = -1
    var aTextureLocation = -1

    val vPMatrix = FloatArray(16)
    var projectionMatrix = FloatArray(16)
    var viewMatrix = FloatArray(16)

    var screenWidthPixel = 0
    var screenHeightPixel = 0

    var screenWidth = 0f //GL COORDS
    var screenHeight = 0f  //GL COORDS
    var screenTouchX = 0f //GL COORDS
    var screenTouchY = 0f //GL COORDS

    var cameraCenterX = 0f
    var cameraCenterY = 0f

    val fpsCounter = FPSCounter()

    var isTouched = false

    lateinit var triangle: Triangle
    lateinit var joystickLeft: Joystick
    lateinit var texture: Texture
    lateinit var texture2: Texture

    fun createObjects() {

        textureUtil.createTextures()

        triangle = Triangle(this, 0.5f,0f,1f)

        //texture = Texture(this, -1f, 0.2f, 1f, 1f, TextureUtil.playerTextureId)//   texture = Texture(this, -0.2f, 0.2f, 1f, 1f, TextureUtil.playerTextureId)
        texture = Texture(this, -0.2f, 0.2f, 1f, 1f, TextureUtil.playerTextureId)

        texture2 = Texture(this, -0.75f, 0.2f, 1f, 1f, TextureUtil.playerTextureId)

        joystickLeft = Joystick(this, 1)
    }
}
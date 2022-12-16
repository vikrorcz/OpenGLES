package com.bura.common.engine

import com.bura.common.gameobject.GameObject
import com.bura.common.gameobject.Player
import com.bura.common.objects.Joystick
import com.bura.common.objects.Texture
import com.bura.common.objects.Triangle
import com.bura.common.util.*

class Engine(val endPoint: DeviceType) {

    companion object {
        lateinit var gles20: GLES20
    }

    enum class DeviceType {
        ANDROID,
        DESKTOP
    }

    //lateinit var endPoint: DeviceType

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

    //var screenWidth = 0f //GL COORDS
    //var screenHeight = 0f  //GL COORDS
    var screenTouchX = 0f //GL COORDS
    var screenTouchY = 0f //GL COORDS

    val fpsCounter = FPSCounter()

    var isTouched = false

    lateinit var triangle: Triangle
    lateinit var joystickLeft: Joystick
    lateinit var texture2: Texture
    lateinit var player: Player

    /**
     *  Desktop monitors are too big, therefore scale scaleFactor
     *  scales down the content for better experience only on desktop devices
     */
    val scaleFactor = if (endPoint == DeviceType.DESKTOP) 0.75f else 1f//screenWidth / 4//1920
    //val scaleWidth = screenWidth / 4
    //val scaleHeight = screenHeight / 2//1080



    var gameObjectArrayList = ArrayList<GameObject>()

    fun createObjects() {

        textureUtil.createTextures()

        triangle = Triangle(this, 0.5f,0f,1f)

        player = Player(this, -0.2f, 0.2f)
        gameObjectArrayList.add(player)

        texture2 = Texture(this, -0.75f, 0.2f, 1f, 1f, TextureUtil.playerTextureId)

        joystickLeft = Joystick(this, 1)
    }
}
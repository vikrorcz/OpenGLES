package com.bura.opengles.engine

import android.content.Context
import com.bura.opengles.R
import com.bura.opengles.`object`.Joystick
import com.bura.opengles.`object`.Texture
import com.bura.opengles.util.*

class Engine(val context: Context) {

    var matrixUtil = MatrixUtil(this)

    var program = 0
    var textureProgram = 1
    var uColorLocation = -1
    var aPositionLocation = -1
    var aTextureLocation = -1
    var uTextureLocation = -1
    var uMatrixLocation = -1

    val vPMatrix = FloatArray(16)
    val projectionMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)

    val rotationMatrix = FloatArray(16)
    val translationMatrix = FloatArray(16)
    val scratch = FloatArray(16)

    var screenWidthPixel = 0
    var screenHeightPixel = 0

    var screenWidth = 0f //GL COORDS
    var screenHeight = 0f  //GL COORDS
    var screenTouchX = 0f //GL COORDS
    var screenTouchY = 0f //GL COORDS

    var cameraCenterX = 0f
    var cameraCenterY = 0f

    var isTouched = false

    val fpsCounter = FPSCounter()
    private val textureUtil = TextureUtil(this)

    lateinit var texture: Texture
    lateinit var joystickLeft: Joystick
    lateinit var joystickRight: Joystick

    fun createShaders() {
        val vertexShaderSource: String = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource: String = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_fragment_shader)

        val textureVertexSource: String = TextResourceReader
            .readTextFileFromResource(context, R.raw.texture_vertex_shader)
        val textureFragmentSource: String = TextResourceReader
            .readTextFileFromResource(context, R.raw.texture_fragment_shader)

        val vertexShader: Int = ShaderUtil.compileVertexShader(vertexShaderSource)
        val fragmentShader: Int = ShaderUtil.compileFragmentShader(fragmentShaderSource)

        val textureFragmentShader: Int = ShaderUtil.compileFragmentShader(textureFragmentSource)
        val textureVertexShader: Int = ShaderUtil.compileVertexShader(textureVertexSource)

        program = ShaderUtil.linkProgram(vertexShader, fragmentShader)
        textureProgram = ShaderUtil.linkProgram(textureVertexShader, textureFragmentShader)

        if (Constants.LOGGER_ON) {
            ShaderUtil.validateProgram(program)
            ShaderUtil.validateProgram(textureProgram)
        }
    }

    fun createObjects() {
        textureUtil.createTextures()
        texture = Texture(this, -0.2f, 0.2f, 1f, 1f, TextureUtil.playerTextureLocation)
        joystickLeft = Joystick(this, 1)
    }
}
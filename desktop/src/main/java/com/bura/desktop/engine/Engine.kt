package com.bura.desktop.engine

import com.bura.desktop.util.*
import org.lwjgl.opengles.GLES20

class Engine {
    var program: Int = -1
    var textureProgram = -1
    var uColorLocation: Int = -1
    var aPositionLocation: Int = -1
    var aTextureLocation = -1
    var uTextureLocation = -1
    var uMatrixLocation = -1

    //val vPMatrix = FloatArray(16)
    //projectionMatrix = FloatArray(16)
    //viewMatrix = FloatArray(16)
    //rotationMatrix = FloatArray(16)
    //translationMatrix = FloatArray(16)
    //scratch = FloatArray(16)

    val vPMatrix = Matrix4f()
    lateinit var projectionMatrix: Matrix4f
    val viewMatrix = Matrix4f()
    val rotationMatrix = Matrix4f()
    val translationMatrix = Matrix4f()
    val scratch = Matrix4f()


    var screenWidthPixel = 0
    var screenHeightPixel = 0

    var screenWidth = 0f //GL COORDS
    var screenHeight = 0f  //GL COORDS
    var screenTouchX = 0f //GL COORDS
    var screenTouchY = 0f //GL COORDS

    var cameraCenterX = 0f
    var cameraCenterY = 0f

    var isTouched = false

    fun createShaders() {
        val path = "desktop\\src\\main\\assets"
        val vertexShaderSource: String = TextResourceReader
            .readTextFileFromResource("$path\\simple_vertex_shader.glsl")//(R.raw.simple_vertex_shader)

        val fragmentShaderSource: String = TextResourceReader
            .readTextFileFromResource("$path\\simple_fragment_shader.glsl")//(R.raw.simple_fragment_shader)

        val testingShader = TestingShader()
        val vertexShader = testingShader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShader = testingShader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)

        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
        println("generated program $program")
    }
}
package com.bura.opengles.engine

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.bura.common.engine.Engine
import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.engine.MyRenderer
import com.bura.common.util.Constants
import com.bura.common.util.MathUtil
import com.bura.common.util.Matrix4f
import com.bura.opengles.R
import com.bura.opengles.util.AndroidGles20
import com.bura.opengles.util.AndroidTextureUtil
import com.bura.opengles.util.ShaderUtil
import com.bura.opengles.util.TextResourceReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.pow
import kotlin.math.sqrt

class MyRenderer(private val context: Context): GLSurfaceView.Renderer {
    private var engine = Engine(Engine.DeviceType.ANDROID).also {
        gles20 = AndroidGles20()
        it.textureUtil = AndroidTextureUtil(context)
    }

    private val myRenderer = MyRenderer(engine)

    @Volatile
    var isTouching = false

    @Volatile
    var touchX = 0f

    @Volatile
    var touchY = 0f

    @Volatile
    var currentMotionEvent = -1

    private fun createShaders() {
        val vertexShaderSource: String = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader) //todo change to common file location
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

        engine.program = ShaderUtil.linkProgram(vertexShader, fragmentShader)
        engine.textureProgram = ShaderUtil.linkProgram(textureVertexShader, textureFragmentShader)

        if (Constants.LOGGER_ON) {
            ShaderUtil.validateProgram(engine.program)
            ShaderUtil.validateProgram(engine.textureProgram)
        }
    }

    override fun onSurfaceCreated(unused: GL10, eglConfig: EGLConfig) {
        createShaders()
        engine.createObjects()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        gles20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height
        engine.screenWidthPixel = width
        engine.screenHeightPixel = height
        //engine.screenWidth = ratio
        //engine.screenHeight = ratio
        Matrix4f.frustum(engine.projectionMatrix, -ratio, ratio, -1f, 1f, 3f, 7f)
        for (i in 0 until engine.projectionMatrix.size) {
            println("projectionMatrix= " + "[" + i + "]" + engine.projectionMatrix[i])
        }
    }

    override fun onDrawFrame(unused: GL10) {
        inputUpdate()
        myRenderer.draw()
    }

    private fun inputUpdate() {
        when (currentMotionEvent) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                engine.screenTouchX = touchX / engine.screenWidthPixel * 4
                engine.screenTouchX -= 2
                engine.screenTouchY = touchY / engine.screenHeightPixel * 2
                engine.screenTouchY = -engine.screenTouchY + 1

                val cx: Float = engine.joystickLeft.centerX
                val cy: Float = engine.joystickLeft.centerY
                val deltaX = engine.screenTouchX - cx
                val deltaY = engine.screenTouchY - cy

                val dif = engine.joystickLeft.outerWidth / 8

                val distance = sqrt(
                    (deltaX - dif).toDouble().pow(2.0) + (deltaY + dif).toDouble().pow(2.0)
                ).toFloat()

                if (engine.joystickLeft.isTouched) {
                    val angle: Double = MathUtil.getAngle(
                        cx + dif,
                        cy - dif,
                        engine.screenTouchX,
                        engine.screenTouchY
                    )
                    engine.joystickLeft.angle = angle.toFloat()
                }

                if (distance <= Constants.JOYSTICK_AREA) {
                    engine.joystickLeft.isTouched = true
                    engine.joystickLeft.actuatorX = deltaX - dif
                    engine.joystickLeft.actuatorY = deltaY + dif
                } else {
                    //out of bounds
                    engine.joystickLeft.actuatorX = (deltaX - dif) / distance * Constants.JOYSTICK_AREA
                    engine.joystickLeft.actuatorY  = (deltaY + dif) / distance * Constants.JOYSTICK_AREA
                }
            }
            MotionEvent.ACTION_UP -> engine.joystickLeft.isTouched = false
        }
    }
}
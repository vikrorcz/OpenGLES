package com.bura.opengles.engine

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import android.view.MotionEvent
import com.bura.opengles.`object`.Rectangle
import com.bura.opengles.`object`.Triangle
import com.bura.opengles.util.Constants
import com.bura.opengles.util.MathUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.pow
import kotlin.math.sqrt

class MyRenderer(context: Context): GLSurfaceView.Renderer {
    private var engine: Engine = Engine(context)

    @Volatile
    var isTouching = false
    set(value) {
        engine.isTouched = value
        field = value
    }

    @Volatile
    var touchX = 0f

    @Volatile
    var touchY = 0f

    @Volatile
    var currentMotionEvent = -1

    private var rectangle = Rectangle(engine, 0f, 0f, 2f).apply {
        color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    }
    private var triangle = Triangle(engine, 0f, 0f, 1f).apply {
        color = floatArrayOf(0.83671875f, 0.26953125f, 0.22265625f, 1.0f)
    }
    private var seeker = Rectangle(engine, 0f, 0f, 2f).apply {
        color = floatArrayOf(0.4453671875f, 0.556953125f, 0.8742265625f, 1.0f)
    }

    override fun onSurfaceCreated(unused: GL10, eglConfig: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        engine.createShaders()
        engine.createObjects()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height
        println("ratio= $ratio")
        engine.screenWidthPixel = width
        engine.screenHeightPixel = height
        engine.screenWidth = ratio * 2
        engine.screenHeight = ratio
        println("ScreenWidth: " + engine.screenWidth + "ScreenHeight: " + engine.screenHeight)
        Matrix.frustumM(engine.projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(unused: GL10) {
        engine.fpsCounter.logFrame()
        inputUpdate()

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.setLookAtM(engine.viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(engine.vPMatrix, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)

        val time = SystemClock.uptimeMillis() % 4000L
        val angle = 0.090f * time.toInt()


        engine.matrixUtil.cameraTranslate(engine.joystickLeft.angle.toDouble()) //->

        engine.matrixUtil.rotate(angle, rectangle.centerX, rectangle.centerY)
        rectangle.draw()
        engine.matrixUtil.restore()

        engine.matrixUtil.rotate(angle, rectangle.centerX, rectangle.centerY)
        triangle.draw()
        engine.matrixUtil.restore()

        engine.matrixUtil.translateByAngle(
            MathUtil.getAngle(
                seeker.centerX,
                seeker.centerY,
                engine.cameraCenterX,
                engine.cameraCenterY,
            ).toFloat()
        )
        //engine.matrixUtil.translate(engine.cameraCenterX, engine.cameraCenterY)
        seeker.draw()
        engine.matrixUtil.restore()
        //<-

        //<-
        engine.matrixUtil.withCameraTranslate() //->

        engine.matrixUtil.translateAndRotate(
            Math.toDegrees(engine.joystickLeft.angle.toDouble()).toFloat() + 90,
            0f,
            0f,
            triangle.centerX,
            triangle.centerY
        )
        engine.texture.draw()
        engine.matrixUtil.restore()

        engine.matrixUtil.translate(engine.cameraCenterX, engine.cameraCenterY)
        engine.joystickLeft.draw()
        engine.matrixUtil.restore()
        //<-
    }

    private fun inputUpdate() {
        when (currentMotionEvent) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                engine.screenTouchX = touchX / engine.screenWidthPixel * 8
                engine.screenTouchX -= 4
                engine.screenTouchY = touchY / engine.screenHeightPixel * 4
                engine.screenTouchY = -engine.screenTouchY + 2
                val cx: Float = engine.joystickLeft.centerX
                val cy: Float = engine.joystickLeft.centerY
                val deltaX = engine.screenTouchX - cx
                val deltaY = engine.screenTouchY - cy
                val distance = sqrt(
                    (deltaX - 0.25f).toDouble().pow(2.0) + (deltaY + 0.25f).toDouble().pow(2.0)
                ).toFloat()

                if (engine.joystickLeft.isTouched) {
                    val angle: Double = MathUtil.getAngle(
                        cx + 0.25f,
                        cy - 0.25f,
                        engine.screenTouchX,
                        engine.screenTouchY
                    )
                    engine.joystickLeft.angle = angle.toFloat()
                }


                if (distance <= Constants.JOYSTICK_AREA) {
                    engine.joystickLeft.isTouched = true
                    engine.joystickLeft.actuatorX = deltaX - 0.25f
                    engine.joystickLeft.actuatorY = deltaY + 0.25f
                } else {
                    //out of bounds
                    engine.joystickLeft.actuatorX = (deltaX - 0.25f) / distance * Constants.JOYSTICK_AREA
                    engine.joystickLeft.actuatorY  = (deltaY + 0.25f) / distance * Constants.JOYSTICK_AREA
                }
                triangle.centerX = engine.cameraCenterX
                triangle.centerY = engine.cameraCenterY
            }
            MotionEvent.ACTION_UP -> engine.joystickLeft.isTouched = false
        }
    }
}
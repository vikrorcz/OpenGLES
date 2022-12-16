package com.bura.desktop

import com.bura.common.engine.Engine
import com.bura.common.engine.Engine.Companion.gles20
import com.bura.common.engine.MyRenderer
import com.bura.common.gameobject.Bullet
import com.bura.common.util.MathUtil
import com.bura.common.util.Matrix4f
import com.bura.desktop.util.Input
import com.bura.desktop.util.LwjglGles20
import com.bura.desktop.util.LwjglTextureUtil
import com.bura.desktop.util.ShaderUtil
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengles.GLES
import org.lwjgl.system.Configuration
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.DoubleBuffer
import kotlin.math.cos
import kotlin.math.sin


class WindowUtil {
    private var window: Long = 0
    private lateinit var input: Input
    private val screenWidthPixel = 1920 //  1480  |  2280   |  2960
    private val screenHeightPixel = 1080 // 720   |  1080   |  1440

    private val engine = Engine(Engine.DeviceType.DESKTOP).also {
        gles20 = LwjglGles20()
        it.textureUtil = LwjglTextureUtil()
    }

    private val myRenderer = MyRenderer(engine)

    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize")
        }
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)

        //Use OpenGLES
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2)//use OpenGLES 2.0, for 3.0 use 3
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0)
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_ES_API)


        window = GLFW.glfwCreateWindow(
            screenWidthPixel,
            screenHeightPixel,
            "OpenGLES",
            MemoryUtil.NULL,
            MemoryUtil.NULL
        )



        //fullscreen
        window = GLFW.glfwCreateWindow(screenWidthPixel, screenHeightPixel, "OpenGLES", GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()) ?: return

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        //init key callbacks
        input = Input()
        GLFW.glfwSetKeyCallback(window, input)

        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSwapInterval(1)//Enable V-Sync
        GLFW.glfwShowWindow(window)

        Configuration.OPENGLES_EXPLICIT_INIT.set(true)
        GL.getFunctionProvider()?.let { GLES.create(it) }
        GLES.createCapabilities()

        gles20.glViewport(0, 0, screenWidthPixel, screenHeightPixel)
        val ratio = screenWidthPixel.toFloat() / screenHeightPixel
        val shaderUtil = ShaderUtil(engine)
        shaderUtil.createProgram()
        shaderUtil.createTextureProgram()

        engine.screenWidthPixel = screenWidthPixel
        engine.screenHeightPixel = screenHeightPixel
        //engine.screenWidth = ratio * 2
        //engine.screenHeight = ratio
        Matrix4f.frustum(engine.projectionMatrix, -ratio, ratio, -1f, 1f, 3f, 7f)
        engine.createObjects()
    }

    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents()
            inputUpdate()
            myRenderer.draw()
            GLFW.glfwSwapBuffers(window)
        }
    }

    private var cursorX: Double = 0.0
    private var cursorY: Double = 0.0
    private val xBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)
    private val yBuffer: DoubleBuffer = BufferUtils.createDoubleBuffer(1)

    private fun inputUpdate() {
        GLFW.glfwGetCursorPos(window, xBuffer, yBuffer)
        cursorX = xBuffer.get(0)
        cursorY = yBuffer.get(0)

        engine.screenTouchX = (cursorX / engine.screenWidthPixel * 4).toFloat()
        engine.screenTouchX -= 2
        engine.screenTouchY = (cursorY / engine.screenHeightPixel * 2).toFloat()
        engine.screenTouchY = -engine.screenTouchY + 1

        if (Input.keys[GLFW.GLFW_KEY_W] || Input.keys[GLFW.GLFW_KEY_UP]) {
            engine.player.centerY += 0.01f
        }

        if (Input.keys[GLFW.GLFW_KEY_S] || Input.keys[GLFW.GLFW_KEY_DOWN]) {
            engine.player.centerY -= 0.01f
        }

        if (Input.keys[GLFW.GLFW_KEY_A] || Input.keys[GLFW.GLFW_KEY_LEFT]) {
            engine.player.centerX -= 0.01f
        }

        if (Input.keys[GLFW.GLFW_KEY_D] || Input.keys[GLFW.GLFW_KEY_RIGHT]) {
            engine.player.centerX += 0.01f
        }
        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS ){
            val x = engine.player.centerX
            val y = engine.player.centerY + 0.05f//* engine.scaleFactor//+ 0.05f * engine.scaleFactor

            val angle = MathUtil.getAngle(
                engine.player.centerX, engine.player.centerY, engine.screenTouchX, engine.screenTouchY
            ).toFloat()

            val offsetX = 0f
            val offsetY = 0.2f
            val bulletX = x + (offsetX * cos(angle) - offsetY * sin(angle))
            val bulletY = y + (offsetX * sin(angle) + offsetY * cos(angle));

            val bullet1 = Bullet(engine, bulletX, bulletY).also {
                it.angle = angle
            }

            val offsetY2 = 0.1f
            val bulletX2 = x + (offsetX * cos(angle) - offsetY2 * sin(angle))
            val bulletY2 = y + (offsetX * sin(angle) + offsetY2 * cos(angle));
            val bullet2 = Bullet(engine, bulletX2, bulletY2).also {
                it.angle = angle
            }

            val offsetY3 = -0.1f
            val bulletX3 = x + (offsetX * cos(angle) - offsetY3 * sin(angle))
            val bulletY3 = y + (offsetX * sin(angle) + offsetY3 * cos(angle));
            val bullet3 = Bullet(engine, bulletX3, bulletY3).also {
                it.angle = angle
            }

            val offsetY4 = -0.2f
            val bulletX4 = x + (offsetX * cos(angle) - offsetY4 * sin(angle))
            val bulletY4 = y + (offsetX * sin(angle) + offsetY4 * cos(angle));
            val bullet4 = Bullet(engine, bulletX4, bulletY4).also {
                it.angle = angle
            }

            engine.gameObjectArrayList.add(bullet1)
            engine.gameObjectArrayList.add(bullet2)
            engine.gameObjectArrayList.add(bullet3)
            engine.gameObjectArrayList.add(bullet4)
            
        }
    }

    fun setup() {
        init()
        loop()

        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)

        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }
}
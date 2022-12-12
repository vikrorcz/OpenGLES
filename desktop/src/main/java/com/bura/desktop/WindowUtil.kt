package com.bura.desktop

import com.bura.common.engine.Engine
import com.bura.common.engine.MyRenderer
import com.bura.common.util.Matrix4f
import com.bura.desktop.util.Input
import com.bura.desktop.util.LwjglGles20
import com.bura.desktop.util.LwjglTextureUtil
import com.bura.desktop.util.ShaderUtil
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengles.GLES
import org.lwjgl.opengles.GLES20
import org.lwjgl.system.Configuration
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class WindowUtil {
    private var window: Long = 0
    private lateinit var input: Input
    private val screenWidthPixel = 1480
    private val screenHeightPixel = 720

    private val engine = Engine().also {
        Engine.gles20 = LwjglGles20()
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

        //Use OPENGLES
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2)//3
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0)
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_ES_API)

        window = GLFW.glfwCreateWindow(
            screenWidthPixel,
            screenHeightPixel,
            "OpenGLES",
            MemoryUtil.NULL,
            MemoryUtil.NULL
        )

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        //init key callbacks
        input = Input()
        glfwSetKeyCallback(window, input)

        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSwapInterval(1)//Enable V-Sync
        GLFW.glfwShowWindow(window)

        Configuration.OPENGLES_EXPLICIT_INIT.set(true)
        GL.getFunctionProvider()?.let { GLES.create(it) }
        GLES.createCapabilities()
        GLES20.glViewport(0, 0, screenWidthPixel, screenHeightPixel)
        val ratio = screenWidthPixel.toFloat() / screenHeightPixel

        val shaderUtil = ShaderUtil(engine)
        shaderUtil.createProgram()
        shaderUtil.createTextureProgram()

        Matrix4f.frustum(engine.projectionMatrix, -ratio, ratio, -1f, 1f, 3f, 7f)

        engine.textureUtil = LwjglTextureUtil()
        engine.createObjects()
    }

    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            inputUpdate()

            GLFW.glfwPollEvents()

            myRenderer.draw()

            GLFW.glfwSwapBuffers(window)
        }

    }

    var posX = 0f
    var posY = 0f

    private fun inputUpdate() {
        if (Input.keys[GLFW.GLFW_KEY_W] || Input.keys[GLFW.GLFW_KEY_UP]) {
            //redColor += 0.01f
            posY += 0.01f
            //engine.scratch = Matrix4f.translateM()
            Matrix4f.translateM(engine.scratch, 0, posY,posY,0f)
            println("W")
        }

        if (Input.keys[GLFW.GLFW_KEY_S] || Input.keys[GLFW.GLFW_KEY_DOWN]) {
            //redColor -= 0.01f
            posY -= 0.01f
            Matrix4f.translateM(engine.scratch, 0, posX, posY, 0f)
        }

        if (Input.keys[GLFW.GLFW_KEY_A] || Input.keys[GLFW.GLFW_KEY_LEFT]) {
            posX -= 0.01f
            Matrix4f.translateM(engine.scratch, 0, posX, posY, 0f)
        }

        if (Input.keys[GLFW.GLFW_KEY_D] || Input.keys[GLFW.GLFW_KEY_RIGHT]) {
            posX += 0.01f
            Matrix4f.translateM(engine.scratch, 0, posX, posY, 0f)
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
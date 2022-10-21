package com.bura.desktop

import com.bura.desktop.`object`.Triangle
import com.bura.desktop.engine.Engine
import com.bura.desktop.util.Input
import com.bura.desktop.util.Matrix4f
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

    var redColor = 1f
    lateinit var engine: Engine
    lateinit var triangle: Triangle


    private fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!GLFW.glfwInit()) {
            throw  IllegalStateException("Unable to initialize")
        }

        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)

        //Use OPENGLES
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2)//3
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0)
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_ES_API)

        window = GLFW.glfwCreateWindow(screenWidthPixel, screenHeightPixel, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL)

        //glfwSetKeyCallback(window, (window, key))
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

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

        Configuration.OPENGLES_EXPLICIT_INIT.set(true);
        GLES.create(GL.getFunctionProvider()!!)

        GLES.createCapabilities()
        GLES20.glViewport(0, 0, screenWidthPixel, screenHeightPixel)
        val ratio: Float = screenWidthPixel.toFloat() / screenHeightPixel
        engine = Engine()
        engine.projectionMatrix = Matrix4f.frustum(-ratio, ratio, -1f, 1f, 3f, 7f)//working properly
        val projectionMArray = engine.projectionMatrix.toArray()
        for (i in projectionMArray.indices) {
            println("engine.projectionMatrix[$i] = ${projectionMArray[i]}")
        }
        //Matrix.frustumM(engine.projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        engine.createShaders()

        triangle = Triangle(engine, 0f, 0f,1f).apply {
            color = floatArrayOf(0f,0f,0f)
        }
        println("triangle color = ${triangle.color[2]}")
    }

    private fun loop() {
        //GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

        while (!GLFW.glfwWindowShouldClose(window)) {
            //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT) // clear the framebuffer
            inputUpdate()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            engine.viewMatrix.setLookAt(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)//nb not working properly
            val viewMatrixArray = engine.vPMatrix.toArray()
            for (i in viewMatrixArray.indices) {
                println("engine.viewMatrixArray[$i] = ${viewMatrixArray[i]}")
            }
            //Matrix.multiplyMM(engine.vPMatrix, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)
            //engine.projectionMatrix.add(engine.viewMatrix)
            engine.vPMatrix.multiply(engine.projectionMatrix)

            val vPMatrixArray = engine.vPMatrix.toArray()
            for (i in vPMatrixArray.indices) {
                println("engine.vPMatrixArray[$i] = ${vPMatrixArray[i]}")
            }
            //engine.projectionMatrix.multiply(engine.viewMatrix)
           // engine.viewMatrix.setLookAt()
           // engine.vPMatrix.multiply(engine.projectionMatrix)
           //Matrix4f.setLookAtM(engine.viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
           //Matrix4f.multiplyMM(engine.vPMatrix, 0, engine.projectionMatrix, 0, engine.viewMatrix, 0)


            GLES20.glClearColor(redColor, 0f, 0f, 0f)
            triangle.draw()

            GLFW.glfwSwapBuffers(window) // swap the color buffers
            GLFW.glfwPollEvents()

        }
    }

    fun draw() {

    }

    private fun inputUpdate() {
        if (Input.keys[GLFW.GLFW_KEY_W] || Input.keys[GLFW.GLFW_KEY_UP]) {
            redColor += 0.01f
            print("MOVE UP")
        }

        if (Input.keys[GLFW.GLFW_KEY_S] || Input.keys[GLFW.GLFW_KEY_DOWN]) {
            redColor -= 0.01f
            print("MOVE DOWN")
        }
    }

    fun setup() {
        init()
        loop()

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }
}
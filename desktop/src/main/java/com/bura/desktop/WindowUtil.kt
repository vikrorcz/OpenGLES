
import com.bura.common.util.Matrix4f
import com.bura.desktop.util.*
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
import engine.Engine
import objects.Triangle


class WindowUtil {
    private var window: Long = 0
    private lateinit var input: Input
    private val screenWidthPixel = 1480
    private val screenHeightPixel = 720

    private var redColor = 1.0f

    private val engine = Engine(LwjglGles20())
    private val triangle = Triangle(engine, 0f,0f,1f)

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

        window =
            GLFW.glfwCreateWindow(screenWidthPixel, screenHeightPixel, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL)

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

        //engine.createShaders()
        val path = "common\\resources"
        val vertexShaderSource = TextResourceReader.readTextFileFromResource("$path\\simple_vertex_shader.glsl")
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource("$path\\simple_fragment_shader.glsl")


        val vertexShader = TestingShader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShader = TestingShader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)
        engine.program = TestingShader.linkColor(vertexShader, fragmentShader)
        val ratio: Float = screenWidthPixel.toFloat() / screenHeightPixel
        engine.projectionMatrix = Matrix4f.frustum(-ratio, ratio, -1f, 1f, 3f, 7f)
    }

    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            inputUpdate()
            draw()

            GLFW.glfwSwapBuffers(window) // swap the color buffers
            GLFW.glfwPollEvents()
        }

    }

    private fun draw() {
        engine.viewMatrix.setLookAt(0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        engine.projectionMatrix.multiply(engine.viewMatrix)
        engine.vPMatrix.multiply(engine.projectionMatrix)
        GLES20.glClearColor(redColor, 0f, 0f, 0f)
        triangle.draw()
    }

    var posX = 0f
    var posY = 0f

    private fun inputUpdate() {
        if (Input.keys[GLFW.GLFW_KEY_W] || Input.keys[GLFW.GLFW_KEY_UP]) {
            //redColor += 0.01f
            posY += 0.01f
            engine.scratch = Matrix4f.translate(posX, posY, 0f)
        }

        if (Input.keys[GLFW.GLFW_KEY_S] || Input.keys[GLFW.GLFW_KEY_DOWN]) {
            //redColor -= 0.01f
            posY -= 0.01f
            engine.scratch = Matrix4f.translate(posX, posY, 0f)
        }

        if (Input.keys[GLFW.GLFW_KEY_A] || Input.keys[GLFW.GLFW_KEY_LEFT]) {
            posX -= 0.01f
            engine.scratch = Matrix4f.translate(posX, posY, 0f)
        }

        if (Input.keys[GLFW.GLFW_KEY_D] || Input.keys[GLFW.GLFW_KEY_RIGHT]) {
            posX += 0.01f
            engine.scratch = Matrix4f.translate(posX, posY, 0f)
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
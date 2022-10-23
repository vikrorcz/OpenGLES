package engine

import com.bura.common.util.GLESUtil
import com.bura.common.util.Matrix4f

class Engine(val GLES20: GLESUtil) {



    var program = -1
    var textureProgram = 1

    var aPositionLocation: Int = -1
    var uColorLocation = -1
    var uMatrixLocation = -1

    val vPMatrix = Matrix4f()
    lateinit var projectionMatrix: Matrix4f
    var viewMatrix = Matrix4f()
    var rotationMatrix = Matrix4f()
    var translationMatrix = Matrix4f()
    var scratch = Matrix4f()

    //lateinit var GLES20: GLESUtil
/*
    fun createShaders() {
        val path = "common\\resources"
        val vertexShaderSource = TextResourceReader.readTextFileFromResource("$path\\simple_vertex_shader.glsl")
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource("$path\\simple_fragment_shader.glsl")


        val vertexShader = TestingShader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShader = TestingShader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource)
        program = TestingShader.linkColor(vertexShader, fragmentShader)
    }

 */
}
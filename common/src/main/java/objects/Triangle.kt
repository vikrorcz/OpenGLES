package objects


import com.bura.common.util.Constants
import com.bura.common.util.GLESUtil
import engine.Engine
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(
    var engine: Engine,
    centerX: Float, centerY: Float,
    size: Float
) {

    var triangleVertices = floatArrayOf(
        centerX, centerY + 0.2f * size, 0.0f,  // top
        centerX - 0.2f * size, centerY - 0.1f * size, 0.0f,  // bottom left
        centerX + 0.2f * size, centerY - 0.1f * size, 0.0f // bottom right
        //-0.5f, -0.5f, 0.0f,
        //0.5f, -0.5f, 0.0f,
        //0.0f,  0.5f, 0.0f,
        //+0.0f, +0.8f,    // Top coordinate
        //-0.8f, -0.8f,    // Bottom-left coordinate
        //+0.8f, -0.8f     // Bottom-right coordinate
    )



    var color: FloatArray = floatArrayOf(0f, 0f, 1f, 1f)

    var vertexCount = triangleVertices.size / Constants.COORDS_PER_VERTEX

    private var vertexData: FloatBuffer? = ByteBuffer
        .allocateDirect(triangleVertices.size * Constants.BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply {
            put(triangleVertices)
            position(0)
        }



    fun draw() {

        engine.GLES20.glUseProgram(engine.program)

        engine.aPositionLocation = engine.GLES20.glGetAttribLocation(engine.program, "a_Position")

        engine.GLES20.glEnableVertexAttribArray(engine.aPositionLocation)
        engine.GLES20.glVertexAttribPointer(
            engine.aPositionLocation, Constants.COORDS_PER_VERTEX,  GLESUtil.GL_FLOAT,
            false, Constants.STRIDE, vertexData!!
        )

        engine.uColorLocation = engine.GLES20.glGetUniformLocation(engine.program, "u_Color")
        engine.GLES20.glUniform4fv(engine.uColorLocation, color)

        engine.uMatrixLocation = engine.GLES20.glGetUniformLocation(engine.program, Constants.U_MATRIX)
        engine.GLES20.glUniformMatrix4fv(engine.uMatrixLocation,  false, engine.scratch.toArray())

        engine.GLES20.glDrawArrays(GLESUtil.GL_TRIANGLES, 0, vertexCount)

        engine.GLES20.glDisableVertexAttribArray(engine.aPositionLocation)

    }
}
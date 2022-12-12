package com.bura.desktop.util

import com.bura.desktop.util.IOUtils.Companion.ioResourceToByteBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengles.GLES20.*
import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.lwjgl.system.MemoryUtil.*


class DemoUtils {
    companion object {
        fun createShader(resource: String, type: Int): Int {
            return createShader(resource, type, null)
        }

        fun createShader(resource: String, type: Int, version: String?): Int {
            val shader: Int = glCreateShader(type)
            val source: ByteBuffer = ioResourceToByteBuffer(resource, 8192)
            if (version == null) {
                val strings = BufferUtils.createPointerBuffer(1)
                val lengths: IntBuffer = BufferUtils.createIntBuffer(1)
                strings.put(0, source)
                lengths.put(0, source.remaining())
                glShaderSource(shader, strings, lengths)
            } else {
                val strings = BufferUtils.createPointerBuffer(2)
                val lengths: IntBuffer = BufferUtils.createIntBuffer(2)
                val preamble: ByteBuffer = memUTF8("#version $version\n", false)
                strings.put(0, preamble)
                lengths.put(0, preamble.remaining())
                strings.put(1, source)
                lengths.put(1, source.remaining())
                glShaderSource(shader, strings, lengths)
            }
            glCompileShader(shader)
            val compiled: Int = glGetShaderi(shader, GL_COMPILE_STATUS)
            val shaderLog: String = glGetShaderInfoLog(shader)
            if (shaderLog.trim { it <= ' ' }.isNotEmpty()) {
                System.err.println(shaderLog)
            }
            if (compiled == 0) {
                throw AssertionError("Could not compile shader")
            }
            return shader
        }
    }
}
package com.bura.desktop.util

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.BufferUtils;
import kotlin.math.max


class IOUtils {
    companion object {
        private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
            val newBuffer: ByteBuffer = BufferUtils.createByteBuffer(newCapacity)
            buffer.flip()
            newBuffer.put(buffer)
            return newBuffer
        }

        fun ioResourceToByteBuffer(resource: String, bufferSize: Int): ByteBuffer {
            var buffer: ByteBuffer
            val url: URL = Thread.currentThread().contextClassLoader.getResource(resource)
                ?: throw IOException("Classpath resource not found: $resource")
            val file = File(url.file)
            if (file.isFile) {
                val fis = FileInputStream(file)
                val fc: FileChannel = fis.channel
                buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
                fc.close()
                fis.close()
            } else {
                buffer = BufferUtils.createByteBuffer(bufferSize)
                val source = url.openStream()// ?: throw FileNotFoundException(resource)
                source.use {
                    val buf = ByteArray(8192)
                    while (true) {
                        val bytes = it.read(buf, 0, buf.size)
                        if (bytes == -1) break
                        if (buffer.remaining() < bytes) buffer = resizeBuffer(
                            buffer,
                            max(
                                buffer.capacity() * 2,
                                buffer.capacity() - buffer.remaining() + bytes
                            )
                        )
                        buffer.put(buf, 0, bytes)
                    }
                    buffer.flip()
                }
            }
            return buffer
        }
    }
}
package com.bura.common.util

class FPSCounter {
    var startTime = System.nanoTime()
    var frames = 0

    fun logFrame() {
        frames++
        if (System.nanoTime() - startTime >= 1000000000) {
            println("fps: $frames")
            frames = 0
            startTime = System.nanoTime()
        }
    }
}
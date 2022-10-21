package com.bura.desktop.util

import org.lwjgl.glfw.GLFW

import org.lwjgl.glfw.GLFWKeyCallback


class Input : GLFWKeyCallback() {
    // Main key callback
    // This functions needs to be wrapped in a class in order to be referenced
    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        println(key)
        keys[key] = action != GLFW.GLFW_RELEASE
    }

    companion object {
        // Keycode sate list, size set according to available number of keys
        var keys = BooleanArray(65536)

        // Key down
        fun isKeyDown(keycode: Int): Boolean {
            return keys[keycode]
        }
    }
}
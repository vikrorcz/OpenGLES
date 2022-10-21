package com.bura.desktop

class DesktopLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val windowUtil = WindowUtil()
            windowUtil.setup()
        }
    }
}
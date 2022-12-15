package com.bura.opengles.engine

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.util.Log
import android.view.MotionEvent

class MyGLSurfaceView(context: Context): GLSurfaceView(context) {

    private lateinit var renderer: MyRenderer
    private var rendererSet = false

    init {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo

        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000 ||
                Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86")

        if (supportsEs2) {
            renderer = MyRenderer(context)
            setEGLContextClientVersion(2)
            setRenderer(renderer)
            rendererSet = true
        } else {
            Log.e("ERROR", "DEVICE DOES NOT SUPPORT GLES20")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (rendererSet) {
            val x = e.x
            val y = e.y
            renderer.touchX = x
            renderer.touchY = y
            when (e.action) {
                MotionEvent.ACTION_MOVE -> {
                    renderer.currentMotionEvent = MotionEvent.ACTION_MOVE
                    renderer.isTouching = true
                }
                MotionEvent.ACTION_DOWN -> {
                    renderer.currentMotionEvent = MotionEvent.ACTION_DOWN
                    renderer.isTouching = true
                }
                MotionEvent.ACTION_UP -> {
                    renderer.currentMotionEvent = MotionEvent.ACTION_UP
                    renderer.isTouching = false
                }
                else -> renderer.isTouching = false
            }
        }
        return true
    }
}
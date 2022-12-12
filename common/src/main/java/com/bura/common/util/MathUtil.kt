package com.bura.common.util

import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class MathUtil {
    companion object {
        fun getAngle(centerX: Float, centerY: Float, touchX: Float, touchY: Float): Double {
            return atan2((centerY - touchY).toDouble(), (centerX - touchX).toDouble())
        }

        fun getLength(x1: Float, y1: Float, x2: Float, y2: Float): Double {
            return sqrt(
                (x1 - x2).toDouble().pow(2.0) + (y1 - y2).toDouble().pow(2.0)
            )
        }
    }
}
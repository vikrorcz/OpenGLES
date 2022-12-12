package com.bura.common.util

class Constants {
    companion object {
        const val LOGGER_ON = true

        const val U_COLOR = "u_Color"
        const val A_POSITION = "a_Position"
        const val U_MATRIX = "u_Matrix"
        const val U_TEXTURE = "u_TexCoord"
        const val A_TEXTURE = "a_TexCoord"

        const val COORDS_PER_VERTEX = 3
        const val STRIDE = COORDS_PER_VERTEX * 4
        const val BYTES_PER_FLOAT = 4

        const val JOYSTICK_AREA = 0.3f
    }
}
package com.bura.common.util

interface TextureUtil {

    companion object {
        val textureHandle = IntArray(4)

        private const val path = "foo/"

        //todo perhaps create a hashmap
        const val playerTextureLocation = "${path}player.png"
        const val joystickOuterTextureLocation = "${path}joystick_outer.png"
        const val joystickInnerTextureLocation = "${path}joystick_inner.png"
        const val machineGunShotTextureLocation = "${path}machinegunshot.png"

        const val playerTextureId = 0
        const val joystickOuterId = 1
        const val joystickInnerId = 2
        const val machineGunShotId = 3

        val resourcesHM = hashMapOf<String, Int>().also {
            it[playerTextureLocation] = playerTextureId
            it[joystickOuterTextureLocation] = joystickOuterId
            it[joystickInnerTextureLocation] = joystickInnerId
        }
    }

    fun createTextures()

    fun loadTexture(resourceLocation: String): Int
}
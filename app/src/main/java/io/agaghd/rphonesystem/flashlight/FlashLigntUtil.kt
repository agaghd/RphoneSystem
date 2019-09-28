package io.agaghd.rphonesystem.flashlight

import android.hardware.Camera

object FlashLigntUtil {

    val TOGGLE_ORDER = "TOGGLE_FLASH_LIGHT"
    val camera = Camera.open()

    fun toggleTouchLight() {
        val light = Camera.Parameters.FLASH_MODE_TORCH == camera.parameters.flashMode
        val parameters = camera.parameters
        parameters.flashMode = if (light) Camera.Parameters.FLASH_MODE_OFF else Camera.Parameters.FLASH_MODE_TORCH
        camera.parameters = parameters
    }
}
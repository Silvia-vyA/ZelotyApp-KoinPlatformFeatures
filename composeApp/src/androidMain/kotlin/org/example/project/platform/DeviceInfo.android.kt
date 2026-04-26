package org.example.project.platform

import android.os.Build

actual class DeviceInfo actual constructor() {

    actual fun getDeviceName(): String {
        return Build.MODEL
    }

    actual fun getOsVersion(): String {
        return "Android ${Build.VERSION.RELEASE}"
    }

    actual fun getManufacturer(): String {
        return Build.MANUFACTURER
    }
}
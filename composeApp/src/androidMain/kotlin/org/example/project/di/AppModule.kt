package org.example.project.di

import org.example.project.platform.DeviceInfo
import org.example.project.platform.NetworkMonitor
import org.koin.dsl.module

val appModule = module {
    single { DeviceInfo() }
    single { NetworkMonitor() }
}
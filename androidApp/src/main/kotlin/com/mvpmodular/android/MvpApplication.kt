package com.mvpmodular.android

import android.app.Application
import com.mvpmodular.shared.initAppKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MvpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initAppKoin {
            androidLogger(Level.INFO)
            androidContext(this@MvpApplication)
        }
    }
}

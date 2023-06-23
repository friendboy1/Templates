package com.artofmainstreams.examples

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appDependencies(AppDependenciesImpl()).build()
    }

    private inner class AppDependenciesImpl : AppDependencies {
        override val context: Context = this@MainApplication
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> component
        else -> this.applicationContext.appComponent
    }
package org.silvatech.buzzmih.Controller

import android.app.Application
import org.silvatech.buzzmih.Utilities.SharedPrefs

class App:Application() {

    companion object {

        lateinit var prefs : SharedPrefs

    }

    override fun onCreate() {

        prefs = SharedPrefs(applicationContext)
        super.onCreate()

    }

}
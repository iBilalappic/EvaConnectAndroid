package com.hypernym.evaconnect

import android.app.Application
import com.hypernym.evaconnect.utils.AppUtils
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class EvaconnectApplication : Application() {
    private val context: EvaconnectApplication get() = EvaconnectApplication().context
    val hTag = "AhsanTimberTags %s"
    var mCalligraphyConfig: CalligraphyConfig? = null


    override fun onCreate() {
        super.onCreate()
        hInitTimber()
        CalligraphyConfig.initDefault(mCalligraphyConfig)
        AppUtils.setApplicationContext(applicationContext)
    }

    companion object {
        var instance: Application? = null
            private set
    }

    init {
        instance = this
    }

    private fun hInitTimber() {
        if (
            BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    super.log(priority, String.format(hTag, tag), message, t)
                }
            })
        }
    }
}
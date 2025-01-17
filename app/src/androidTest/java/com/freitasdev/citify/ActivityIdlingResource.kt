package com.freitasdev.citify

import android.app.Activity
import android.view.View
import androidx.test.espresso.IdlingResource

class ActivityIdlingResource(private val activity: Activity) : IdlingResource {
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName() = "ActivityIdlingResource"

    override fun isIdleNow(): Boolean {
        val isIdle = !activity.isFinishing && activity.window?.decorView?.visibility == View.VISIBLE

        if(isIdle) {
            callback?.onTransitionToIdle()
        }

        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

}
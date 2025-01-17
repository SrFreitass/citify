package com.freitasdev.citify

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.registerIdlingResources
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule

import org.junit.Test
import org.junit.runner.RunWith


import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.freitasdev.citify", appContext.packageName)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun onLaunched() {
        onView(withId(R.id.search_input))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.state_filter))
            .check(matches(isDisplayed()))

        onView(withId(R.id.region_filter))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onSyncCities() {
        onView(withId(R.id.sync_button))
            .perform(click())

        onView(withId(R.id.loading_text))
            .check(matches(withText("Sincronizando dados...")))

        var activity: Activity? = null

        activityRule.scenario.onActivity {
                activity = it
        }

        val idlingResource = activity?.let { ActivityIdlingResource(it) }

        IdlingRegistry
            .getInstance()
            .register(
                idlingResource
            )

        onView(withId(R.id.cities))
            .check(matches(isDisplayed()))

        IdlingRegistry
            .getInstance()
            .unregister(
                idlingResource
            )
    }


    @Test
    fun titleOnView() {
        onView(withText("Cidades do Brasil"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createCity() {

    }
}
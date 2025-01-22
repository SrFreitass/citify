package com.freitasdev.citify


import android.provider.Settings
import android.view.KeyEvent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.freitasdev.citify.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.hasToString
import org.hamcrest.Matchers.not


import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
val idlingResource = CountingIdlingResource("waitForView")

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

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

        onView(withId(R.id.cities)).check(matches(isDisplayed()))

    }

    @Test
    fun onSearchCity() {
        onSyncCities()

        onView(withId(R.id.search_input))
            .perform(click(), typeText("Campo Grande"), closeSoftKeyboard(), pressImeActionButton())

        onView(withId(R.id.cities)).check(matches(isDisplayed()))
    }

    @Test
    fun onFilterByState() {
        onSyncCities()

        onView(withId(R.id.state_filter))
            .perform(click())

        onData(hasToString("MS"))
            .perform(click())

        onView(withId(R.id.cities))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onFilterByRegion() {
        onSyncCities()

        onView(withId(R.id.radio_btn_1)).perform(click())

        onView(withId(R.id.cities))
            .check(matches(isDisplayed()))
    }

    @Test
    fun titleOnView() {
        onView(withText("Cidades do Brasil"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun createCity() {
        onView(withId(R.id.create_button))
            .perform(click())

        onView(withId(R.id.city_input))
            .perform(typeText("Brasilia"), closeSoftKeyboard(), pressImeActionButton())

        onView(withId(R.id.state_input))
            .perform(click())

        onView(withText("DF"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.region_input))
            .perform(click())

        onView(withText("Centro-Oeste"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.create_city_btn))
            .perform(click())

        onView(withId(R.id.cities))
            .check(matches(isDisplayed()))

    }

    @Test
    fun updateCity() {
        createCity()

        onView(withId(R.id.update_btn))
            .perform(click())

        onView(withId(R.id.city_input))
            .perform(replaceText("Solidao"), closeSoftKeyboard(), pressImeActionButton())

        onView(withId(R.id.state_input))
            .perform(click())

        onView(withText("MT"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.region_input))
            .perform(click())

        onView(withText("Centro-Oeste"))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        onView(withId(R.id.save_update))
            .perform(click())

        onView(withId(R.id.cities))
            .check(matches(isDisplayed()))

        onView(withText("Solidao"))
            .check(matches(isDisplayed()))

        onView(withText("MT"))
            .check(matches(isDisplayed()))

        onView(withText("Centro-Oeste"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun deleteCity() {
        createCity()

        onView(withId(R.id.delete_btn))
            .perform(click())

        onView(withText("CONFIRMAR"))
            .perform(click())

        onView(withId(R.id.cities))
            .check(matches(not(isDisplayed())))
    }
}
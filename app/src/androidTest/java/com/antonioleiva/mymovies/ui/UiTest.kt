package com.antonioleiva.mymovies.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.antonioleiva.mymovies.R
import com.antonioleiva.mymovies.data.server.TheMovieDb
import com.antonioleiva.mymovies.ui.main.MainActivity
import com.antonioleiva.mymovies.utils.fromJson
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get

class UiTest : KoinTest {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson(
                ApplicationProvider.getApplicationContext(),
                "popularmovies.json"
            )
        )

        val resource = OkHttp3IdlingResource.create("OkHttp", get<TheMovieDb>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun clickAMovieNavigatesToDetail() {
        activityTestRule.launchActivity(null)

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                click()
            )
        )

        onView(withId(R.id.movieDetailToolbar))
            .check(matches(hasDescendant(withText("Spider-Man: Far from Home"))))

    }
}
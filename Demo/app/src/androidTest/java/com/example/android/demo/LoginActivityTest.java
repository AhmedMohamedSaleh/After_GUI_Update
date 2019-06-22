package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by ebrahem1 on 6/15/2019.
 */
public class LoginActivityTest {

    // Run the darget activity  .
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testUserInputScenario_3()
    {
        // perform button click
        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        Espresso.onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.sign_in)).perform(click());

    }

    @Test
    public void testUserInputScenario_1()
    {
        // perform button click
        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        Espresso.onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.link_signup)).perform(click());
    }

    @Test
    public void testUserInputScenario_2()
    {
        // perform button click
        Espresso.onView(withId(R.id.link_signup)).perform(click());
    }


}
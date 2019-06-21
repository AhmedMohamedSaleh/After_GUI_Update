package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class API_Configration_Test {

    // Run the darget activity  .
    @Rule
    public ActivityTestRule<Local_API_Configration> mActivityTestRule = new ActivityTestRule<>(Local_API_Configration.class);

    /*
    @Test
    public void testUserInputScenario()
    {
        String url_1 = "https://api-skin-dis.herokuapp.com/";
        // input some text in the edit text

        Espresso.onView(withId(R.id.api_url)).perform(typeText(url_1)).perform(closeSoftKeyboard());
        // close soft keyboard
        //Espresso.closeSoftKeyboard();
        // perform button click
        Espresso.onView(withId(R.id.Save)).perform(click());

    }
    */
    @Test
    public void testUserInputScenario_1()
    {
        // perform button click
        Espresso.onView(withId(R.id.Save)).perform(click());
        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        Espresso.onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.link_signup)).perform(click());
    }
    @Test
    public void testUserInputScenario_2()
    {
        // perform button click
        Espresso.onView(withId(R.id.Save)).perform(click());
        Espresso.onView(withId(R.id.link_signup)).perform(click());
    }
    @Test
    public void testUserInputScenario_3()
    {
        // perform button click
        Espresso.onView(withId(R.id.Save)).perform(click());
        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        Espresso.onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.sign_in)).perform(click());

    }

}

package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.jsoup.helper.Validate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
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
    public void URL()
    {
        Valid_URL();
        //Invalid_URL();
    }

    public void Valid_URL()
    {
        if (Local_API_Configration.active)
        {
            Espresso.onView(withId(R.id.Save)).perform(click());
        }
        if (LoginActivity.active){

        }
        else{
            Espresso.onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }
    public void Valid_URL_1()
    {
        if (Local_API_Configration.active)
        {
            Espresso.onView(withId(R.id.api_url)).perform(typeText(""));
            Espresso.onView(withId(R.id.Save)).perform(click());
        }
        if (LoginActivity.active){

        }
        else{
            Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        }
    }
    public void Valid_URL_2()
    {
        if (Local_API_Configration.active)
        {
            Espresso.onView(withId(R.id.api_url)).perform(typeText(""));
            Espresso.onView(withId(R.id.Save)).perform(click());
            if (Get_Connection_Status.getUrl_api().equals("https://api-skin-dis.herokuapp.com/"))
            {

            }
        }
        if (LoginActivity.active){

        }
        else{
            Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        }
    }

   // @Test
    public void testUserInputScenario_1()
    {
        // perform button click

        if (!LoginActivity.active)
        {
            Espresso.onView(withId(R.id.Save)).perform(click());
            Espresso.onView(withId(R.id.username)).perform();
            if ( LoginActivity.evalue.equals("username") )
            {
                Espresso.onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard()) ;
                Espresso.onView(withId(R.id.username)).check(matches(hasErrorText("Field can't be empty"))) ;
                Espresso.onView(withId(R.id.link_signup)).perform(click()) ;
            }
        }
        else {
            Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        }

    }
    /*
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
*/
}

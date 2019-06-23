package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.helper.Validate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class API_Configration_Test {

    // Run the darget activity  .
    @Rule
    public ActivityTestRule<Local_API_Configration> mActivityTestRule = new ActivityTestRule<>(Local_API_Configration.class);

    @Test
    public void Valid_URL_Herrocu_URL()
    {
        if (Local_API_Configration.active)
        {

            onView(withId(R.id.Save)).perform(click());
            if (Get_Connection_Status.getUrl_api().equals("https://api-skin-dis.herokuapp.com/"))
            {
                if (LoginActivity.active){

                }
                else{
                    onView(withId(R.id.error_id)).perform(typeText("error"));
                }

            }else{
                onView(withId(R.id.error_id)).perform(typeText("error"));
            }
        }
        else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }

    }

    @Test
    public void Valid_URL_Local_URL()
    {
        if (Local_API_Configration.active)
        {
            onView(withId(R.id.api_url)).perform(typeText("invalid_url"));
            onView(withId(R.id.Save)).perform(click());

                onView(withId(R.id.api_url)).perform(replaceText("192.168.1.3"));
                onView(withId(R.id.Save)).perform(click());

            //System.out.println( Get_Connection_Status.getUrl_api() );

            if (Get_Connection_Status.getUrl_api().equals("http://192.168.1.3:5000/"))
                {
                   // return true;
                }else{
                    onView(withId(R.id.error_id)).perform(typeText("error"));
                }
            if (LoginActivity.active){

            }
            else{
                onView(withId(R.id.error_id)).perform(typeText("error"));
            }
        }
        else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
       // return false;
    }

   // @Test
    public void testUserInputScenario_1()
    {
        // perform button click

        if (!LoginActivity.active)
        {
            onView(withId(R.id.Save)).perform(click());
            onView(withId(R.id.username)).perform();
            if ( LoginActivity.evalue.equals("username") )
            {
                onView(withId(R.id.password)).perform(typeText("amd")).perform(closeSoftKeyboard()) ;
                onView(withId(R.id.username)).check(matches(hasErrorText("Field can't be empty"))) ;
                onView(withId(R.id.link_signup)).perform(click()) ;
            }
        }
        else {
            onView(withId(R.id.username)).perform(typeText("amd"));
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

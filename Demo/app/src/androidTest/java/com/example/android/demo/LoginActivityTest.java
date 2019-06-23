package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.demo.R.id.sign_in;
import static org.junit.Assert.*;

/**
 * Created by ebrahem1 on 6/15/2019.
 */

public class LoginActivityTest {

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
    //Handler handler = new Handler();

    // Run the darget activity  .
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void valid_username()
    {
        Get_Connection_Status.setUrl_api(Get_Connection_Status.getUrl_api()+"/");

        // perform button click
            System.out.println( "1111 : " + mActivityTestRule.getActivity().sign_in.isEnabled() );
        //mActivityTestRule.getActivity().Validate_Inputs_And_Enable_Buttons();
        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        //assertEquals(true, mActivityTestRule.getActivity().validateUsername());
        Espresso.onView(withId(R.id.password)).perform(typeText("amd"),pressImeActionButton()).perform(closeSoftKeyboard());
        if (LoginActivity.evalue.equals("password"))
        {
            System.out.println( "1111 wwwwww : " + mActivityTestRule.getActivity().sign_in.isEnabled() );
            Espresso.onView(withId(sign_in)).perform(click());
            System.out.println( mActivityTestRule.getActivity().sign_in.isEnabled() );
            /*while(true) {
                System.out.println( mActivityTestRule.getActivity().sign_in.isEnabled() );
                if (!mActivityTestRule.getActivity().sign_in.isEnabled())
                    break;
            }*/
            if (Profile.active){

            }
            else{
                onView(withId(R.id.error_id)).perform(typeText("error"));
            }
        }
    }


    //@Test
    public void INvalid_username_pattern()
    {
        // perform button click
        System.out.println( "1111 : " + mActivityTestRule.getActivity().sign_in.isEnabled() );
        //mActivityTestRule.getActivity().Validate_Inputs_And_Enable_Buttons();
        Espresso.onView(withId(R.id.username)).perform(typeText("3_invalid"));
        Espresso.onView(withId(R.id.password)).perform(typeText("amd"));
        onView(withId(R.id.username)).check(matches(hasErrorText("Please enter a valid user name"))) ;
        assertEquals(true, mActivityTestRule.getActivity().validateUsername());
    }

    //@Test
    public void INvalid_password_pattern()
    {
        // perform button click

        Espresso.onView(withId(R.id.username)).perform(typeText("amd"));
        assertEquals(true, mActivityTestRule.getActivity().validateUsername());
        //assertEquals(true, mActivityTestRule.getActivity().validateUsername());
        Espresso.onView(withId(R.id.password)).perform(typeText("___"),pressImeActionButton()).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).check(matches(hasErrorText("Please enter a valid password"))) ;
        assertEquals(true, mActivityTestRule.getActivity().validatePassword());

    }


}
package com.example.android.demo;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by ebrahem1 on 6/22/2019.
 */

public class Upload_test {

    @Rule
    public ActivityTestRule<Upload> mActivityTestRule = new ActivityTestRule<>(Upload.class);

   // @Test
    public void test_select()
    {
        if(mActivityTestRule.getActivity().select_button.isEnabled())
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }

    @Test
    public void test_choose_image(){
        if(mActivityTestRule.getActivity().select_button.isEnabled())
        {
            onView(withId(R.id.select)).perform(click());

            onView(isRoot()).perform(ViewActions.pressBack());

            onView(withText("image path didn't establish .")).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }

   /* @Test
    public void test_cancel_first_look(){
        if(mActivityTestRule.getActivity().cancel_button.isEnabled())
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }
    @Test
    public void test_upload_first_look(){
        if(mActivityTestRule.getActivity().upload_button.isEnabled())
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }
*/

}

package com.example.android.demo;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;



/**
 * Created by ebrahem1 on 6/22/2019.
 */

public class Profile_Test {

    @Rule
    public ActivityTestRule<Profile> mActivityTestRule = new ActivityTestRule<>(Profile.class);

    @Test
    public void test_signout(){
        Espresso.onView(withId(R.id.signout_button)).perform(click());
        if (LoginActivity.active)
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }

    @Test
    public void test_upload(){
        Espresso.onView(withId(R.id.upload_button)).perform(click());
        if (Upload.active)
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }

    @Test
    public void test_ViewImage(){
        Espresso.onView(withId(R.id.view_images_button)).perform(click());
        if (User_Photos.active)
        {

        }else{
            onView(withId(R.id.error_id)).perform(typeText("error"));
        }
    }





}

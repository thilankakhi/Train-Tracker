package com.example.traintracker;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainMenuTest {
    @Rule
    public ActivityTestRule<MainMenu> activityTestRule = new ActivityTestRule<>(MainMenu.class);

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(TrainSchedule.class.getName(),null,false);
    private Activity activity;

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        activity = null;
    }

    @Test
    public void viewTrainSchedule(){
        Espresso.onView(withId(R.id.train_shedule)).perform(click());
        Activity trainSchedule = getInstrumentation().waitForMonitorWithTimeout(monitor,2000);
        assertNotNull(trainSchedule);
    }
}
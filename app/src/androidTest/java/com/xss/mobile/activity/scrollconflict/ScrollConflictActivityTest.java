package com.xss.mobile.activity.scrollconflict;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.xss.mobile.MainActivity;
import com.xss.mobile.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by xss on 2017/5/5.
 */
public class ScrollConflictActivityTest {

    // @Rule 修饰被测试的 Activity
    @Rule
    public ActivityTestRule mActivityTextRule = new ActivityTestRule(MainActivity.class);


    public static Matcher<Object> withValue(final int value) {
        return new BoundedMatcher<Object, String>(String.class) {

            @Override
            public void describeTo(Description description) {
                description.appendValue("has value " + value);
            }

            @Override
            protected boolean matchesSafely(String item) {
                return item.equals(value);
            }
        };
    }

    @Test
    public void clickItem() {
        onData(withValue(3))
                .inAdapterView(withId(R.id.lv_list))
                .perform(click());
    }

}
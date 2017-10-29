package com.xss.mobile;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by xss on 2017/5/5.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // @Rule 修饰被测试的 Activity
    @Rule
    public ActivityTestRule mActivityTextRule = new ActivityTestRule(MainActivity.class);

    /**
     * 测试用例：点击 btn_test 按钮，对其调用 click()，最后检验控件是不是 enabled 状态
     */
    @Test
    public void testTextViewDisplay() {
        onView(withId(R.id.btn_test))
                .perform(click())
                .check(matches(isClickable()));
    }

    /**
     * 测试用例：点击 RecyclerView 的第一个 item，
     */
    @Test
    public void clickItem() {
        // 必须在 item 可见时，才能执行以下点击，item不可见时，执行用例会failed
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    /**
     * 测试用例：检查 text文本 是否成功 显示在了屏幕上
     */
    @Test
    public void isTextDisplayedOnScreen() {
        onView(withText("Hello World!"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isAllTextDisplayedOnScreen() {
        // allOf 保证 test 的是 unique 控件
        onView(allOf(withId(R.id.btn_a1), withText("Hello World!")))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.btn_a1), not(withText("Unwanted!"))))
                .check(matches(isDisplayed()));

        // checkt the btn contains text hello !
        onView(withId(R.id.btn_a1))
                .check(matches(withText("Hello !")));
    }

}
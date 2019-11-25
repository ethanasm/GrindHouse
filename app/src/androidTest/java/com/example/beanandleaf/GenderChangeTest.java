package com.example.beanandleaf;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GenderChangeTest {

    @Rule
    public ActivityTestRule<LandingPage> mActivityTestRule = new ActivityTestRule<>(LandingPage.class);

    @Test
    public void genderChangeTest() {

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.link_signup)));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.name)));
        appCompatEditText.perform(scrollTo(), replaceText("Helen Sam"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.email)));
        appCompatEditText2.perform(scrollTo(), replaceText("samh@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password)));
        appCompatEditText3.perform(scrollTo(), replaceText("samhel"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.register)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_profile)));
        bottomNavigationItemView.perform(click());

        //Change sex
        ViewInteraction appCompatRadioButton = onView(allOf(withId(R.id.female)));
        appCompatRadioButton.perform(scrollTo(), click());

        //Submit sex change
        ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.update_profile)));
        appCompatButton3.perform(scrollTo(), click());

        //Navigate away to see if value is updated when we navigate back
        ViewInteraction bottomNavigationItemView2 = onView(allOf(withId(R.id.navigation_map)));
        bottomNavigationItemView2.perform(click());

        ViewInteraction bottomNavigationItemView3 = onView(allOf(withId(R.id.navigation_profile)));
        bottomNavigationItemView3.perform(click());

        //Ensure that the female sex is selected
        ViewInteraction femaleRadioButton = onView(allOf(withId(R.id.female)));
        femaleRadioButton.check(matches(isChecked()));

        //Make sure the other gender options are not
        ViewInteraction radioButton2 = onView(allOf(withId(R.id.male)));
        radioButton2.check(matches(isNotChecked()));
        ViewInteraction radioButton3 = onView(allOf(withId(R.id.other)));
        radioButton3.check(matches(isNotChecked()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

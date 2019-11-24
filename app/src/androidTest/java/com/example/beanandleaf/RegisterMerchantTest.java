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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterMerchantTest {

    @Rule
    public ActivityTestRule<LandingPage> mActivityTestRule = new ActivityTestRule<>(LandingPage.class);

    @Test
    public void registerMerchantTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.link_signup), withText("No account yet? Create one")));
        appCompatButton.perform(click());

        //Set Name
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name)));
        appCompatEditText.perform(scrollTo(), replaceText("Merchant One"), closeSoftKeyboard());

        //Set Email
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.email)));
        appCompatEditText2.perform(scrollTo(), replaceText("m@gmail.com"), closeSoftKeyboard());

        //Set Password
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password)));
        appCompatEditText3.perform(scrollTo(), replaceText("merchant"), closeSoftKeyboard());

        //Set as Merchant
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password), withText("merchant")));
        appCompatEditText4.perform(pressImeActionButton());

        //Verify that Merchant Button Exists
        ViewInteraction radioButton = onView(
                allOf(withId(R.id.Merchant),
                        childAtPosition(
                                allOf(withId(R.id.userType),
                                        childAtPosition(
                                                withId(R.id.relativeLayout2),
                                                8)),
                                1),
                        isDisplayed()));
        radioButton.check(matches(isDisplayed()));

        //Click Merchant
        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.Merchant), withText("Merchant"),
                        childAtPosition(
                                allOf(withId(R.id.userType),
                                        childAtPosition(
                                                withId(R.id.relativeLayout2),
                                                7)),
                                1)));
        appCompatRadioButton.perform(scrollTo(), click());

        //Create Account
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.register), withText("Create My Account")));
        appCompatButton2.perform(scrollTo(), click());

        //Check that Store Icon Displays (Only available in Merchant View)
        ViewInteraction imageView = onView(
                allOf(withId(R.id.icon)));
        imageView.check(matches(isDisplayed()));
        
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

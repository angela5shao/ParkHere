package csci310.parkhere.ui.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PaymentTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void paymentTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.loginButton), withText("login")));
        button.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                withId(R.id.emailText));
        editText.perform(scrollTo(), replaceText("annchies@usc.edu"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                withId(R.id.passwordText));
        editText2.perform(scrollTo(), replaceText(".1111111111"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.loginButton), withText("login")));
        button2.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.usertypeSpinner));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Search by latitude, longitude"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.in_lat),
                        withParent(withId(R.id.latlongSearchLayout))));
        appCompatEditText.perform(scrollTo(), replaceText("34.0597"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.in_long),
                        withParent(withId(R.id.latlongSearchLayout))));
        appCompatEditText2.perform(scrollTo(), replaceText("-118.445"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.in_start_date)));
        appCompatEditText3.perform(scrollTo(), replaceText("1-12-2016"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.in_start_time)));
        appCompatEditText4.perform(scrollTo(), replaceText("22:00"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.in_end_date)));
        appCompatEditText5.perform(scrollTo(), replaceText("2-12-2016"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.in_end_time)));
        appCompatEditText6.perform(scrollTo(), replaceText("22:00"), closeSoftKeyboard());


        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.distSpinner));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Within 3 mi"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.btn_confirm), withText("Search")));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                childAtPosition(
                        withId(R.id.searchresultList),
                        0));
        linearLayout.perform(scrollTo(), click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.searchspacedetail_reservebutton), withText("Reserve")));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.payment_button), withText("proceed to checkout")));
        button3.perform(scrollTo(), click());

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

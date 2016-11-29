package csci310.parkhere.ui.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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
public class SearchTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void searchTest() {
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
        appCompatEditText3.perform(scrollTo(), replaceText("29-11-2016"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.in_start_time)));
        appCompatEditText4.perform(scrollTo(), replaceText("22:00"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.in_end_date)));
        appCompatEditText5.perform(scrollTo(), replaceText("30-11-2016"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.in_end_time)));
        appCompatEditText6.perform(scrollTo(), replaceText("22:00"), closeSoftKeyboard());



//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.btn_start_date), withText("SELECT START DATE")));
//        appCompatButton.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(android.R.id.button1), withText("OK"),
//                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
//                                withParent(withClassName(is("android.widget.LinearLayout"))))),
//                        isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.btn_start_time), withText("SELECT START TIME")));
//        appCompatButton3.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(android.R.id.button1), withText("OK"),
//                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
//                                withParent(withClassName(is("android.widget.LinearLayout"))))),
//                        isDisplayed()));
//        appCompatButton4.perform(click());
//
//        ViewInteraction appCompatButton5 = onView(
//                allOf(withId(R.id.btn_end_date), withText("SELECT END DATE")));
//        appCompatButton5.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton6 = onView(
//                allOf(withId(android.R.id.button1), withText("OK"),
//                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
//                                withParent(withClassName(is("android.widget.LinearLayout"))))),
//                        isDisplayed()));
//        appCompatButton6.perform(click());
//
//        ViewInteraction appCompatButton7 = onView(
//                allOf(withId(R.id.btn_end_time), withText("SELECT END TIME")));
//        appCompatButton7.perform(scrollTo(), click());
//
//        ViewInteraction appCompatButton8 = onView(
//                allOf(withId(android.R.id.button1), withText("OK"),
//                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
//                                withParent(withClassName(is("android.widget.LinearLayout"))))),
//                        isDisplayed()));
//        appCompatButton8.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.distSpinner));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Within 3 mi"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.btn_confirm), withText("Search")));
        appCompatButton9.perform(scrollTo(), click());

//        ViewInteraction appCompatImageView = onView(
//                allOf(withId(R.id.profilePic), isDisplayed()));
//        appCompatImageView.perform(click());

    }

}

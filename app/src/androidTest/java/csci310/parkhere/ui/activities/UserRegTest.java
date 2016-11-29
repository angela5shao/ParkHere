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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserRegTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void userRegTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.registerButton), withText("signup")));
        button.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                withId(R.id.nameText));
        editText.perform(scrollTo(), replaceText("Angela"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.nameText), withText("Angela")));
        editText2.perform(scrollTo(), click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.nameText), withText("Angela")));
        editText3.perform(scrollTo(), replaceText("Angela A"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                withId(R.id.emailText));
        editText4.perform(scrollTo(), replaceText("Angela.sh@hotmail.com"), closeSoftKeyboard());

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.emailText), withText("Angela.sh@hotmail.com")));
        editText5.perform(scrollTo(), click());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.emailText), withText("Angela.sh@hotmail.com")));
        editText6.perform(scrollTo(), replaceText("angela.sh@hotmail.com"), closeSoftKeyboard());

        ViewInteraction editText7 = onView(
                withId(R.id.passwordText));
        editText7.perform(scrollTo(), replaceText(".1111111111"), closeSoftKeyboard());

        ViewInteraction editText8 = onView(
                withId(R.id.phoneText));
        editText8.perform(scrollTo(), replaceText("2132132133"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.registerNextButton), withText("next")));
        button2.perform(scrollTo(), click());

        ViewInteraction editText9 = onView(
                withId(R.id.liscenseIdText));
        editText9.perform(scrollTo(), replaceText("2562156"), closeSoftKeyboard());

        ViewInteraction editText10 = onView(
                withId(R.id.liscensePlateNumText));
        editText10.perform(scrollTo(), replaceText("7jop9"), closeSoftKeyboard());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.nextButton), withText("next")));
        button3.perform(scrollTo(), click());

        ViewInteraction editText11 = onView(
                allOf(withId(R.id.codeDialog), isDisplayed()));
        editText11.perform(click());

        ViewInteraction editText12 = onView(
                allOf(withId(R.id.codeDialog), isDisplayed()));
        editText12.perform(replaceText("b36c61"), closeSoftKeyboard());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.btn_confirm), withText("Confirm"), isDisplayed()));
        button4.perform(click());

    }

}

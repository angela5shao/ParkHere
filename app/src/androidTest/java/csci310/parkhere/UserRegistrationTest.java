package csci310.parkhere;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.ui.RegisterMainActivity;
import csci310.parkhere.ui.RegisterRenterActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

//@RunWith(AndroidJUnit4.class)
//@LargeTest
public class UserRegistrationTest {

    public static final String NAME_TO_BE_TYPED = "Espresso East";
    public static final String EMAIL_TO_BE_TYPED = "espressoeast@usc.edu";
    public static final String NAME2_TO_BE_TYPED = "Espresso West";
    public static final String EMAIL2_TO_BE_TYPED = "espressowest@usc.edu";
    public static final String PASSWORD_SHORT_TO_BE_TYPED = "12345";
    public static final String PASSWORD_LONG_TO_BE_TYPED = "123456789012";
    public static final String PHONE_TO_BE_TYPED = "2132132133";

    @Rule
    public ActivityTestRule<RegisterMainActivity> mActivityRule = new ActivityTestRule(RegisterMainActivity.class);

//    @Test
//    public void listGoesOverTheFold() {
//        onView(withText("Hello world!")).check(matches(isDisplayed()));
//    }
    @Test
    public void uniqueEmailLongPasswordRegistration() {
        // Type name, email, password, phone.
        onView(withId(R.id.nameText)).perform(typeText(NAME_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_LONG_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());

        // Select "Renter". Then press button.
        onView(withId(R.id.usertypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Renter"))).perform(click());
        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));

        onView(withId(R.id.nextButton)).perform(click());

        // Check that intent to Renter (same as Provider) Registration Activity is called.
        intended(toPackage("csci310.parkhere.RegisterRenterActivity"));
    }

    @Test
    public void uniqueEmailShortPasswordRegistration() {
        // Type name, email, password, phone.
        onView(withId(R.id.nameText)).perform(typeText(NAME2_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL2_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_SHORT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());

        // Select "Renter". Then press button.
        onView(withId(R.id.usertypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Renter"))).perform(click());
        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));

        onView(withId(R.id.nextButton)).perform(click());

        // Check that intent to Renter (same as Provider) Registration Activity is not called.
        intended(hasComponent(RegisterRenterActivity.class.getName()), times(0));
    }

    @Test
    public void duplicateRegistration() {
        // Type name, email, password, phone.
        onView(withId(R.id.nameText)).perform(typeText(NAME_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_LONG_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());

        // Select "Renter". Then press button.
        onView(withId(R.id.usertypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Renter"))).perform(click());
        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));

        onView(withId(R.id.nextButton)).perform(click());

        // Check that intent to Renter (same as Provider) Registration Activity is not called.
        intended(hasComponent(RegisterRenterActivity.class.getName()), times(0));
    }
}
package csci310.parkhere.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;
import csci310.parkhere.ui.HomeActivity;
import csci310.parkhere.ui.LoginActivity;
import csci310.parkhere.ui.ProviderActivity;
import csci310.parkhere.ui.RegisterMainActivity;
import csci310.parkhere.ui.RegisterRenterActivity;
import csci310.parkhere.ui.RenterActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

//    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com"; //"espressoeast@usc.edu";
//    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890"; //"12345";
    public static final String PASSWORD_INCORRECT_TO_BE_TYPED = "0987654321";

    public static final String EMAIL_TO_BE_TYPED = "renter@usc.edu";
    public static final String EMAIL2_TO_BE_TYPED = "provider@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "123456789012";
    public static final String LICENSE_PLATE2_TO_BE_TYPED = "079079079";

    public static final String PHONE_TO_BE_TYPED = "2132132133";
    public static final String LICENSE_TO_BE_TYPED = "0909090";
    public static final String LICENSE_PLATE_TO_BE_TYPED = "089089089";

    @Rule
    public IntentsTestRule<HomeActivity> mActivityRule = new IntentsTestRule(HomeActivity.class);

    @Test
    public void successfulLogin() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login (as a renter). Type email, password. Then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Assuming registered as a renter, check that intent to Renter Activity is called.
        intended(hasComponent(RenterActivity.class.getName()));
//        intended(hasComponent(new ComponentName(getTargetContext(), RenterActivity.class)));
    }


    /*
    Test that role is correct when logged in after registering & logging out as a provider.
     */
    @Test
    public void switchToProviderRoleLogoutCheckRole() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login (as a renter). Type email, password. Then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent(RenterActivity.class.getName()));

        // Switch to provider
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Renter")).perform(click());

        // Logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());

        // In HomeActivity, click on login.  Login in LoginActivity
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Check that RenterActivity is called.
        intended(hasComponent(RenterActivity.class.getName()));
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
//                Activity.RESULT_OK, new Intent());
//        intending(toPackage("csci310.parkhere.ui")).respondWith(result);

        // For consistency purposes (set last role as renter), switch back to renter and logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Provider")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());
    }

    @Test
    public void switchToRenterRoleLogoutCheckRole() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login (as a provider). Type email, password. Then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL2_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Check that ProviderActivity is called.
        intended(hasComponent(ProviderActivity.class.getName()));

        // Switch to renter
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Renter")).perform(click());

        // Enter license plate number with original password, phone number, and licence ID
        onView(withId(R.id.licenseIDText)).perform(typeText(EMAIL2_TO_BE_TYPED));
        onView(withId(R.id.licenseplateText)).perform(typeText(EMAIL2_TO_BE_TYPED));
        onView(withId(R.id.phoneText)).perform(typeText(EMAIL2_TO_BE_TYPED));
        onView(withId(R.id.pwText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());

        // Logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());

        // In HomeActivity, click on login.  Login in LoginActivity
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Check that RenterActivity is called.
        intended(hasComponent(RenterActivity.class.getName()));
//        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
//                Activity.RESULT_OK, new Intent());
//        intending(toPackage("csci310.parkhere.ui")).respondWith(result);

        // For consistency purposes (set last role as renter), switch back to renter and logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Provider")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());
    }

    /*
    Test that login fails with incorrect credentials
     */
    @Test
    public void incorrectEmailAndPasswordLogin() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login (as a renter). Type email, password. Then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_INCORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

//        intended(hasComponent(RenterActivity.class.getName()), times(0));
//        intended(hasComponent(ProviderActivity.class.getName()), times(0));

        onView(withId(R.id.renter_ui)).check(doesNotExist());
        onView(withId(R.id.provider_ui)).check(doesNotExist());
    }
}
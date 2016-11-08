package csci310.parkhere.ui;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

//    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com"; //"espressoeast@usc.edu";
//    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890"; //"12345";
    public static final String PASSWORD_INCORRECT_TO_BE_TYPED = "0987654321";

    public static final String EMAIL_TO_BE_TYPED = "renter8@usc.edu";
    public static final String EMAIL2_TO_BE_TYPED = "provider6@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "123456789012";
//    public static final String LICENSE_PLATE2_TO_BE_TYPED = "079079079";

    public static final String PHONE_TO_BE_TYPED = "2132132133";
    public static final String LICENSE_TO_BE_TYPED = "0909090";
    public static final String LICENSE_PLATE_TO_BE_TYPED = "089089089";

    @Rule
    public IntentsTestRule<HomeActivity> mActivityRule = new IntentsTestRule(HomeActivity.class);

//    @Test
//    public void successfulLogin() {
//        // From HomeActivity, click on Login
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Login (as a renter). Type email, password. Then press Login button
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Assuming registered as a renter, check that intent to Renter Activity is called.
//        intended(hasComponent(RenterActivity.class.getName()));
////        intended(hasComponent(new ComponentName(getTargetContext(), RenterActivity.class)));
//    }


//    /*
//    Test that role is correct when logged in after registering & logging out as a provider.
//     */
//    @Test
//    public void switchToProviderRoleLogoutCheckRole() {
//        // From HomeActivity, click on Login
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Login (as a renter). Type email, password. Then press Login button
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//        intended(hasComponent(RenterActivity.class.getName()));
//
//        // Switch to provider
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Switch to Provider")).perform(click());
//
//        // Logout
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Log Out")).perform(click());
//
//        // In HomeActivity, click on login.  Login in LoginActivity
//        onView(withId(R.id.loginButton)).perform(click());
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Check that RenterActivity is called.
//        intended(hasComponent(RenterActivity.class.getName()));
//
//        // For consistency purposes (set last role as renter), switch back to renter and logout
////        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
////        onView(withText("Switch to Renter")).perform(click());
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Log Out")).perform(click());
//    }
//
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

        // Click on edit profile
        onView(withId(R.id.editLogo)).perform(click());

        // Enter license plate number with original password, phone number, and licence ID
        onView(withId(R.id.licenseIDText)).perform(clearText(),typeText(LICENSE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.licenseplateText)).perform(clearText(),typeText(LICENSE_PLATE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phoneText)).perform(clearText(),typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.pwText)).perform(clearText(),typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());

        // Click save button
        onView(withId(R.id.btn_save)).perform(scrollTo(), click());

        // Logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());

        // In HomeActivity, click on login.  Login in LoginActivity
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL2_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Check that RenterActivity is called.
        intended(hasComponent(RenterActivity.class.getName()));

        // For consistency purposes (set last role as renter), switch back to renter and logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Provider")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());
    }
//
//    /*
//    Test that login fails with incorrect credentials
//     */
//    @Test
//    public void incorrectEmailAndPasswordLogin() {
//        // From HomeActivity, click on Login
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Login (as a renter). Type email, password. Then press Login button
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_INCORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
////        intended(hasComponent(RenterActivity.class.getName()), times(0));
////        intended(hasComponent(ProviderActivity.class.getName()), times(0));
//
//        onView(withId(R.id.renter_ui)).check(doesNotExist());
//        onView(withId(R.id.provider_ui)).check(doesNotExist());
//    }
}
package csci310.parkhere;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import csci310.parkhere.ui.LoginActivity;
import csci310.parkhere.ui.ProviderActivity;
import csci310.parkhere.ui.RegisterMainActivity;
import csci310.parkhere.ui.RegisterRenterActivity;
import csci310.parkhere.ui.RenterActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

//@RunWith(AndroidJUnit4.class)
//@LargeTest
public class LoginTest {

    public static final String EMAIL_TO_BE_TYPED = "yy"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "123"; //"12345";
    public static final String PASSWORD_INCORRECT_TO_BE_TYPED = "123456789012";
    public static final String LAST_USER_ROLE = "Provider";
    public static final String LICENCE_TO_BE_TYPED = "1122334455";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void successfulLogin() {
        // Login (as a renter only) and logout. Type email, password. Then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Check that intent to Renter Activity is called.
        intended(toPackage("csci310.parkhere.RenterActivity"));
    }

//    @Test
//    public void addProviderRoleLogoutCheckRole() {
//        // Type email, password. Then press Login button
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Switch to provider
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Switch to Provider")).perform(click());
//
//        // Check that RegisterProviderActivity is called.
//        intended(toPackage("csci310.parkhere.RegisterProviderActivity"));
//
//        // Enter licence ID. Click on Next.
//        onView(withId(R.id.liscenseIdText)).perform(typeText(LICENCE_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.nextButton)).perform(click());
//
//        // Check that ProviderActivity is called.
//        intended(toPackage("csci310.parkhere.ProviderActivity"));
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
//        // Check that ProviderActivity is called.
//        intended(toPackage("csci310.parkhere.ProviderActivity"));
//    }
//
//    @Test
//    public void incorrectEmailAndPasswordLogin() {
//        // Type email, password. Then press Login button
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_INCORRECT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        intended(hasComponent(RenterActivity.class.getName()), times(0));
//        intended(hasComponent(ProviderActivity.class.getName()), times(0));
//    }
}
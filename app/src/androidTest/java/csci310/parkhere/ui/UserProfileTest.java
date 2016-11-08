package csci310.parkhere.ui;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileTest {
    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890"; //"12345";
//    public static final String LAST_USER_ROLE = "Provider";
    public static final String LICENSE_TO_BE_TYPED = "747234";
    public static final String LICENSE_PLATE_TO_BE_TYPED = "089089089";
    public static final String PHONE_TO_BE_TYPED = "7472346798";
    public static final String PHONE_TO_BE_TYPED_SHORT = "747234";

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule(HomeActivity.class);

    @Before
    public void init() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        // Login: type email and password, and then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void editPasswordLongPhoneNumberLong() {

    }

    @Test
    public void editPasswordShort() {
        // Click on Profile and validate email
        onView(withId(R.id.profilePic)).perform(click());
        onView(withId(R.id.usernameText)).check(matches(withText(EMAIL_TO_BE_TYPED)));
        onView(withId(R.id.editLogo)).perform(click());
        onView(withId(R.id.pwText)).perform(typeText("01234"), closeSoftKeyboard());
        onView(withText(R.id.btn_save)).perform(ViewActions.scrollTo()).perform(click());
        // TODO: check licence ID & licence plate
        onView(withId(R.id.profilePic)).perform(click());
        onView(withId(R.id.licenseIDText)).check(matches(withText(LICENSE_TO_BE_TYPED)));
        onView(withId(R.id.licenseplateText)).check(matches(withText(LICENSE_PLATE_TO_BE_TYPED)));
        onView(withId(R.id.phoneText)).check(matches(withText(PHONE_TO_BE_TYPED)));
    }

    @Test
    public void editPhoneNumberShort() {
        // Click on Profile and validate email
        onView(withId(R.id.profilePic)).perform(click());
        onView(withId(R.id.usernameText)).check(matches(withText(EMAIL_TO_BE_TYPED)));
        onView(withId(R.id.editLogo)).perform(click());
        onView(withId(R.id.pwText)).perform(typeText("1234567890"));
        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED_SHORT), closeSoftKeyboard());
        onView(withText(R.id.btn_save)).perform(ViewActions.scrollTo()).perform(click());
        // TODO: check licence ID & licence plate
        onView(withId(R.id.profilePic)).perform(click());
        onView(withId(R.id.licenseIDText)).check(matches(withText(LICENSE_TO_BE_TYPED)));
        onView(withId(R.id.licenseplateText)).check(matches(withText(LICENSE_PLATE_TO_BE_TYPED)));
        onView(withId(R.id.phoneText)).check(matches(withText(PHONE_TO_BE_TYPED)));
    }

    @Test
    public void checkPrivateProfileInfo() {
        // Click on Profile and validate email
        onView(withId(R.id.profilePic)).perform(click());
        onView(withId(R.id.usernameText)).check(matches(withText(EMAIL_TO_BE_TYPED)));
        // TODO: check licence ID & licence plate
        onView(withId(R.id.licenseIDText)).check(matches(withText(LICENSE_TO_BE_TYPED)));
        onView(withId(R.id.licenseplateText)).check(matches(withText(LICENSE_PLATE_TO_BE_TYPED)));
        onView(withId(R.id.phoneText)).check(matches(withText(PHONE_TO_BE_TYPED)));
    }
}

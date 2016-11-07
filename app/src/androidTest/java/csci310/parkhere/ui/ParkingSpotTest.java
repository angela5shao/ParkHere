package csci310.parkhere.ui;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import csci310.parkhere.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ParkingSpotTest {
    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890"; //"12345";
    public static final String LAST_USER_ROLE = "Provider";
    public static final String LICENSE_TO_BE_TYPED = "0909090";
    public static final String LICENSE_PLATE_TO_BE_TYPED = "089089089";

    public static final String ADDRESS_TO_BE_TYPED = "UC Irvine";
    public static final String DESCRIPTION_TO_BE_TYPED = "Call me to get entry into compound";
    public static final String CARTYPE_TO_SELECT = "SUV";
    public static final String CANCEL_POLICY_TO_SELECT = "10% penalty for cancellation";

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule(HomeActivity.class);

    @Before
    public void init() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login: type email and password, and then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

//    @Test
//    public void addNewSpot() {
//        // Ensure in Provider role
//        intended(hasComponent(ProviderActivity.class.getName()));
//
//        // Click on Add, type new space info (address, car type, cancellation policy)
//        onView(withId(R.id.spaces_addbutton)).perform(click());
//        onView(withId(R.id.btn_add_address)).perform(click());
//        onView(withId(R.id.emailText)).perform(typeText(ADDRESS_TO_BE_TYPED));
//        // TODO: search address
//        onView(withId(R.id.in_descrip)).perform(typeText(DESCRIPTION_TO_BE_TYPED));
//
//        onView(withId(R.id.cartypeSpinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(CARTYPE_TO_SELECT))).perform(click());
////        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));
//        onView(withId(R.id.cancelPolicySpinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(CANCEL_POLICY_TO_SELECT))).perform(click());
//        onView(withId(R.id.btn_confirm)).perform(click());
//    }

    @Test
    public void addNewTime() {
        // Assuming logged in as Renter, switch to Provider
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Switch to Provider")).perform(click());

        // Click on first spot and check that space detail frag shows up
        onData(hasToString(startsWith("100 Citadel Dr #480"))).perform(click());
        // get the text which the fragment shows
        ViewInteraction fragmentText = onView(withId(R.id.spacedetail_address));
        // check the fragments text is now visible in the activity
        fragmentText.check(ViewAssertions.matches(isDisplayed()));

        // Click on add time button
        onView(withId(R.id.btn_add_time)).perform(click());

        // Select new date
//        onView(withId(R.id.calendar_view)).perform()
    }

//    @Test
//    public void addTimeThatOverlapsWithExisting() {
//
//    }
}


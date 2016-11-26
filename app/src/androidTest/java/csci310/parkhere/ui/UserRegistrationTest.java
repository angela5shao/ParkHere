package csci310.parkhere.ui;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import csci310.parkhere.R;
import csci310.parkhere.ui.activities.HomeActivity;
import csci310.parkhere.ui.activities.ProviderActivity;
import csci310.parkhere.ui.activities.RegisterMainActivity;
import csci310.parkhere.ui.activities.RegisterProviderActivity;
import csci310.parkhere.ui.activities.RegisterRenterActivity;
import csci310.parkhere.ui.activities.RenterActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRegistrationTest {

    public static final String NAME_TO_BE_TYPED = "Renter9 USC";
    public static final String EMAIL_TO_BE_TYPED = "renter9@usc.edu";
    public static final String NAME2_TO_BE_TYPED = "Provider7 USC";
    public static final String EMAIL2_TO_BE_TYPED = "provider7@usc.edu";
    public static final String NAME3_TO_BE_TYPED = "Renter10 USC";
    public static final String EMAIL3_TO_BE_TYPED = "renter10@usc.edu";
    public static final String PASSWORD_SHORT_TO_BE_TYPED = "12345";
    public static final String PASSWORD_LONG_TO_BE_TYPED = ".123456789012";
    public static final String PHONE_TO_BE_TYPED = "2132132133";
    public static final String LICENSE_TO_BE_TYPED = "0909090";
    public static final String LICENSE_PLATE_TO_BE_TYPED = "089089089";



    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule(HomeActivity.class);
//    public IntentsTestRule<HomeActivity> mActivityRule = new IntentsTestRule(HomeActivity.class);

    @Before
    public void init() {
        Long tsLong = System.currentTimeMillis()/1000;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(tsLong);
//        System.out.println("START TIME: " + sdf.format(resultdate) + ":) :) :) :) :)");
        Intents.init();
    }

    @After
    public void after() {
        Long tsLong = System.currentTimeMillis()/1000;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(tsLong);
//        System.out.println("END TIME: " + sdf.format(resultdate) + ":) :) :) :) :)");
        Intents.release();
    }

    /*
    Tests that registration succeeds with unique email and long enough password.
     */
//    @Test
//    public void uniqueEmailLongPasswordRenterRegistration() {
//        // From HomeActivity, click on Login
//        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());
//
//        // Type name, email, password, phone.
//        onView(withId(R.id.nameText)).perform(typeText(NAME_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_LONG_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());
//
//        // Select "Renter". Then press button.
////        onView(withId(R.id.usertypeSpinner)).perform(click());
////        onData(allOf(is(instanceOf(String.class)), is("Renter"))).perform(click());
////        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));
//        onView(withId(R.id.registerNextButton)).perform(click());
//
//        // Check that intent to Renter (same as Provider) Registration Activity is called.
//        intended(hasComponent(RegisterRenterActivity.class.getName()));
//
//        // Type license ID & license plate number
//        onView(withId(R.id.liscenseIdText)).perform(typeText(LICENSE_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.liscensePlateNumText)).perform(typeText(LICENSE_PLATE_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.nextButton)).perform(click());
//
//        // Check that intent to Renter Activity is called.
//        intended(hasComponent(RenterActivity.class.getName()));
//
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Log Out")).perform(click());
//    }

    @Test
    public void uniqueEmailLongPasswordProviderRegistration() {
        // From HomeActivity, click on Login
        onView(withId(R.id.registerButton)).perform(click());
        intended(hasComponent(RegisterMainActivity.class.getName()));

        // Type name, email, password, phone.
        onView(withId(R.id.nameText)).perform(typeText(NAME2_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.emailText)).perform(typeText(EMAIL2_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_LONG_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());

        // Select "Provider". Then press button.
        onView(withId(R.id.usertypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Provider"))).perform(click());
//        onView(withId(R.id.usertypeSpinner)).check(matches(withSpinnerText(containsString("Renter"))));
        onView(withId(R.id.registerNextButton)).perform(click());

        // Check that intent to Renter (same as Provider) Registration Activity is called.
        intended(hasComponent(RegisterProviderActivity.class.getName()));

        // Type license IDe
        onView(withId(R.id.liscenseIdText)).perform(typeText(LICENSE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.nextButton)).perform(click());

        // Check that intent to Renter Activity is called.
        intended(hasComponent(ProviderActivity.class.getName()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());
    }

//    /*
//    Tests that registration fails with unique email and password that's too short.
//     */
//    @Test
//    public void uniqueEmailShortPasswordRegistration() {
//        // From HomeActivity, click on Login
//        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());
//
//        // Type name, email, password, phone.
//        onView(withId(R.id.nameText)).perform(typeText(NAME3_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL3_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_SHORT_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());
//
//        // Select "Renter". Then press button.
//        onView(withId(R.id.registerNextButton)).perform(click());
//
//        // Check that intent to Renter (same as Provider) Registration Activity is not called.
//        intended(hasComponent(HomeActivity.class.getName()));
//
////        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK,new Intent());
////
////        intending(hasComponent(HomeActivity.class.getName())).respondWith(intentResult);
////
//////        onView(withId(R.id.view_id_to_perform_clicking)).check(matches(isDisplayed())).perform(click());
////
////        intended(allOf(hasComponent(HomeActivity.class.getName())));
//////        onView(withText("Please input password longer than 10 digits")).inRoot(new ToastMatcher())
//////                .check(matches(isDisplayed()));
//    }

    /*
    Tests that registration fails with duplicate email.
     */
//    @Test
//    public void duplicateRegistration() {
//        // From HomeActivity, click on Login
//        onView(ViewMatchers.withId(R.id.registerButton)).perform(click());
//
//        // Type name, email, password, phone.
//        onView(withId(R.id.nameText)).perform(typeText(NAME_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_LONG_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.phoneText)).perform(typeText(PHONE_TO_BE_TYPED), closeSoftKeyboard());
//
//        // Select "Renter". Then press button.
//        onView(withId(R.id.registerNextButton)).perform(click());
//
//        // Check that intent to Renter (same as Provider) Registration Activity is not called.
//        intended(hasComponent(HomeActivity.class.getName()));
//    }
}
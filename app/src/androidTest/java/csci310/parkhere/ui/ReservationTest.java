package csci310.parkhere.ui;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import csci310.parkhere.R;
import csci310.parkhere.ui.activities.HomeActivity;
import csci310.parkhere.ui.activities.RenterActivity;

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
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Created by ivylinlaw on 11/7/16.
 */
public class ReservationTest {
    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890";
    public static final int SIZE_OF_FUTURE_RES_LIST = 1;
    public static final int SIZE_OF_PASSED_RES_LIST = 4;

    @Rule
    public IntentsTestRule<RenterActivity> mActivityRule = new IntentsTestRule(HomeActivity.class);

    @Before
    public void init() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login: type email and password, and then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // TODO: If in ProviderActivity, switch to RenterActivity
        try{
            intended(hasComponent(RenterActivity.class.getName()));
        }
        catch(AssertionError ae) {
            // Switch to renter
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText("Switch to Renter")).perform(click());
            intended(hasComponent(RenterActivity.class.getName()));
        }

        //get the text which the fragment shows
        ViewInteraction resFragmentText = onView(withId(R.id.reservationsTextView));

        //check the fragment text does not exist on fresh activity start
        resFragmentText.check(ViewAssertions.doesNotExist());

        // click on Reservation to show the fragment
        //resLink
//        onView(withText("Reservations")).perform(click());
        onView(withId(R.id.resLink)).perform(click());

        //check the fragments text is now visible in the activity
        resFragmentText.check(matches(isDisplayed()));

        //get the text which the fragment shows
        ViewInteraction resDetailFragmentText = onView(withId(R.id.reservationDetailTextView));

        //check the fragment text does not exist on fresh activity start
        resDetailFragmentText.check(ViewAssertions.doesNotExist());

        // check futureList size
        onView(withId(R.id.futureList)).check(matches(ListMatcher.withListSize(SIZE_OF_FUTURE_RES_LIST)));
        // check passedList size
        onView(withId(R.id.passedList)).check(matches(ListMatcher.withListSize(SIZE_OF_PASSED_RES_LIST)));
    }

//    @Test
//    public void checkReviewReservationDialog() {
//        //get the text which the fragment shows
//        ViewInteraction resDetailFragmentText = onView(withId(R.id.reservationDetailTextView));
//
//        //click on first item on reservation list
//        onData(anything()).inAdapterView(withId(R.id.passedList)).atPosition(3).perform(click());
//
//        //check the fragments text is now visible in the activity
//        resDetailFragmentText.check(matches(isDisplayed()));
//
//        //try if there is review button
//        try {
//            onView(withId(R.id.btn_review)).check(matches(isDisplayed()));
//        } catch(AssertionError ae) {
//            return;
//        }
//
//        //if there is review button
//        onView(withId(R.id.btn_review)).perform(click());
//
//        //check review dialog
//        onView(withId(R.id.review_label)).check(matches(isDisplayed()));
//    }

//    @Test
//    public void checkPassedReservationDetail() {
//        //get the text which the fragment shows
//        ViewInteraction resDetailFragmentText = onView(withId(R.id.reservationDetailTextView));
//
//        //click on first item on reservation list
//        onData(anything()).inAdapterView(withId(R.id.passedList)).atPosition(0).perform(click());
//
//        //check the fragments text is now visible in the activity
//        resDetailFragmentText.check(matches(isDisplayed()));
//
//        //check if there is no cancel button
//        onView(withId(R.id.btn_cancel)).check(matches(not(isDisplayed())));
//    }
//
    @Test
    public void checkFutureReservationDetail() {
        //get the text which the fragment shows
        ViewInteraction resDetailFragmentText = onView(withId(R.id.reservationDetailTextView));

        //click on first item on reservation list
        onData(anything()).inAdapterView(withId(R.id.futureList)).atPosition(0).perform(click());

        //check the fragments text is now visible in the activity
        resDetailFragmentText.check(matches(isDisplayed()));

        //check if there is cancel button
        onView(withId(R.id.btn_cancel)).check(matches(isDisplayed()));

        //check if there is no review button
        onView(withId(R.id.btn_review)).check(matches(not(isDisplayed())));
    }
}

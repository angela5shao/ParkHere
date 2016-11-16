package csci310.parkhere.ui;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

//import android.support.test.espresso.contrib.PickerActions;
//import static android.support.test.espresso.contrib.PickerActions;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest { //extends ActivityInstrumentationTestCase2<RenterActivity> {
    public static final String EMAIL_TO_BE_TYPED = "renter3@usc.edu";//"yy@yy.com"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "123456789012"; //"1234567890"; //"12345";
    public static final String PHONE_TO_BE_MATCHED = "2132132133";
    public static final String LICENSE_TO_BE_MATCHED = "0909090";
    public static final String LICENSE_PLATE_TO_BE_MATCHED = "089089089";

    public static final String ADDRESS_TO_BE_TYPED = "UC Berkeley";
    public static final String ADDRESS_LAT= "37.8719";
    public static final String ADDRESS_LONG= "-122.2585";
    public static final int START_YEAR = 2016;
    public static final int START_MONTH = 10;
    public static final int START_DAY = 17;
    public static final int START_HOUR = 15;
    public static final int START_MINUTE = 10;
    public static final int END_YEAR = 2016;
    public static final int END_MONTH = 10;
    public static final int END_DAY = 19;
    public static final int END_HOUR = 15;
    public static final int END_MINUTE = 10;
    public static final String SORT_OPTION_SPINNER_DEFAULT = "Sort by distance";

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

        // TODO: If RenterActivity check if SearchFragment

        // TODO: Click on Search on ActionBar and check if SearchFragment

        // Search: type address
//        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));

//        onView(withId(R.id.btn_add_address)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(ADDRESS_TO_BE_TYPED)))
//                .inRoot(RootMatchers.withDecorView(not(is(activityActivityTestRule
//                        .getActivity().getWindow().getDecorView()))))
//                .perform(click());
//        onView(withClassName(Matchers.equalTo(AutoCompleteTextView.class.getName())))
//                .perform(typeText(ADDRESS_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.usertypeSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Search by latitude, longitude"))).perform(click());
        onView(withId(R.id.latlongSearchLayout)).check(matches(isDisplayed()));

        // Type in lat & long
        onView(withId(R.id.in_lat)).perform(clearText(), typeText(ADDRESS_LAT));
        onView(withId(R.id.in_long)).perform(clearText(), typeText(ADDRESS_LONG));

        // Select start date
        onView(withId(R.id.btn_start_date)).perform(scrollTo(),click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(START_YEAR, START_MONTH, START_DAY));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());
        onView(withId(R.id.in_start_date)).check(matches(withText(START_DAY + "-" + START_MONTH + "-" + START_YEAR)));

        // Select end date
        onView(withId(R.id.btn_end_date)).perform(scrollTo(),click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(END_YEAR, END_MONTH, END_DAY));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());
        onView(withId(R.id.in_end_date)).check(matches(withText(END_DAY + "-" + END_MONTH + "-" + END_YEAR)));
    }

//    @Test
//    public void canSearchWithDatesOnly() {
//        //get the text which the fragment shows
//        ViewInteraction fragmentText = onView(withId(R.id.displaySearchTextView));
//
//        //check the fragment text does not exist on fresh activity start
//        fragmentText.check(ViewAssertions.doesNotExist());
//
//        //click the search button to show the search result fragment
//        onView(withId(R.id.btn_confirm)).perform(scrollTo(), click());
//
//        //check the fragments text is now visible in the activity
//        fragmentText.check(ViewAssertions.matches(isDisplayed()));
//
//        // Logout
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Log Out")).perform(click());
//    }
//
//    @Test
//    public void canSearchWithDatesAndTimes() {
//        // Select start time & end time
//        onView(withId(R.id.btn_start_time)).perform(scrollTo(),click());
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(START_HOUR, START_MINUTE));
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.in_start_time)).check(matches(withText(START_HOUR + ":" + START_MINUTE)));
//
//        onView(withId(R.id.btn_end_time)).perform(scrollTo(),click());
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(END_HOUR, END_MINUTE));
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.in_end_time)).check(matches(withText(END_HOUR + ":" + END_MINUTE)));
//
//        //click the search button to show the search result fragment
//        onView(withId(R.id.btn_confirm)).perform(scrollTo(), click());
//
//        //check the fragments text in search result fragment is now visible in the activity (ie check fragment is displayed)
//        ViewInteraction fragmentText = onView(withId(R.id.displaySearchTextView));
//        fragmentText.check(ViewAssertions.matches(isDisplayed()));
//
//        // Logout
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
//        onView(withText("Log Out")).perform(click());
//    }
//
    @Test
    public void checkPublicProfile() {
        //click the search button to show the search result fragment
        onView(withId(R.id.btn_confirm)).perform(scrollTo(), click());

        //click on first item on search result list
        onData(anything()).inAdapterView(withId(R.id.searchresultList)).atPosition(0).perform(click());

        // Click on profile item
        onView(withId(R.id.profilePic)).perform(click());

        // Check that email, phone number, and licence ID are correct
        onView(withId(R.id.usernameText)).check(matches(withText(EMAIL_TO_BE_TYPED)));
        onView(withId(R.id.licenseIDText)).check(matches(withText(LICENSE_TO_BE_MATCHED)));
        onView(withId(R.id.licenseplateText)).check(matches(withText(LICENSE_PLATE_TO_BE_MATCHED)));

        // Logout
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Log Out")).perform(click());
    }



    //    @Test
//    public void searchWithMoreThanOneCarTypes() {
//        // TODO
//    }
//
//    @Test
//    public void ensureSearchResultsWithin3Miles() {
//        // TODO: ??
//    }
//
//    @Test
//    public void validateFilterByPrice() {
//        //get the text which the fragment shows
//        ViewInteraction fragmentText = onView(withId(R.id.displaySearchTextView));
//
//        //check the fragment text does not exist on fresh activity start
//        fragmentText.check(ViewAssertions.doesNotExist());
//
//        //click the search button to show the search result fragment
//        onView(withId(R.id.btn_confirm)).perform(scrollTo(), click());
//
//        //check the fragments text is now visible in the activity
//        fragmentText.check(ViewAssertions.matches(isDisplayed()));
//
//        //check if sorting spinner exits in display search
//        onView(withId(R.id.sortOptionSpinner)).check(matches(withSpinnerText(containsString(SORT_OPTION_SPINNER_DEFAULT))));
//
//        //click on first item on search result list
//        onData(anything()).inAdapterView(withId(R.id.searchresultList)).atPosition(0).perform(click());
//    }
//
//    @Test
//    public void validateFilterBySpaceRating() {
//
//    }
//
//    @Test
//    public void validateFilterByProviderRating() {
//
//    }
//
}

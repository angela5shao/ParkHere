package csci310.parkhere.ui;

import android.support.test.filters.LargeTest;
//import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import csci310.parkhere.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.PickerActions;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {
    public static final String EMAIL_TO_BE_TYPED = "yy"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234"; //"12345";
    public static final String LAST_USER_ROLE = "Provider";
    public static final String ADDRESS_USER_ROLE = "UC Berkeley";

    @Rule
    public ActivityTestRule<RenterActivity> mActivityRule = new ActivityTestRule(RenterActivity.class);

    @Before
    public void init() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login: type email and password, and then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // TODO: If in ProviderActivity, switch to RenterActivity

        // Search: type address
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));

        // Select start date & time
        onView(withId(R.id.btn_start_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 8, 1));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
        onView(withText("OK")).perform(click());


        // Select end date & time
        onView(withId(R.id.btn_end_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 8, 30));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
        onView(withText("OK")).perform(click());
    }


    @Test
    public void searchWithMoreThanOneCarTypes() {
        // TODO
    }

    @Test
    public void ensureSearchResultsWithin3Miles() {
        // TODO: ??
    }

    @Test
    public void validateFilterByPrice() {

    }

    @Test
    public void validateFilterBySpaceRating() {

    }

    @Test
    public void validateFilterByProviderRating() {

    }

    @Test
    public void searchWithDates() {

    }
}

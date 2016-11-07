package csci310.parkhere.ui;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Matcher;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

//import android.support.test.espresso.contrib.PickerActions;
//import static android.support.test.espresso.contrib.PickerActions;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {
    public static final String EMAIL_TO_BE_TYPED = "yy@yy.com"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234567890"; //"12345";
    public static final String LAST_USER_ROLE = "Provider";
    public static final String ADDRESS_TO_BE_TYPED = "UC Berkeley";

    @Rule
    public ActivityTestRule<RenterActivity> mActivityRule = new ActivityTestRule(RenterActivity.class);

    @Before
    public static ViewAction setDate(final int day, final int month, final int year) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((DatePicker) view).updateDate(year, month, day);
            }

            @Override
            public String getDescription() {
                return "Set the date into the datepicker(day, month, year)";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(DatePicker.class);
            }
        };
    }

    public void init() {
        // From HomeActivity, click on Login
        onView(withId(R.id.loginButton)).perform(click());

        // Login: type email and password, and then press Login button
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
        onView(withId(R.id.passwordText)).perform(typeText(PASSWORD_CORRECT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // TODO: If in ProviderActivity, switch to RenterActivity
        intended(hasComponent(RenterActivity.class.getName()));

        // TODO: If RenterActivity check if SearchFragment

        // TODO: Click on Search on ActionBar and check if SearchFragment

        // Search: type address
        onView(withId(R.id.emailText)).perform(typeText(EMAIL_TO_BE_TYPED));
//        AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(
//                R.id.auto_complete_text_view);
//        String [] completions = new String[] {
//                "Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Southern Ocean", "Artic Ocean",
//                "Mediterranean Sea", "Caribbean Sea", "South China Sea", "Bering Sea",
//                "Gulf of Mexico", "Okhotsk Sea", "East China Sea", "Hudson Bay", "Japan Sea",
//                "Andaman Sea", "North Sea", "Red Sea", "Baltic Sea" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line,
//                completions);
//        autoComplete.setAdapter(adapter);

        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements

        //btn_start_date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 8, 25));
        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click());
//        onView(withId(R.id.emailText)).perform(typeText(ADDRESS_TO_BE_TYPED));

//        // Select start date & time
//        onView(withId(R.id.btn_start_date)).perform(click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 8, 1));
//        onView(withText("OK")).perform(click());
//
//        onView(withId(R.id.btn_start_time)).perform(click());
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
//        onView(withText("OK")).perform(click());
//
//
//        // Select end date & time
//        onView(withId(R.id.btn_end_date)).perform(click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2016, 8, 30));
//        onView(withText("OK")).perform(click());
//
//        onView(withId(R.id.btn_end_time)).perform(click());
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
//        onView(withText("OK")).perform(click());
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

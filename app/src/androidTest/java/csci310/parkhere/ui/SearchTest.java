package csci310.parkhere.ui;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {
    public static final String EMAIL_TO_BE_TYPED = "yy"; //"espressoeast@usc.edu";
    public static final String PASSWORD_CORRECT_TO_BE_TYPED = "1234"; //"12345";
    public static final String PASSWORD_INCORRECT_TO_BE_TYPED = "123456789012";
    public static final String LAST_USER_ROLE = "Provider";

    @Rule
    public ActivityTestRule<RenterActivity> mActivityRule = new ActivityTestRule(RenterActivity.class);

    @Test
    public void successfulLogin() {

    }
}

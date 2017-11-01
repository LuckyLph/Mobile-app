package ca.csf.mobile1.tp2.view.dialog;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KeyPickerDialogTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private OnKeySelectedListener onKeySelectedListener;

    @Before
    public void before() {
        onKeySelectedListener = mock(OnKeySelectedListener.class);
    }

    @Test
    public void canOpenKeyPickerDialog() throws Throwable {
        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());

        openKeyPickerDialog(0);

        onView(withId(R.id.keyPickerDialog)).check(matches(isDisplayed()));
    }

    @Test
    public void canPerformOkOnKeyPickerDialog() throws Throwable {
        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());

        openKeyPickerDialog(0);
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());
        verify(onKeySelectedListener).onKeySelected(anyInt());
    }

    @Test
    public void canPerformCancelOnKeyPickerDialog() throws Throwable {
        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());

        openKeyPickerDialog(0);
        onView(withId(android.R.id.button2)).perform(click());

        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());
        verify(onKeySelectedListener).onKeySelectionCancelled();
    }

    @Test
    public void canSetKeyOnKeyPickerDialog() throws Throwable {
        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());

        openKeyPickerDialog(15);
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.keyPickerDialog)).check(doesNotExist());
        verify(onKeySelectedListener).onKeySelected(eq(15));
    }

    private void openKeyPickerDialog(final int key) throws Throwable {
        activityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyPickerDialog.show(activityRule.getActivity(), key, 5, new OnKeySelectedListener[] {onKeySelectedListener});
            }
        });
    }

}
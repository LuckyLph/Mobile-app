package ca.csf.mobile1.tp2.activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.csf.mobile1.tp2.R;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.csf.mobile1.tp2.test.KeyPickerDialogActions.ok;
import static ca.csf.mobile1.tp2.test.KeyPickerDialogActions.setKey;
import static ca.csf.mobile1.tp2.test.OrientationChangeAction.orientationLandscape;
import static ca.csf.mobile1.tp2.test.OrientationChangeAction.orientationPortrait;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void canChangeKeyId() {
        show();

        openKeyPickerDialog();
        inputKeyInPickerDialog(11977);
        closeKeyPickerDialog();

        checkKeyIs(11977);
    }

    @Test
    public void canEncryptMessage() {
        show();
        openKeyPickerDialog();
        inputKeyInPickerDialog(11977);
        closeKeyPickerDialog();

        onView(withId(R.id.inputEditText)).perform(typeText("Tronald Dump"));
        closeSoftKeyboard();
        onView(withId(R.id.encryptButton)).perform(click());
        onView(withId(R.id.outputTextView)).check(matches(withText("Vayuw.rpnqlb")));
    }

    @Test
    public void canDecryptMessage() {
        show();
        openKeyPickerDialog();
        inputKeyInPickerDialog(11977);
        closeKeyPickerDialog();

        onView(withId(R.id.inputEditText)).perform(typeText("Vayuw.rpnqlb"));
        closeSoftKeyboard();
        onView(withId(R.id.decryptButton)).perform(click());
        onView(withId(R.id.outputTextView)).check(matches(withText("Tronald Dump")));
    }

    @Test
    public void canDisplayKey() {
        show();
        openKeyPickerDialog();
        inputKeyInPickerDialog(11977);
        closeKeyPickerDialog();

        onView(withId(R.id.keyTextView)).check(matches(withText(activityRule.getActivity().getResources().
                getString(R.string.text_key) + 11977)));
    }

    @Test
    public void canCopyOutputedText() {
        show();
        openKeyPickerDialog();
        inputKeyInPickerDialog(11977);
        closeKeyPickerDialog();
        onView(withId(R.id.inputEditText)).perform(typeText("Tronald Dump"));
        closeSoftKeyboard();
        onView(withId(R.id.encryptButton)).perform(click());
        onView(withId(R.id.copyButton)).perform(click());
        Assert.assertEquals(contentOfClipboard(), "Vayuw.rpnqlb");
    }

    @Test
    public void canReciveTextFromOtherApp() {
        show("Tronald Dump");
        closeKeyPickerDialog();
        onView(withId(R.id.inputEditText)).check(matches(withText("Tronald Dump")));
    }

    @Test
    public void willOpenKeyPickerDialogWhenRecivingTextFromOtherApp() {
        show("Tronald Dump");
        onView(withId(R.id.keyPickerDialog)).check(matches(isDisplayed()));
    }

    //BEN_CORRECTION : Pourquoi du PascalCase tout à coup ?
    @Test
    public void IsUsingSubstitution() {
        canEncryptMessage();
    }

    //BEN_CORRECTION : Pourquoi du PascalCase tout à coup ?
    //                 Aussi, faute d'anglais (pas pénalisé, je ne peux pas, loi 101...)
    @Test
    public void CantWrightBaloney() {
        show();
        onView(withId(R.id.inputEditText)).perform(replaceText(" |#/!$%?&*()_=+-«»°^¸[]{},';:`><¨±@£¢¤¬¦²³¼"));
        String test = onView(withId(R.id.inputEditText)).toString();
        onView(withId(R.id.inputEditText)).check(matches(withText(" ")));
    }

    private void show() {
        activityRule.launchActivity(null);
    }

    private void show(String textToDecrypt) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToDecrypt);

        activityRule.launchActivity(intent);
    }

    private void openKeyPickerDialog() {
        onView(withId(R.id.floatingActionButton)).perform(click());
    }

    private void inputKeyInPickerDialog(int key) {
        onView(withId(R.id.keyPickerDialog)).perform(setKey(key));
    }

    private void closeKeyPickerDialog() {
        onView(withId(R.id.keyPickerDialog)).perform(ok());
    }

    private void setOrientationLandscape() {
        onView(withId(R.id.rootView)).perform(orientationLandscape());
    }

    private void setOrientationPortrait() {
        onView(withId(R.id.rootView)).perform(orientationPortrait());
    }

    private void setKeyTo(int key) {
        openKeyPickerDialog();
        inputKeyInPickerDialog(key);
        closeKeyPickerDialog();
    }

    private void checkKeyIs(int key) {
        String keyText = activityRule.getActivity().getResources().getString(R.string.text_key);
        onView(withId(R.id.keyTextView)).check(matches(withText(keyText + key)));
    }

    private String contentOfClipboard() {
        ClipboardManager clipboard = (ClipboardManager) activityRule.getActivity().getSystemService(CLIPBOARD_SERVICE);
        return clipboard.getPrimaryClip().getItemAt(0).getText().toString();
    }

}

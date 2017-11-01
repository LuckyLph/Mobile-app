package ca.csf.mobile1.tp2.test;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.view.dialog.KeyPickerDialog;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class KeyPickerDialogActions {

    public static ViewAction setKey(final int keyValue) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "change key to " + keyValue;
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.setKey(keyValue);
            }
        };
    }

    public static ViewAction ok() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "performed OK";
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.performOk();
            }
        };
    }

    public static ViewAction cancel() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "performed Cancel";
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.performCancel();
            }
        };
    }

}

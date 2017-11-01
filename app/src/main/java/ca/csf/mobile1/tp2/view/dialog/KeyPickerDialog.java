package ca.csf.mobile1.tp2.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import ca.csf.mobile1.tp2.R;

public final class KeyPickerDialog {

    private static final int DEFAULT_KEY = 0;

    private final Context context;
    private final int keyLength;
    private final OnKeySelectedListener[] onKeySelectedListeners;

    private final List<TextView> textViews;
    private AlertDialog dialog;

    public static KeyPickerDialog show(Context context, int key, int keyLength, OnKeySelectedListener[] onKeySelectedListener) {
        KeyPickerDialog keyPickerDialog = new KeyPickerDialog(context, keyLength, onKeySelectedListener);
        keyPickerDialog.setKey(key);
        keyPickerDialog.show();

        return keyPickerDialog;
    }

    private KeyPickerDialog(Context context, int keyLength, OnKeySelectedListener[] onKeySelectedListeners) {
        this.context = context;
        this.keyLength = keyLength;
        this.onKeySelectedListeners = onKeySelectedListeners;

        this.textViews = new LinkedList<>();

        createView();
    }

    private void createView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        GridLayout gridLayout =  (GridLayout) layoutInflater.inflate(R.layout.dialog_key_picker, null);
        List<ImageButton> arrowUpButtons = new LinkedList<>();
        List<ImageButton> arrowDownButtons = new LinkedList<>();

        gridLayout.setColumnCount(keyLength);
        gridLayout.setTag(R.id.keyPickerDialogTag, this);

        //Three loops, because items are added to grid after inflation. Each loop represents a row.
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_arrow_up, gridLayout);
            arrowUpButtons.add((ImageButton) gridLayout.getChildAt(i));
        }
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_number, gridLayout);
            TextView textView = (TextView) gridLayout.getChildAt(i + keyLength);
            textViews.add(textView);
        }
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_arrow_down, gridLayout);
            arrowDownButtons.add((ImageButton) gridLayout.getChildAt(i + keyLength * 2));
        }

        for (int i = 0; i < keyLength; i++) {
            ImageButton arrowUpButton = arrowUpButtons.get(i);
            ImageButton arrowDownButton = arrowDownButtons.get(i);

            final int index = i;
            arrowUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowButtonClicked(index, 1);
                }
            });
            arrowDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArrowButtonClicked(index, -1);
                }
            });
        }

        setKey(DEFAULT_KEY);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.text_key);
        dialogBuilder.setView(gridLayout);

        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onOkButtonClicked();
            }
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCancelButtonClicked();
            }
        });

        dialog = dialogBuilder.create();
    }

    private void show() {
        dialog.show();
    }

    public void setKey(int key) {
        String keyAsString = String.format("%0" + keyLength + "d", key);
        for(int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setText(String.valueOf(keyAsString.charAt(i)));
        }
    }

    public void performOk() {
        onOkButtonClicked();
    }

    public void performCancel() {
        onCancelButtonClicked();
    }

    private void onArrowButtonClicked(int index, int increment) {
        TextView numberTextView = textViews.get(index);
        int currentValue = Integer.valueOf(numberTextView.getText().toString());
        currentValue = (currentValue + increment) % 10;
        currentValue = currentValue < 0 ? currentValue + 10 : currentValue;
        numberTextView.setText(String.valueOf(currentValue));
    }

    private void onOkButtonClicked() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TextView textView : textViews) {
            stringBuilder.append(textView.getText());
        }

        for (int i = 0; i < onKeySelectedListeners.length; i++){
            onKeySelectedListeners[i].onKeySelected(Integer.valueOf(stringBuilder.toString()));
        }

        dialog.dismiss();
    }

    private void onCancelButtonClicked() {
        for (int i = 0; i < onKeySelectedListeners.length; i++){
            onKeySelectedListeners[i].onKeySelectionCancelled();
        }

        dialog.cancel();
    }
}

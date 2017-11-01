package ca.csf.mobile1.tp2.view.input.filter;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class CharactersInputFilter implements InputFilter {

    private char[] acceptedCharacters;

    public CharactersInputFilter(char[] acceptedCharacters) {
        this.acceptedCharacters = acceptedCharacters;
    }

    public void setAcceptedCharacters(char[] acceptedCharacters) {
        this.acceptedCharacters = acceptedCharacters;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //Source code taken from class android.text.method.NumberKeyListener. Added little tweaks.
        int i;
        for (i = start; i < end; i++) {
            if (!isAccepted(source.charAt(i))) {
                break;
            }
        }

        if (i == end) {
            return null;
        }

        if (end - start == 1) {
            return "";
        }

        SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
        i -= start;
        end -= start;

        for (int j = end - 1; j >= i; j--) {
            if (!isAccepted(source.charAt(j))) {
                filtered.delete(j, j + 1);
            }
        }

        return filtered;
    }

    private boolean isAccepted(char charToVerify) {
        for (char currentChar : acceptedCharacters) {
            if (currentChar == charToVerify) {
                return true;
            }
        }
        return false;
    }

}

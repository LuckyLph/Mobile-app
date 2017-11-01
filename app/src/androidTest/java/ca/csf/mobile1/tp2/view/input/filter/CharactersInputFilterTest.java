package ca.csf.mobile1.tp2.view.input.filter;

import android.text.SpannableString;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharactersInputFilterTest {

    @Test
    public void filterAcceptsSentCharacters() {
        CharactersInputFilter charactersOnlyInputFilter = new CharactersInputFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SpannableString dest = new SpannableString("");
        assertNull(charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                    dest, 0, dest.length()));
    }

    @Test
    public void filterRefuseNonSentCharacters() {
        CharactersInputFilter charactersOnlyInputFilter = new CharactersInputFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "éèàÀÉÈ0123456789abcdefghijklmnopqrstuvwxyz";
        SpannableString dest = new SpannableString("");
        assertEquals("", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                          dest, 0, dest.length()).toString());
    }

    @Test
    public void filterRemovesUnwantedCharacters() {
        CharactersInputFilter charactersOnlyInputFilter = new CharactersInputFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "ABCD!#$%?{}[]@EFGH";
        SpannableString dest = new SpannableString("");
        assertEquals("ABCDEFGH", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                                  dest, 0, dest.length()).toString());
    }

}
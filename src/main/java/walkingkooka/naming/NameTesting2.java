/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Name} with mostly helpers to assert construction failure.
 */
public interface NameTesting2<N extends Name, C extends Comparable<C> & HashCodeEqualsDefined> extends NameTesting<N, C>{

    /**
     * All upper case ascii letters
     */
    final static String ASCII_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * All lower case ascii letters
     */
    final static String ASCII_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    /**
     * All ascii letters
     */
    final static String ASCII_LETTERS = ASCII_UPPERCASE + ASCII_LOWERCASE;

    /**
     * The digits 0 .. 9
     */
    final static String ASCII_DIGITS = "09123456789";

    /**
     * All ascii letters and digits
     */
    final static String ASCII_LETTERS_DIGITS = ASCII_LETTERS + ASCII_DIGITS;

    /**
     * Space or tab
     */
    final static String SPACE_HTAB = " \t";

    /**
     * Space, tab, CR and NL.
     */
    final static String WHITESPACE = SPACE_HTAB + "\r\n";

    /**
     * All ascii characters
     */
    final static String ASCII = IntStream.rangeClosed(0, 127)
            .mapToObj(c -> String.valueOf((char)c))
            .collect(Collectors.joining(""));

    /**
     * All control characters between 0 and 31 including tab, CR and NL.
     */
    final static String CONTROL = IntStream.rangeClosed(0, 31)
            .mapToObj(i -> String.valueOf((char)i))
            .collect(Collectors.joining(""));


    /**
     * All ascii characters from space (32) to 127.
     */
    final static String ASCII_NON_CONTROL = IntStream.rangeClosed(32, 127)
            .mapToObj(i -> String.valueOf((char)i))
            .collect(Collectors.joining(""));

    /**
     * <a href="https://tools.ietf.org/html/rfc2045#page-5"></a>
     * <pre>
     * token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
     *                  or tspecials>
     *
     *      tspecials :=  "(" / ")" / "<" / ">" / "@" /
     *                    "," / ";" / ":" / "\" / <">
     *                    "/" / "[" / "]" / "?" / "="
     *                    ; Must be in quoted-string,
     *                    ; to use within parameter values
     * </pre>
     */
    final static String RFC2045_TSPECIAL = "()<>@,;:\\\"/[]?=";

    /**
     * <a href="https://tools.ietf.org/html/rfc2045#page-5"></a>
     * <pre>
     * token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
     *                  or tspecials>
     *
     *      tspecials :=  "(" / ")" / "<" / ">" / "@" /
     *                    "," / ";" / ":" / "\" / <">
     *                    "/" / "[" / "]" / "?" / "="
     *                    ; Must be in quoted-string,
     *                    ; to use within parameter values
     * </pre>
     */
    final static String RFC2045 = subtract(ASCII,
            CONTROL + WHITESPACE + RFC2045_TSPECIAL);

    /**
     * All characters above 127 and 255.
     */
    final static String BYTE_NON_ASCII = IntStream.rangeClosed(128, 255)
            .mapToObj(i -> String.valueOf((char)i))
            .collect(Collectors.joining(""));

    /**
     * Subtracts all subtract characters from text.
     */
    static String subtract(final String text,
                           final String subtract) {
        final StringBuilder b = new StringBuilder();
        b.append(text);

        for(char c : subtract.toCharArray()) {
            final int i = b.indexOf(String.valueOf(c));
            if(-1 != i) {
                b.deleteCharAt(i);
            }
        }

        return b.toString();
    }

    @Test
    default void testNaming() {
        this.checkNaming(Name.class);
    }

    void checkNaming(Class<?>... name);

    @Test
    default void testPublicClass() {
        assertEquals(MemberVisibility.PUBLIC, this.typeVisibility(), "Visibility of name");
    }

    @Test
    default void testNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createName(null);
        });
    }

    @Test
    default void testEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createName("");
        });
    }

    @Test
    default void testWith() {
        this.createNameAndCheck(this.nameText());
    }

    // invalid / valid characters.........................................................................

    @Test
    default void testNameInvalidCharFails() {
        final int min = this.minLength();
        final int max = Math.min(this.maxLength(), 99);

        for (int i = min; i <= max; i++) {
            final char[] chars = new char[i];

            // fill with valid characters except for last with invalid.
            final int last = i -1;
            for(int j = 0; j < last; j++){
                final String s = this.possibleValidChars(j);
                chars[j] = this.possibleValidChars(j)
                        .charAt(0);
            }

            final String invalid = this.possibleInvalidChars(last);
            for(char c : invalid.toCharArray()) {
                chars[last] = c;
                final String text = new String(chars);

                final InvalidCharacterException expected = assertThrows(InvalidCharacterException.class, () -> {
                    this.createName(text);
                });
                assertEquals(last,
                        expected.position(),
                        () -> "Incorrect position reported " + CharSequences.quoteAndEscape(text));
            }
        }
    }

    /**
     * Try all combinations of valid characters in all positions between min and max lengths.
     */
    @Test
    default void testNameValidChars() {
        final int min = this.minLength();
        final int max = Math.min(this.maxLength(), 99);

        final int longest = IntStream.rangeClosed(0, max)
                .map(i -> this.possibleValidChars(i).length())
                .max()
                .getAsInt();

        for (int i = min; i <= max; i++) {
            final char[] chars = new char[i];

            for(int j = 0; j < longest; j++) {

                for(int k = 0; k < i; k++){
                    final String possible = this.possibleValidChars(k);
                    chars[k] = possible.charAt(j % (possible.length() -1));
                }
                this.createName(new String(chars));
            }
        }
    }

    @Test
    default void testNameMaxLength() {
        final int max = this.maxLength();
        if (max != Integer.MAX_VALUE) {
            final String chars = IntStream.rangeClosed(0, max + 1)
                    .mapToObj(i -> String.valueOf(this.possibleValidChars(i).charAt(0)))
                    .collect(Collectors.joining(""));
            assertThrows(IllegalArgumentException.class, () -> {
                this.createName(chars);
            });
        }
    }

    /**
     * A positive value at least equal or greater than 1.
     */
    int minLength();

    /**
     * Returns the max length or {@link Integer#MAX_VALUE} if one does not exist.
     */
    int maxLength();

    String possibleValidChars(final int position);

    String possibleInvalidChars(final int position);
}

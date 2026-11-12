/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.predicate.character;

import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Objects;

final public class CharPredicates implements PublicStaticHelper {

    /**
     * {@see CharPredicateBuilder}
     */
    public static CharPredicateBuilder builder() {
        return CharPredicateBuilder.empty();
    }

    /**
     * {@see CharPredicateAlways}.
     */
    public static CharPredicate always() {
        return CharPredicateAlways.INSTANCE;
    }

    /**
     * {@see CharPredicateLogicalAnd}.
     */
    public static CharPredicate and(final CharPredicate left, final CharPredicate right) {
        return CharPredicateLogicalAnd.wrap(left, right);
    }

    /**
     * {@see CharPredicateLogicalAndNot}.
     */
    public static CharPredicate andNot(final CharPredicate left, final CharPredicate right) {
        return CharPredicateLogicalAndNot.wrap(left, right);
    }

    /**
     * {@see CharPredicateAscii}
     */
    public static CharPredicate ascii() {
        return CharPredicateAscii.INSTANCE;
    }

    /**
     * {@see CharPredicateAsciiControl}
     */
    public static CharPredicate asciiControl() {
        return CharPredicateAsciiControl.INSTANCE;
    }

    /**
     * {@see CharPredicateAsciiPrintable}
     */
    public static CharPredicate asciiPrintable() {
        return CharPredicateAsciiPrintable.INSTANCE;
    }

    /**
     * {@see CharPredicateAny}
     */
    public static CharPredicate any(final String chars) {
        return CharPredicateAny.with(chars);
    }

    /**
     * {@see CaseSensitivity#INSENSITIVE#charPredicate}.
     */
    public static CharPredicate caseInsensitive(final char c) {
        return CaseSensitivity.INSENSITIVE.charPredicate(c);
    }

    /**
     * {@see CharPredicateCaseInsensitive}.
     */
    public static CharPredicate caseInsensitive(final CharPredicate predicate) {
        return CharPredicateCaseInsensitive.with(predicate);
    }

    /**
     * {@see CharPredicateDigit}.
     */
    public static CharPredicate digit() {
        return CharPredicateDigit.INSTANCE;
    }

    /**
     * {@see CharPredicateEndOfLine}.
     */
    public static CharPredicate endOfLine() {
        return CharPredicateEndOfLine.INSTANCE;
    }

    /**
     * {@see FakeCharPredicate}.
     */
    public static CharPredicate fake() {
        return FakeCharPredicate.create();
    }

    /**
     * Fails if the chars are null or empty or any characters fail the initial or part test.
     * It is assumed the {@link CharPredicate} have a meaningful toString as it is included in any exception messages.
     */
    public static <C extends CharSequence> C failIfNullOrEmptyOrInitialAndPartFalse(final C chars,
                                                                                    final String label,
                                                                                    final CharPredicate initial,
                                                                                    final CharPredicate part) {
        CharSequences.failIfNullOrEmpty(chars, label);
        CharSequences.failIfNullOrEmpty(label, "label");

        final int invalidChar = findInitialAndPartInvalidChar(
            chars,
            initial,
            part
        );

        if (-1 != invalidChar) {
            throw new InvalidCharacterException(
                chars.toString(),
                invalidChar
            );
        }

        return chars;
    }

    public static boolean isInitialAndPart(final String text,
                                           final CharPredicate initial,
                                           final CharPredicate part) {
        return !CharSequences.isNullOrEmpty(text) &&
            -1 == findInitialAndPartInvalidChar(
                text,
                initial,
                part
            );
    }

    /**
     * This helper does double duty and is used by both {@link #failIfNullOrEmptyOrInitialAndPartFalse(CharSequence, String, CharPredicate, CharPredicate)}
     * and {@link #isInitialAndPart(String, CharPredicate, CharPredicate)}, the former throws when
     * the result is not -1 and the latter returns false for a result of -1.
     */
    private static int findInitialAndPartInvalidChar(final CharSequence chars,
                                                     final CharPredicate initial,
                                                     final CharPredicate part) {
        Objects.requireNonNull(initial, "initial");
        Objects.requireNonNull(part, "part");

        int result = -1;

        final char first = chars.charAt(0);
        if (!initial.test(first)) {
            result = 0;
        } else {
            final int length = chars.length();
            for (int i = 1; i < length; i++) {
                final char c = chars.charAt(i);
                if (!part.test(c)) {
                    result = i;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * {@see CaseSensitivity#SENSITIVE#charPredicate}.
     */
    public static CharPredicate is(final char c) {
        return CaseSensitivity.SENSITIVE.charPredicate(c);
    }

    /**
     * {@see CharPredicateLetter}.
     */
    public static CharPredicate letter() {
        return CharPredicateLetter.INSTANCE;
    }

    /**
     * {@see CharPredicateLetterOrDigit}.
     */
    public static CharPredicate letterOrDigit() {
        return CharPredicateLetterOrDigit.INSTANCE;
    }

    /**
     * {@see CharPredicateLowerCasing}.
     */
    public static CharPredicate lowerCasing(final CharPredicate predicate) {
        return CharPredicateLowerCasing.wrap(predicate);
    }

    /**
     * {@see CharPredicateNever}.
     */
    public static CharPredicate never() {
        return CharPredicateNever.INSTANCE;
    }

    /**
     * {@see CharPredicateNot}.
     */
    public static CharPredicate not(final CharPredicate charPredicate) {
        return CharPredicateNot.wrap(charPredicate);
    }

    /**
     * {@see CharPredicateLogicalOr}.
     */
    public static CharPredicate or(final CharPredicate left, final CharPredicate right) {
        return CharPredicateLogicalOr.wrap(left, right);
    }

    /**
     * {@see CharPredicatePrintable}
     */
    public static CharPredicate printable() {
        return CharPredicatePrintable.INSTANCE;
    }

    /**
     * {@see CharPredicateRange}
     */
    public static CharPredicate range(final char start, final char end) {
        return CharPredicateRange.with(start, end);
    }

    /**
     * {@see CharPredicateRfc2045Token}
     */
    public static CharPredicate rfc2045Token() {
        return CharPredicateRfc2045Token.INSTANCE;
    }

    /**
     * {@see CharPredicateRfc2045TokenSpecial}
     */
    public static CharPredicate rfc2045TokenSpecial() {
        return CharPredicateRfc2045TokenSpecial.INSTANCE;
    }

    /**
     * {@see CharPredicateToString}.
     */
    public static CharPredicate toString(final CharPredicate predicate, final String toString) {
        return CharPredicateToString.wrap(predicate, toString);
    }

    /**
     * {@see CharPredicateWhitespace}.
     */
    public static CharPredicate whitespace() {
        return CharPredicateWhitespace.INSTANCE;
    }

    /**
     * {@see CharPredicateXml}.
     */
    public static CharPredicate xml() {
        return CharPredicateXml.INSTANCE;
    }

    /**
     * Stop creation
     */
    private CharPredicates() {
        throw new UnsupportedOperationException();
    }
}

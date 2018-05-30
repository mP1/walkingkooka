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
 */

package walkingkooka.text;

import java.util.Objects;

/**
 * Base class for any {@link CharSequence} that adds padding.
 */
abstract class PaddedCharSequence<S extends PaddedCharSequence<S>> extends CharSequenceTemplate<S> {

    private static final long serialVersionUID = 5307971165744641577L;

    /**
     * Checks the validity of any {@link PaddedCharSequence} factory parameters.
     */
    static void check(final CharSequence chars, final int length) {
        Objects.requireNonNull(chars, "chars");

        final int charsLength = chars.length();
        if (length < charsLength) {
            throw new IllegalArgumentException("Length " + length + " < " + charsLength);
        }
    }

    /**
     * Package private constructor to limit sub classing.
     */
    PaddedCharSequence(final CharSequence chars, final int length, final char pad) {
        super();

        this.chars = chars;
        this.paddingLength = length;
        this.pad = pad;
    }

    /**
     * Invokes {@link #charAtIndex(int, int)}.
     */
    @Override final char charAtIndex(final int index) {
        return this.charAtIndex(index, this.paddingLength);
    }

    abstract char charAtIndex(int index, int length);

    /**
     * Added the wrapped {@link String} length and the padded length
     */
    @Override final public int length() {
        return this.paddingLength + this.charsLength();
    }

    /**
     * Invokes {@link #doSubSequence(int, int, int)}
     */
    @Override final CharSequence doSubSequence(final int start, final int end) {
        return this.doSubSequence(start, end, this.paddingLength);
    }

    abstract CharSequence doSubSequence(int start, int end, int paddingLength);

    @Override final protected int calculateHashCode() {
        return this.paddingLength ^ this.pad ^ this.charsHashCode();
    }

    /**
     * Provided so sub classes with a chars offset and length can replace.
     */
    int charsHashCode() {
        return this.chars.hashCode();
    }

    /**
     * Uses the {@link #charsOffset()} and {@link #charsLength()} to calculate the hash code of the
     * wrapped {@link CharSequence}.
     */
    final int slowSequenceHashCode() {
        int hashCode = 0;

        int offset = this.charsOffset();
        final int length = this.charsLength();
        final CharSequence chars = this.charSequence();

        for (int i = 0; i < length; i++) {
            hashCode = (31 * hashCode) + chars.charAt(offset++);
        }
        return hashCode;
    }

    @Override final boolean doEquals(final S padded) {
        return (this.paddingLength == padded.paddingLength) && (this.pad == padded.pad)
                && this.charsEquality(padded);
    }

    /**
     * Provided so sub classes with a chars offset and length can replace.
     */
    boolean charsEquality(final S padded) {
        return this.chars.equals(padded.chars);
    }

    /**
     * Uses the {@link #charsOffset()} and {@link #charsLength()} to testing equality using the
     * wrapped {@link CharSequence}.
     */
    final boolean slowSequenceEquality(final PaddedCharSequence<?> padded) {
        boolean equals = false;

        final int length = this.charsLength();
        final int length2 = padded.charsLength();
        if (length == length2) {
            final CharSequence chars = this.charSequence();
            final CharSequence other = padded.charSequence();

            final int offset = this.charsOffset();
            final int offset2 = padded.charsOffset();

            equals = true;

            for (int i = 0; i < length; i++) {
                if (chars.charAt(i + offset) != other.charAt(i + offset2)) {
                    equals = false;
                    break;
                }
            }
        }

        return equals;
    }

    // properties

    /**
     * The wrapped {@link CharSequence}.
     */
    final CharSequence chars;

    /**
     * The wrapped {@link CharSequence}
     */
    final CharSequence charSequence() {
        return this.chars;
    }

    /**
     * The offset of the first interesting character in the chars.
     */
    int charsOffset() {
        return 0;
    }

    /**
     * The true {@link CharSequence} length. This method will be overridden by sub classes that also
     * include override {@link #charsOffset()} .
     */
    int charsLength() {
        return this.chars.length();
    }

    /**
     * The number of padding characters to pad
     */
    final int paddingLength;

    /**
     * The padding character that appears before or after the wrapped {@link CharSequence}.
     */
    final char pad() {
        return this.pad;
    }

    final char pad;
}

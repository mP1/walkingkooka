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

/**
 * A {@link CharSequence} that adds of or more padding characters after another {@link
 * CharSequence}
 */
final class RightPaddedCharSequence2 extends RightPaddedCharSequence {

    private static final long serialVersionUID = -4798137668463106194L;

    static RightPaddedCharSequence2 wrap(final CharSequence chars, final int charsOffset,
                                         final int charsLength, final int paddingLength, final char pad) {
        return new RightPaddedCharSequence2(chars, charsOffset, charsLength, paddingLength, pad);
    }

    /**
     * Private constructor use static factory.
     */
    private RightPaddedCharSequence2(final CharSequence chars, final int charsOffset,
                                     final int charsLength, final int paddingLength, final char pad) {
        super(chars, paddingLength, pad);

        this.charsOffset = charsOffset;
        this.charsLength = charsLength;
    }

    /**
     * Overridden to calculate the hash code for only the interested characters between offset and
     * for length.
     */
    @Override
    int charsHashCode() {
        return this.slowSequenceHashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof RightPaddedCharSequence2;
    }

    @Override
    boolean charsEquality(final RightPaddedCharSequence padded) {
        return this.slowSequenceEquality(padded);
    }

    @Override
    int charsOffset() {
        return this.charsOffset;
    }

    private final int charsOffset;

    /**
     * Returns the true character count of the wrapped {@link CharSequence}.
     */
    @Override
    int charsLength() {
        return this.charsLength;
    }

    private final int charsLength;
}

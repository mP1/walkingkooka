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
 * A {@link CharSequence} that adds of or more padding characters before another {@link
 * CharSequence}.
 */
final class LeftPaddedCharSequence2 extends LeftPaddedCharSequence {

    private static final long serialVersionUID = -349125965243161271L;

    static LeftPaddedCharSequence2 wrap(final CharSequence chars, final int charsOffset,
                                        final int charsLength, final int paddingLength, final char pad) {
        return new LeftPaddedCharSequence2(chars, charsOffset, charsLength, paddingLength, pad);
    }

    /**
     * Private constructor use static factory.
     */
    private LeftPaddedCharSequence2(final CharSequence chars, final int charsOffset,
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
        return other instanceof LeftPaddedCharSequence2;
    }

    @Override
    boolean charsEquality(final LeftPaddedCharSequence padded) {
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

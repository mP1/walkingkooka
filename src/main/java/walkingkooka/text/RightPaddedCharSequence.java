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
 * CharSequence}. Note the length parameter is the desired length after padding has been applied.
 */
class RightPaddedCharSequence extends PaddedCharSequence<RightPaddedCharSequence> {

    private static final long serialVersionUID = -283758475997672409L;

    static CharSequence wrap(final CharSequence chars, final int length, final char pad) {
        PaddedCharSequence.check(chars, length);

        final int requiredPadding = length - chars.length();
        return requiredPadding == 0 ?
                chars :
                new RightPaddedCharSequence(chars, requiredPadding, pad);
    }

    /**
     * Package private constructor use static factory.
     */
    RightPaddedCharSequence(final CharSequence chars, final int paddingLength, final char pad) {
        super(chars, paddingLength, pad);
    }

    /**
     * If index is less than the padded length returns the padding character otherwise returns from
     * the wrapped chars.
     */
    @Override final char charAtIndex(final int index, final int length) {
        return index < this.charsLength() ?
                this.charSequence().charAt(this.charsOffset() + index) :
                this.pad();
    }

    /**
     * Returns another {@link RightPaddedCharSequence} with a new length but the same repeated
     * character.
     */
    @Override
    CharSequence doSubSequence(final int start, final int end, final int paddingLength) {
        CharSequence result;

        for (; ; ) {
            // sub chars is wholly in the wrapped CharSequence
            final int charsLength = this.charsLength();
            final CharSequence chars = this.charSequence();
            if (end <= charsLength) {
                final int offset = this.charsOffset();
                result = chars.subSequence(offset + start, offset + end);
                break;
            }

            // sub chars is wholly in the padding area
            final char pad = this.pad();
            if (start >= charsLength) {
                result = CharSequences.repeating(pad, end - start);
                break;
            }

            // sub chars includes some padding create another RightPaddedCharSequence
            final int offset = this.charsOffset();
            result = RightPaddedCharSequence2.wrap(chars,
                    offset + start,
                    charsLength - start,
                    end - charsLength,
                    pad);
            break;
        }

        return result;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return (other instanceof RightPaddedCharSequence) && (false
                == (other instanceof RightPaddedCharSequence2));
    }
}

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
 * CharSequence}. Note the length parameter is the desired length after padding has been applied.
 */
class LeftPaddedCharSequence extends PaddedCharSequence<LeftPaddedCharSequence> {

    private static final long serialVersionUID = -5985057118653331657L;

    static CharSequence wrap(final CharSequence chars, final int length, final char pad) {
        PaddedCharSequence.check(chars, length);

        final int requiredPadding = length - chars.length();
        return requiredPadding == 0 ?
                chars :
                new LeftPaddedCharSequence(chars, requiredPadding, pad);
    }

    /**
     * Package private constructor use static factory.
     */
    LeftPaddedCharSequence(final CharSequence chars, final int paddingLength, final char pad) {
        super(chars, paddingLength, pad);
    }

    /**
     * If index is less than the padded length returns the padding character otherwise returns from
     * the wrapped chars.
     */
    @Override final char charAtIndex(final int index, final int length) {
        return index < length ?
                this.pad() :
                this.charSequence().charAt((this.charsOffset() + index) - length);
    }

    /**
     * Returns another {@link LeftPaddedCharSequence} with a new length but the same repeated
     * character.
     */
    @Override final CharSequence doSubSequence(final int start, final int end, final int paddingLength) {
        CharSequence result;

        for (; ; ) {
            // sub chars is wholly in the padding only
            if (end <= paddingLength) {
                result = CharSequences.repeating(this.pad(), end);
                break;
            }

            final CharSequence chars = this.charSequence();
            final int offset = this.charsOffset();

            // sub chars is wholly in the wrapped CharSequence
            if (start >= paddingLength) {
                result = chars.subSequence((offset + start) - paddingLength,
                        (offset + end) - paddingLength);
                break;
            }
            result = LeftPaddedCharSequence2.wrap(//
                    chars,// chars
                    offset, // chars offset
                    end - paddingLength, // chars length
                    paddingLength - start, // padding length
                    this.pad());
            break;
        }

        return result;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return (other instanceof LeftPaddedCharSequence) && (false
                == (other instanceof LeftPaddedCharSequence2));
    }
}

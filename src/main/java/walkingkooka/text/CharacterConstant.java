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

import walkingkooka.test.HashCodeEqualsDefined;

/**
 * Holds both a character and its {@link String} equivalent. This is ideal for constants that should
 * not be inlined and is intended to replace dual declaration of constants of for the character and
 * another to hold the {@link String} equivalent.
 */
final public class CharacterConstant implements CharSequence, HashCodeEqualsDefined {

    final static char LOWER_BOUNDS = '\n';

    final static char UPPER_BOUNDS = 'z';

    /**
     * The cache holds only printables between {@link #LOWER_BOUNDS} and {@link #UPPER_BOUNDS}.
     */
    private final static CharacterConstant[] CONSTANTS = CharacterConstant.fill();

    private static CharacterConstant[] fill() {
        final CharacterConstant[] constants = new CharacterConstant[
                (CharacterConstant.UPPER_BOUNDS + 1) - CharacterConstant.LOWER_BOUNDS];
        for (char c = CharacterConstant.LOWER_BOUNDS; c < (CharacterConstant.UPPER_BOUNDS + 1); c++) {
            constants[c - CharacterConstant.LOWER_BOUNDS] = new CharacterConstant(c);
        }
        return constants;
    }

    /**
     * Factory that returns a {@link CharacterConstant} which may be shared/cached.
     */
    public static CharacterConstant with(final char c) {
        return (c >= CharacterConstant.LOWER_BOUNDS) && (c <= CharacterConstant.UPPER_BOUNDS) ?
                CharacterConstant.CONSTANTS[c - CharacterConstant.LOWER_BOUNDS] :
                new CharacterConstant(c);
    }

    private CharacterConstant(final char character) {
        super();
        this.character = character;
        this.string = String.valueOf(character);
    }

    public char character() {
        return this.character;
    }

    private final char character;

    public String string() {
        return this.string;
    }

    private final String string;

    // SubSequence

    @Override
    public int length() {
        return 1;
    }

    @Override
    public char charAt(final int index) {
        if (0 != index) {
            this.failIndexOfOutBounds("index must be 0");
        }
        return this.character;
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        if (0 != start) {
            this.failIndexOfOutBounds("start must be 0 but was " + start);
        }
        return 0 == end ? "" : 1 == end ? this : this.failSubSequence(end);
    }

    private CharSequence failSubSequence(final int end) {
        this.failIndexOfOutBounds("end must be 0 or 1 but was " + end);
        return null; // never happens
    }

    private void failIndexOfOutBounds(final String message) {
        throw new StringIndexOutOfBoundsException(message);
    }

    // HasHashCodeAndEqualsDefined

    @Override
    public int hashCode() {
        return this.character;
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || //
                ((other instanceof Character) && this.equals0((Character) other)) || //
                ((other instanceof String) && this.equals1((String) other)) || //
                ((other instanceof CharacterConstant) && this.equals2((CharacterConstant) other));
    }

    private boolean equals0(final Character c) {
        return this.equals(c.charValue());
    }

    private boolean equals1(final String string) {
        return this.string.equals(string);
    }

    private boolean equals2(final CharacterConstant other) {
        return this.equals(other.character);
    }

    public boolean equals(final char c) {
        return this.character == c;
    }

    @Override
    public String toString() {
        return this.string;
    }
}

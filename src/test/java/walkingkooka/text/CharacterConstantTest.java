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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class CharacterConstantTest implements ClassTesting2<CharacterConstant>,
    CharSequenceTesting2<CharacterConstant> {

    private final static char CHAR = 'a';

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWith() {
        final char c = 0x200;
        this.check(CharacterConstant.with(c), c);
    }

    @Test
    public void testCachedLowerBounds() {
        this.checkCached(CharacterConstant.LOWER_BOUNDS);
    }

    @Test
    public void testCachedBetweenBounds() {
        this.checkCached((char) ((CharacterConstant.LOWER_BOUNDS + CharacterConstant.UPPER_BOUNDS)
            / 2));
    }

    @Test
    public void testCachedUpperBounds() {
        this.checkCached(CharacterConstant.UPPER_BOUNDS);
    }

    @Test
    public void testCachedComma() {
        assertSame(
            CharacterConstant.COMMA,
            CharacterConstant.with(',')
        );
    }

    private void checkCached(final char c) {
        final CharacterConstant constant = CharacterConstant.with(c);
        assertSame(constant, CharacterConstant.with(c), "not cached");

        this.check(constant, c);
    }

    private void check(final CharacterConstant constant, final char c) {
        this.checkEquals(c, constant.character(), "character");
        this.checkEquals(String.valueOf(c), constant.string(), "string");
    }

    // toDelimiteredString..............................................................................................

    @Test
    public void testToDelimiteredStringWithComma() {
        this.toDelimiteredStringAndCheck(
            CharacterConstant.COMMA,
            Lists.of("11", "22", "33"),
            "11,22,33"
        );
    }

    @Test
    public void testToDelimiteredStringWithSemiColon() {
        this.toDelimiteredStringAndCheck(
            CharacterConstant.with(';'),
            Lists.of("11", "22", "33"),
            "11;22;33"
        );
    }

    private void toDelimiteredStringAndCheck(final CharacterConstant characterConstant,
                                             final Iterable<String> values,
                                             final String expected) {
        this.toDelimiteredStringAndCheck(
            characterConstant,
            values,
            characterConstant.constantSeparator(),
            expected
        );
    }

    private void toDelimiteredStringAndCheck(final CharacterConstant characterConstant,
                                             final Iterable<String> values,
                                             final IntFunction<CharSequence> separator,
                                             final String expected) {
        this.checkEquals(
            expected,
            characterConstant.toDelimiteredString(
                values,
                separator
            ),
            () -> characterConstant + " " + values + " " + separator
        );
    }

    // Object..........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(CharacterConstant.with((char) (CHAR
            + 1)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharacterConstant.with('a'), "a");
    }

    // CharSequence.....................................................................................................

    @Override
    public CharacterConstant createCharSequence() {
        return CharacterConstant.with(CHAR);
    }

    @Override
    public CharacterConstant createObject() {
        return this.createCharSequence();
    }

    @Override
    public Class<CharacterConstant> type() {
        return CharacterConstant.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

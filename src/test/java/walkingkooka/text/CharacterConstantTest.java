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
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharacterConstantTest implements ParseStringTesting<List<Integer>>,
    ClassTesting2<CharacterConstant>,
    CharSequenceTesting<CharacterConstant> {

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

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseEmpty() {
        this.parseStringAndCheck(
            "",
            Lists.empty()
        );
    }

    @Test
    public void testParseFunctionFails() {
        this.parseStringFails(
            "123,abc",
            new IllegalArgumentException("Unable to parse \"123,abc\", For input string: \"abc\"")
        );
    }

    @Test
    public void testParseFunctionThrowsInvalidCharacterExceptionFails() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> CharacterConstant.with('.')
                .parse(
                    "123,45*",
                    (s) -> {
                        throw new InvalidCharacterException(s, 3);
                    }
                )
        );
        this.checkEquals(
            "Invalid character ',' at 3 in \"123,45*\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseFunctionThrowsInvalidCharacterExceptionFails2() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> CharacterConstant.with(',')
                .parse(
                    "123,45*",
                    (s) -> {
                        if (s.equals("123")) {
                            return 123;
                        }
                        throw new InvalidCharacterException(s, 2);
                    }
                )
        );
        this.checkEquals(
            "Invalid character '*' at 6 in \"123,45*\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testParseOne() {
        this.parseStringAndCheck(
            "123",
            Lists.of(
                123
            )
        );
    }

    @Test
    public void testParseTwo() {
        this.parseStringAndCheck(
            "123,456",
            Lists.of(
                123,
                456
            )
        );
    }

    @Test
    public void testParseThree() {
        this.parseStringAndCheck(
            "123,456,789",
            Lists.of(
                123,
                456,
                789
            )
        );
    }

    // toSeparatedString............................................................................................................

    @Test
    public void testToSeparatedStringEmpty() {
        this.toSeparatedStringAndCheck(
            Lists.empty(),
            Function.identity(),
            ""
        );
    }

    @Test
    public void testToSeparatedStringOne() {
        this.toSeparatedStringAndCheck(
            Lists.of("abc"),
            s -> CharSequences.quote(s).toString(),
            "\"abc\""
        );
    }

    @Test
    public void testToSeparatedStringSeveral() {
        this.toSeparatedStringAndCheck(
            Lists.of("abc", "def", "ghi"),
            s -> CharSequences.quote(s).toString(),
            "\"abc\",\"def\",\"ghi\""
        );
    }

    private <T> void toSeparatedStringAndCheck(final Collection<T> values,
                                               final Function<T, String> component,
                                               final String expected) {
        this.checkEquals(
            expected,
            CharacterConstant.with(',')
                .toSeparatedString(
                    values,
                    component
                )
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public List<Integer> parseString(final String text) {
        return CharacterConstant.with(',')
            .parse(
                text,
                Integer::parseInt
            );
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
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

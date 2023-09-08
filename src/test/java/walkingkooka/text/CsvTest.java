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
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.test.ParseStringTesting;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CsvTest implements ParseStringTesting<List<Integer>>, PublicStaticHelper {

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
                () -> Csv.parseCsv(
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

    // toCsv............................................................................................................

    @Test
    public void testToCsvEmpty() {
        this.toCsvAndCheck(
                Lists.empty(),
                Function.identity(),
                ""
        );
    }

    @Test
    public void testToCsvOne() {
        this.toCsvAndCheck(
                Lists.of("abc"),
                s -> CharSequences.quote(s).toString(),
                "\"abc\""
        );
    }

    @Test
    public void testToCsvSeveral() {
        this.toCsvAndCheck(
                Lists.of("abc", "def", "ghi"),
                s -> CharSequences.quote(s).toString(),
                "\"abc\",\"def\",\"ghi\""
        );
    }

    private <T> void toCsvAndCheck(final List<T> values,
                                   final Function<T, String> component,
                                   final String expected) {
        this.checkEquals(
                expected,
                Csv.toCsv(
                        values,
                        component
                )
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public List<Integer> parseString(final String text) {
        return Csv.parseCsv(
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
}

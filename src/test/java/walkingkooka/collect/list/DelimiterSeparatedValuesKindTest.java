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

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.reflect.ThrowableTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DelimiterSeparatedValuesKindTest implements PublicClassTesting<DelimiterSeparatedValuesKind<?>>,
    ThrowableTesting {

    // character........................................................................................................

    @Test
    public void testCharacterWithComma() {
        this.characterAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            ','
        );
    }

    @Test
    public void testCharacterWithTab() {
        this.characterAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            '\t'
        );
    }

    private void characterAndCheck(final DelimiterSeparatedValuesKind<?> values,
                                   final char expected) {
        this.checkEquals(
            expected,
            values.character()
        );
    }

    // setElements......................................................................................................

    private final static List<String> ELEMENTS = Lists.of(
        "AA",
        "BB",
        "CC"
    );

    @Test
    public void testSetElementsWithComma() {
        this.setElementsAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            ELEMENTS,
            CsvStringList.EMPTY.setElements(ELEMENTS)
        );
    }

    @Test
    public void testSetElementsWithTab() {
        this.setElementsAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            ELEMENTS,
            TsvStringList.EMPTY.setElements(ELEMENTS)
        );
    }

    private <T extends CsvTsvStringList<T>> void setElementsAndCheck(final DelimiterSeparatedValuesKind<T> values,
                                                                     final List<String> elements,
                                                                     final T expected) {
        this.checkEquals(
            expected,
            values.setElements(elements)
        );
    }

    // empty............................................................................................................

    @Test
    public void testEmptyWithComma() {
        this.emptyAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            CsvStringList.EMPTY
        );
    }

    @Test
    public void testEmptyWithTab() {
        this.emptyAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            TsvStringList.EMPTY
        );
    }

    private <T extends CsvTsvStringList<T>> void emptyAndCheck(final DelimiterSeparatedValuesKind<T> values,
                                                               final T expected) {
        this.checkEquals(
            expected,
            values.empty()
        );
    }

    // escape...........................................................................................................

    @Test
    public void testEscapeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> DelimiterSeparatedValuesKind.COMMA.escape(null)
        );
    }

    @Test
    public void testEscapeWithEmptyString() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "",
            ""
        );
    }

    @Test
    public void testEscapeWithCharactersNotRequiringEscaping() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "abc",
            "abc"
        );
    }

    @Test
    public void testEscapeWithCommaWithCharactersRequiringQuotes() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "ab,c\t",
            "\"ab,c\t\""
        );
    }

    @Test
    public void testEscapeWithTabWithCharactersNotRequiringQuotes() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            "ab,c",
            "ab,c"
        );
    }

    @Test
    public void testEscapeWithTabWithCharactersRequiringQuotes() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            "ab,c\t",
            "\"ab,c\t\""
        );
    }

    @Test
    public void testEscapeWithCharactersRequiringEscaping() {
        this.escapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "ab\"c",
            "\"ab\"\"c\""
        );
    }

    private void escapeAndCheck(final DelimiterSeparatedValuesKind<?> kind,
                                final String string,
                                final String expected) {
        this.checkEquals(
            expected,
            kind.escape(string),
            () -> string
        );
    }

    // unescape.........................................................................................................

    @Test
    public void testUnescapeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> DelimiterSeparatedValuesKind.COMMA.unescape(null)
        );
    }

    @Test
    public void testUnescapeWithOnlyDoubleQuoteFails() {
        assertThrows(
            InvalidCharacterException.class,
            () -> DelimiterSeparatedValuesKind.COMMA.unescape("\"")
        );
    }

    @Test
    public void testUnescapeWithMissingTerminatingDoubleQuoteFails() {
        this.getMessageAndCheck(
            assertThrows(
                InvalidCharacterException.class,
                () -> DelimiterSeparatedValuesKind.COMMA.unescape("\"missing")
            ),
            "Invalid character 'g' at 7"
        );
    }

    @Test
    public void testUnescapeWithEmptyString() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "",
            ""
        );
    }

    @Test
    public void testUnescapeWithCharactersNotRequiringEscaping() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "abc",
            "abc"
        );
    }

    @Test
    public void testUnescapeWithCommaWithTab() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "abc\t",
            "abc\t"
        );
    }

    @Test
    public void testUnescapeWithTabWithComma() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            "ab,c",
            "ab,c"
        );
    }

    @Test
    public void testUnescapeWithTabWithCharactersRequiringQuotes() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            "\"ab,c\t\"",
            "ab,c\t"
        );
    }

    @Test
    public void testUnescapeWithDoubleQuoted() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "\"Hello\"",
            "Hello"
        );
    }

    @Test
    public void testUnescapeWithDoubleQuotedAndEscapedQuotes() {
        this.unescapeAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            "\"Hel\"\"lo\"",
            "Hel\"lo"
        );
    }

    private void unescapeAndCheck(final DelimiterSeparatedValuesKind<?> kind,
                                  final String string,
                                  final String expected) {
        this.checkEquals(
            expected,
            kind.unescape(string),
            () -> string
        );
    }
    
    // class............................................................................................................

    @Override
    public Class<DelimiterSeparatedValuesKind<?>> type() {
        return Cast.to(DelimiterSeparatedValuesKind.class);
    }
}

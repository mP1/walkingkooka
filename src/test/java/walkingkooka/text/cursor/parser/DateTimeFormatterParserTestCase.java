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

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.type.JavaVisibility;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class DateTimeFormatterParserTestCase<P extends DateTimeFormatterParser<FakeParserContext>, T extends ParserToken>
        implements ClassTesting2<P>,
        HashCodeEqualsDefinedTesting<P>,
        ParserTesting<P, FakeParserContext>,
        TypeNameTesting<P> {

    DateTimeFormatterParserTestCase() {
        super();
    }

    @Test
    public final void testWithNullFormatterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createParser(null, this.pattern());
        });
    }

    @Test
    public final void testWithNullPatternFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createParser(this.formatter(), null);
        });
    }

    @Test
    public final void testWithEmptyPatternFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createParser(this.formatter(), "");
        });
    }

    @Test
    public final void testDifferentPattern() {
        this.checkNotEquals(this.createParser(this.differentPattern()));
    }

    public final P createParser() {
        return this.createParser(this.pattern());
    }

    final P createParser(final String pattern) {
        return this.createParser(DateTimeFormatter.ofPattern(pattern), pattern);
    }

    abstract P createParser(final DateTimeFormatter formatter, final String pattern);

    abstract T createParserToken(final DateTimeFormatter formatter, final String text);


    final DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern(this.pattern());
    }

    abstract String pattern();

    abstract String differentPattern();

    @Override
    public FakeParserContext createContext() {
        return new FakeParserContext();
    }

    final void parseAndCheck2(final String text) {
        this.parseAndCheck2(text, "");
    }

    final void parseAndCheck2(final String text, final String after) {
        this.parseAndCheck2(this.pattern(), text, after);
    }

    final void parseAndCheck2(final String pattern, final String text, final String after) {
        this.parseAndCheck2(DateTimeFormatter.ofPattern(pattern), pattern, text, after);
    }

    final void parseAndCheck2(final DateTimeFormatter formatter, final String pattern, final String text, final String after) {
        this.parseAndCheck(this.createParser(formatter, pattern),
                text + after,
                this.createParserToken(formatter, text),
                text,
                after);
    }

    final TextCursor parseFailAndCheck2(final String pattern, final String cursorText) {
        return this.parseFailAndCheck(this.createParser(pattern), cursorText);
    }

    final void parseThrows2(final String pattern, final String text) {
        this.parseThrows(this.createParser(pattern), this.createContext(), TextCursors.charSequence(text), "");
    }

    @Override
    public String parserTokenTypeNamePrefix() {
        return "";
    }

    // ClassTesting......................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HashCodeEqualsDefinedTesting ....................................................................................

    @Override
    public P createObject() {
        return this.createParser();
    }

    // TypeNameTesting ..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return DateTimeFormatterParser.class.getSimpleName();
    }
}

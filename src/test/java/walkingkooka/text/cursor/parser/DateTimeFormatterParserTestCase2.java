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
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

import java.math.MathContext;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class DateTimeFormatterParserTestCase2<P extends DateTimeFormatterParser<ParserContext>, T extends ParserToken>
        extends DateTimeFormatterParserTestCase<P>
        implements ParserTesting2<P, ParserContext> {

    DateTimeFormatterParserTestCase2() {
        super();
    }

    @Test
    public final void testWithNullFormatterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createParser((Function<DateTimeContext, DateTimeFormatter>) null);
        });
    }

    @Override
    public final P createParser() {
        return this.createParser(this.pattern());
    }

    final P createParser(final String pattern) {
        // JAPAN will be overridden by DateTimeFormatterParser
        return this.createParser(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.JAPAN));
    }

    private P createParser(final DateTimeFormatter formatter) {
        return this.createParser((c) -> formatter.withLocale(c.locale()));
    }

    abstract P createParser(final Function<DateTimeContext, DateTimeFormatter> formatter);

    abstract T createParserToken(final DateTimeFormatter formatter, final String text);


    final DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern(this.pattern());
    }

    abstract String pattern();

    @Override
    public ParserContext createContext() {
        return ParserContexts.basic(DateTimeContexts.locale(LOCALE, 50),
                DecimalNumberContexts.decimalFormatSymbols(new DecimalFormatSymbols(LOCALE), '^', '+', LOCALE, MathContext.UNLIMITED));
    }

    private final static Locale LOCALE = Locale.ENGLISH;

    final void parseAndCheck2(final String text) {
        this.parseAndCheck2(text, "");
    }

    final void parseAndCheck2(final String pattern,
                              final String text) {
        this.parseAndCheck2(pattern, text, "");
    }

    final void parseAndCheck2(final String pattern,
                              final String text,
                              final String after) {
        this.parseAndCheck2((c) -> DateTimeFormatter.ofPattern(pattern),
                pattern,
                text,
                after);
    }

    final void parseAndCheck2(final Function<DateTimeContext, DateTimeFormatter> formatter,
                              final String pattern,
                              final String text,
                              final String after) {
        final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(pattern).withLocale(LOCALE);
        formatter2.parse(text);

        this.parseAndCheck(this.createParser(formatter2),
                text + after,
                this.createParserToken(formatter2, text),
                text,
                after);
    }

    final TextCursor parseFailAndCheck2(final String pattern, final String cursorText) {
        return this.parseFailAndCheck(this.createParser(pattern), cursorText);
    }

    final void parseThrows2(final String pattern, final String text) {
        this.parseThrows(this.createParser(pattern), this.createContext(), TextCursors.charSequence(text), "");
    }

}

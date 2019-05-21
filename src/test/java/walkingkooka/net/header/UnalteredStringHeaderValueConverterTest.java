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
 *
 */

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

public final class UnalteredStringHeaderValueConverterTest extends StringHeaderValueConverterTestCase<UnalteredStringHeaderValueConverter> {

    @Override
    public void testInvalidHeaderValueFails() {
    }

    @Override
    public void testTypeNaming() {
    }

    @Test
    public void testParseAsciiControlCharacters() {
        IntStream.range(0, 31)
                .filter(i -> i != '\t')
                .forEach(i -> this.parseAndCheck2("" + Character.toChars(i)));
    }

    @Test
    public void testParseNonControlCharacters() {
        IntStream.range(32, 256)
                .forEach(i -> this.parseAndCheck2( "" + Character.toChars(i)));
    }

    @Test
    public void testParseIncludingComments() {
        this.parseAndCheck2("Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0");
    }

    @Test
    public void testParseSurroundedWhitespaceKept() {
        this.parseAndCheck2(" a b c ");
    }

    @Test
    public void testParseDoubleQuoted() {
        this.parseAndCheck2("\"abc\"");
    }

    private void parseAndCheck2(final String text) {
        this.parseAndCheck(text, text);
    }

    @Test
    public void testToText() {
        final String text = "\"abc\" def (comment-1)";
        this.toTextAndCheck(text, text);
    }

    @Test
    public void testRoundtrip() {
        final String text = "\"abc\" def (comment-1)";
        this.parseAndToTextAndCheck(text, text);
    }

    @Override
    public String typeNamePrefix() {
        return "UnalteredString";
    }

    @Override
    String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected UnalteredStringHeaderValueConverter converter() {
        return UnalteredStringHeaderValueConverter.INSTANCE;
    }

    @Override
    final String converterToString() {
        return "String";
    }

    @Override
    public Class<UnalteredStringHeaderValueConverter> type() {
        return UnalteredStringHeaderValueConverter.class;
    }
}

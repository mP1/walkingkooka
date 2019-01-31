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
import walkingkooka.predicate.character.CharPredicates;

import java.time.OffsetDateTime;

public final class QuotedUnquotedStringHeaderValueConverterTest extends
        HeaderValueConverterTestCase<QuotedUnquotedStringHeaderValueConverter, String> {

    @Override
    protected String requiredPrefix() {
        return "QuotedUnquotedString";
    }

    @Test
    public void testUnquoted() {
        this.parseAndToTextAndCheck("123", "123");
    }

    @Test
    public void testQuoted() {
        this.parseAndToTextAndCheck("\"abc\"", "abc");
    }

    @Test
    public void testQuotedWithEscaping() {
        this.parseAndToTextAndCheck("\"abc\\\"def\"", "abc\"def");
    }

    @Override
    QuotedUnquotedStringHeaderValueConverter converter() {
        return QuotedUnquotedStringHeaderValueConverter.with(CharPredicates.asciiPrintable(),
                true,
                CharPredicates.digit());
    }

    @Override
    TokenHeaderValueParameterName name() {
        return TokenHeaderValueParameterName.Q;
    }

    @Override
    String invalidHeaderValue() {
        return "\0";
    }

    @Override
    String value() {
        return "123";
    }

    @Override
    String valueType() {
        return this.valueType(String.class);
    }

    @Override
    String converterToString() {
        return "String";
    }

    @Override
    protected Class<QuotedUnquotedStringHeaderValueConverter> type() {
        return QuotedUnquotedStringHeaderValueConverter.class;
    }
}

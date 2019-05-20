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
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpHeaderNameListHeaderValueConverterTest extends
        NonStringHeaderValueConverterTestCase<HttpHeaderNameListHeaderValueConverter, List<HttpHeaderName<?>>> {

    @Override
    public String typeNamePrefix() {
        return HttpHeaderName.class.getSimpleName();
    }

    @Test
    public void testParse() {
        this.parseAndCheck2("Accept", HttpHeaderName.ACCEPT);
    }

    @Test
    public void testParse2() {
        this.parseAndCheck2("Accept,Content-Length", HttpHeaderName.ACCEPT, HttpHeaderName.CONTENT_LENGTH);
    }

    @Test
    public void testParseTokenWhitespaceToken() {
        this.parseAndCheck2("Accept, Content-Length", HttpHeaderName.ACCEPT, HttpHeaderName.CONTENT_LENGTH);
    }

    private void parseAndCheck2(final String value, final HttpHeaderName<?>... headers) {
        this.parseAndCheck(value, Lists.of(headers));
    }

    @Test
    public void testCheckIncludesNullFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.header(), null));
        });
    }

    @Test
    public void testCheckIncludesWrongTypeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.header(), "WRONG!"));
        });
    }

    private HttpHeaderName header() {
        return HttpHeaderName.ACCEPT;
    }

    @Test
    public void testToText() {
        this.toTextAndCheck2("Accept",
                HttpHeaderName.ACCEPT);
    }

    @Test
    public void testToText2() {
        this.toTextAndCheck2("Accept, Content-Length",
                HttpHeaderName.ACCEPT,
                HttpHeaderName.CONTENT_LENGTH);
    }

    private void toTextAndCheck2(final String value,
                                 final HttpHeaderName<?>... headers) {
        this.toTextAndCheck(Lists.of(headers), value);
    }

    @Override
    HttpHeaderNameListHeaderValueConverter converter() {
        return HttpHeaderNameListHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<List<HttpHeaderName<?>>> name() {
        return HttpHeaderName.TRAILER;
    }

    @Override
    String invalidHeaderValue() {
        return "   ";
    }

    @Override
    List<HttpHeaderName<?>> value() {
        return Lists.of(HttpHeaderName.ACCEPT, HttpHeaderName.ACCEPT_LANGUAGE);
    }

    @Override
    String valueType() {
        return this.listValueType(HttpHeaderName.class);
    }

    @Override
    String converterToString() {
        return "List<HttpHeaderName>";
    }

    @Override
    public Class<HttpHeaderNameListHeaderValueConverter> type() {
        return HttpHeaderNameListHeaderValueConverter.class;
    }
}

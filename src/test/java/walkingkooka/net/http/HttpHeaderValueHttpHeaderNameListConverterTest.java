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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class HttpHeaderValueHttpHeaderNameListConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueHttpHeaderNameListConverter, List<HttpHeaderName<?>>> {

    @Test
    public void testHeader() {
        this.parseAndCheck2("Accept", HttpHeaderName.ACCEPT);
    }

    @Test
    public void testHeaderHeader() {
        this.parseAndCheck2("Accept,Content-Length", HttpHeaderName.ACCEPT, HttpHeaderName.CONTENT_LENGTH);
    }

    @Test
    public void testHeaderWhitespaceHeader() {
        this.parseAndCheck2("Accept, Content-Length", HttpHeaderName.ACCEPT, HttpHeaderName.CONTENT_LENGTH);
    }

    private void parseAndCheck2(final String value, final HttpHeaderName<?>... headers) {
        this.parseAndCheck(value, Lists.of(headers));
    }

    @Override
    HttpHeaderName<List<HttpHeaderName<?>>> headerOrParameterName() {
        return HttpHeaderName.TRAILER;
    }

    @Override
    HttpHeaderValueHttpHeaderNameListConverter converter() {
        return HttpHeaderValueHttpHeaderNameListConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "   ";
    }

    @Override
    String converterToString() {
        return "List<HttpHeaderName>";
    }

    @Override
    protected Class<HttpHeaderValueHttpHeaderNameListConverter> type() {
        return HttpHeaderValueHttpHeaderNameListConverter.class;
    }
}

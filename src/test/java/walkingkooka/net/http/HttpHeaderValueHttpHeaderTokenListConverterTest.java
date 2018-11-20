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
import walkingkooka.collect.map.Maps;

import java.util.List;

public final class HttpHeaderValueHttpHeaderTokenListConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueHttpHeaderTokenListConverter, List<HttpHeaderToken>> {

    @Test
    public void testToken() {
        this.parseAndCheck2("EN", this.en());
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("EN,EN_AU", this.en(), this.en_AU());
    }

    @Test
    public void testTokenCommaWhitespaceToken() {
        this.parseAndCheck2("EN, EN_AU", this.en(), this.en_AU());
    }

    @Test
    public void testTokenWithParameters() {
        this.parseAndCheck2("ES; q=0.5", this.es());
    }

    @Test
    public void testManyTokensSomeWithParameters() {
        this.parseAndCheck2("ES; q=0.5, EN, FR; q=0.25", this.es(), this.en(), fr());
    }

    private void parseAndCheck2(final String headerValue, final HttpHeaderToken... tokens) {
        this.parseAndCheck(headerValue, Lists.of(tokens));
    }

    private HttpHeaderToken en() {
        return HttpHeaderToken.with("EN", HttpHeaderToken.NO_PARAMETERS);
    }

    private HttpHeaderToken en_AU() {
        return HttpHeaderToken.with("EN_AU", HttpHeaderToken.NO_PARAMETERS);
    }

    private HttpHeaderToken es() {
        return HttpHeaderToken.with("ES", Maps.one(HttpHeaderParameterName.Q, "0.5"));
    }

    private HttpHeaderToken fr() {
        return HttpHeaderToken.with("FR", Maps.one(HttpHeaderParameterName.Q, "0.25"));
    }

    @Override
    HttpHeaderName<List<HttpHeaderToken>> headerOrParameterName() {
        return HttpHeaderName.CONTENT_LANGUAGE;
    }

    @Override
    HttpHeaderValueHttpHeaderTokenListConverter converter() {
        return HttpHeaderValueHttpHeaderTokenListConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "/////";
    }

    @Override
    String converterToString() {
        return "List<HttpHeaderToken>";
    }

    @Override
    protected Class<HttpHeaderValueHttpHeaderTokenListConverter> type() {
        return HttpHeaderValueHttpHeaderTokenListConverter.class;
    }
}

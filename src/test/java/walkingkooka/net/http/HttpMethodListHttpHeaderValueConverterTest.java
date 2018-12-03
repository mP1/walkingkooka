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
import walkingkooka.net.header.HeaderValueException;

import java.util.List;

public final class HttpMethodListHttpHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<HttpMethodListHttpHeaderValueConverter, List<HttpMethod>> {

    @Override
    protected String requiredPrefix() {
        return HttpMethod.class.getSimpleName();
    }

    @Test
    public void testParseGet() {
        this.parseAndCheck2("GET", HttpMethod.GET);
    }

    @Test
    public void testParseGetPost() {
        this.parseAndCheck2("GET,POST", HttpMethod.GET, HttpMethod.POST);
    }

    @Test
    public void testParseGetWhitespacePost() {
        this.parseAndCheck2("GET,  POST", HttpMethod.GET, HttpMethod.POST);
    }

    private void parseAndCheck2(final String headerValue, final HttpMethod... methods) {
        this.parseAndCheck(headerValue, Lists.of(methods));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesNullFails() {
        this.check(Lists.of(this.method(), null));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesWrongTypeFails() {
        this.check(Lists.of(this.method(), "WRONG!"));
    }

    private HttpMethod method() {
        return HttpMethod.GET;
    }

    @Test
    public void testGetResponse() {
        this.formatAndCheck2("GET", HttpMethod.GET);
    }

    @Test
    public void testGetPostResponse() {
        this.formatAndCheck2("GET, POST", HttpMethod.GET, HttpMethod.POST);
    }

    private void formatAndCheck2(final String headerValue, final HttpMethod... methods) {
        this.toTextAndCheck(Lists.of(methods), headerValue);
    }

    @Override
    protected HttpMethodListHttpHeaderValueConverter converter() {
        return HttpMethodListHttpHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<List<HttpMethod>> name() {
        return HttpHeaderName.ALLOW;
    }

    @Override
    protected String invalidHeaderValue() {
        return "/relative/url/must/fail";
    }

    @Override
    protected List<HttpMethod> value() {
        return Lists.of(HttpMethod.GET, HttpMethod.POST);
    }

    @Override
    protected String converterToString() {
        return "List<HttpMethod>";
    }

    @Override
    protected Class<HttpMethodListHttpHeaderValueConverter> type() {
        return HttpMethodListHttpHeaderValueConverter.class;
    }
}

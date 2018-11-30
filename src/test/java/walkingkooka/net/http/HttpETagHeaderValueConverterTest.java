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

public final class HttpETagHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<HttpETagHeaderValueConverter, HttpETag> {

    @Override
    protected String requiredPrefix() {
        return HttpETag.class.getSimpleName();
    }

    @Test
    public void testRequest() {
        this.parseAndCheck("W/\"123\"", HttpETag.with("123", HttpETagValidator.WEAK));
    }

    @Test
    public void testResponse() {
        this.formatAndCheck(HttpETag.with("123", HttpETagValidator.WEAK), "W/\"123\"");
    }

    @Override
    protected HttpETagHeaderValueConverter converter() {
        return HttpETagHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<HttpETag> name() {
        return HttpHeaderName.E_TAG;
    }

    @Override
    protected String invalidHeaderValue() {
        return "I/";
    }

    @Override
    protected HttpETag value() {
        return HttpETag.with("01234567890", HttpETagValidator.WEAK);
    }

    @Override
    protected String converterToString() {
        return HttpETag.class.getSimpleName();
    }

    @Override
    protected Class<HttpETagHeaderValueConverter> type() {
        return HttpETagHeaderValueConverter.class;
    }
}

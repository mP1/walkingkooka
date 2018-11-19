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
import walkingkooka.net.AbsoluteUrl;

public final class HttpHeaderValueAbsoluteUrlConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueAbsoluteUrlConverter, AbsoluteUrl> {

    @Test
    public void testReferer() {
        final String url = "http://example.com";
        this.parseAndCheck(url, AbsoluteUrl.parse(url));
    }

    @Override
    HttpHeaderName<AbsoluteUrl> headerOrParameterName() {
        return HttpHeaderName.REFERER;
    }

    @Override
    HttpHeaderValueAbsoluteUrlConverter converter() {
        return HttpHeaderValueAbsoluteUrlConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "/relative/url/must/fail";
    }

    @Override
    String converterToString() {
        return AbsoluteUrl.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueAbsoluteUrlConverter> type() {
        return HttpHeaderValueAbsoluteUrlConverter.class;
    }
}

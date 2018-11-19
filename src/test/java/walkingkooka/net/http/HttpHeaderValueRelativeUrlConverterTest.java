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
import walkingkooka.net.RelativeUrl;

public final class HttpHeaderValueRelativeUrlConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueRelativeUrlConverter, RelativeUrl> {

    @Test
    public void testContentLocation() {
        final String url = "/relative/url/file.html";
        this.parseAndCheck(url, RelativeUrl.parse(url));
    }

    @Override
    HttpHeaderName<RelativeUrl> headerOrParameterName() {
        return HttpHeaderName.CONTENT_LOCATION;
    }

    @Override
    HttpHeaderValueRelativeUrlConverter converter() {
        return HttpHeaderValueRelativeUrlConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    String converterToString() {
        return RelativeUrl.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueRelativeUrlConverter> type() {
        return HttpHeaderValueRelativeUrlConverter.class;
    }
}

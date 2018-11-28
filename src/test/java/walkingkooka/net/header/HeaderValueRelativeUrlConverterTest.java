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

import org.junit.Test;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.http.HttpHeaderName;

public final class HeaderValueRelativeUrlConverterTest extends
        HeaderValueConverterTestCase<HeaderValueRelativeUrlConverter, RelativeUrl> {

    @Test
    public void testContentLocation() {
        final String url = "/relative/url/file.html";
        this.parseAndFormatAndCheck(url, RelativeUrl.parse(url));
    }

    @Override
    protected HeaderValueRelativeUrlConverter converter() {
        return HeaderValueRelativeUrlConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<RelativeUrl> name() {
        return HttpHeaderName.CONTENT_LOCATION;
    }

    @Override
    protected String invalidHeaderValue() {
        return "http://example.com";
    }

    @Override
    protected String converterToString() {
        return RelativeUrl.class.getSimpleName();
    }

    @Override
    protected Class<HeaderValueRelativeUrlConverter> type() {
        return HeaderValueRelativeUrlConverter.class;
    }
}

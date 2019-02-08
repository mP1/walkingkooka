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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;

public final class UrlHeaderValueConverterTest extends
        HeaderValueConverterTestCase<UrlHeaderValueConverter, Url> {

    @Override public String typeNamePrefix() {
        return Url.class.getSimpleName();
    }

    @Override
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testLocationAbsoluteUrl() {
        final String url = "http://example.com";
        this.parseAndToTextAndCheck(url, AbsoluteUrl.parse(url));
    }

    @Test
    public void testLocationRelativeUrl() {
        final String url = "/relative/url/file.html";
        this.parseAndToTextAndCheck(url, RelativeUrl.parse(url));
    }

    @Override
    protected UrlHeaderValueConverter converter() {
        return UrlHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<Url> name() {
        return HttpHeaderName.LOCATION;
    }

    @Override
    String invalidHeaderValue() {
        return "\\";
    }

    @Override
    Url value() {
        return Url.parse("/path?p1=v1");
    }

    @Override
    String valueType() {
        return this.valueType(Url.class);
    }

    @Override
    String converterToString() {
        return Url.class.getSimpleName();
    }

    @Override
    public Class<UrlHeaderValueConverter> type() {
        return UrlHeaderValueConverter.class;
    }
}

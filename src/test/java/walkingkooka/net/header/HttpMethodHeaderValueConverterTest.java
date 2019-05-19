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
import walkingkooka.net.http.HttpMethod;

public final class HttpMethodHeaderValueConverterTest extends
        HeaderValueConverterTestCase<HttpMethodHeaderValueConverter, HttpMethod> {

    @Override
    public String typeNamePrefix() {
        return HttpMethod.class.getSimpleName();
    }

    @Test
    public void testRoundtripGet() {
        this.parseAndToTextAndCheck("GET", HttpMethod.GET);
    }

    @Test
    public void testRoundtripPost() {
        this.parseAndToTextAndCheck("POST", HttpMethod.POST);
    }

    @Test
    public void testRoundtripCustom() {
        this.parseAndToTextAndCheck("XCUSTOM", HttpMethod.with("XCUSTOM"));
    }

    @Override
    HttpMethodHeaderValueConverter converter() {
        return HttpMethodHeaderValueConverter.INSTANCE;
    }

    @Override
    LinkParameterName<HttpMethod> name() {
        return LinkParameterName.METHOD;
    }

    @Override
    String invalidHeaderValue() {
        return "/relative/url/must/fail";
    }

    @Override
    HttpMethod value() {
        return HttpMethod.GET;
    }

    @Override
    String valueType() {
        return this.valueType(HttpMethod.class);
    }

    @Override
    String converterToString() {
        return HttpMethod.class.getSimpleName();
    }

    @Override
    public Class<HttpMethodHeaderValueConverter> type() {
        return HttpMethodHeaderValueConverter.class;
    }
}

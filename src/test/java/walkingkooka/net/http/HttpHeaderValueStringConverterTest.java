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

import org.junit.Ignore;
import org.junit.Test;

public final class HttpHeaderValueStringConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueStringConverter, String> {

    private final static String TEXT = "abc123";

    @Test
    @Ignore
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testRequestValue() {
        this.parseAndCheck(TEXT, TEXT);
    }

    @Test
    public void testResponseValue() {
        this.formatAndCheck(TEXT, TEXT);
    }

    @Override
    HttpHeaderName<String> headerOrParameterName() {
        return HttpHeaderName.CACHE_CONTROL;
    }

    @Override
    HttpHeaderValueStringConverter converter() {
        return HttpHeaderValueStringConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    String converterToString() {
        return String.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueStringConverter> type() {
        return HttpHeaderValueStringConverter.class;
    }
}

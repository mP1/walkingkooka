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

public final class HttpHeaderValueLongConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueLongConverter, Long> {

    @Test
    public void testLong() {
        this.parseAndCheck("123", 123L);
    }

    @Override
    HttpHeaderName<Long> headerOrParameterName() {
        return HttpHeaderName.CONTENT_LENGTH;
    }

    @Override
    HttpHeaderValueLongConverter converter() {
        return HttpHeaderValueLongConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "abc";
    }

    @Override
    String converterToString() {
        return Long.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueLongConverter> type() {
        return HttpHeaderValueLongConverter.class;
    }
}

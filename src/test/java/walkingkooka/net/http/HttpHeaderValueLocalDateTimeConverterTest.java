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

import java.time.LocalDateTime;

public final class HttpHeaderValueLocalDateTimeConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueLocalDateTimeConverter, LocalDateTime> {

    private final static String TEXT = "Wed, 21 Oct 2015 07:28:00 GMT";
    private final static LocalDateTime VALUE = LocalDateTime.of(2015, 10, 21, 7, 28, 0);

    @Test
    public void testDateRequest() {
        this.parseAndCheck(TEXT, VALUE);
    }

    @Test
    public void testLastModifiedResponse() {
        this.formatAndCheck(VALUE, TEXT);
    }


    @Override
    HttpHeaderName headerOrParameterName() {
        return HttpHeaderName.DATE;
    }

    @Override
    HttpHeaderValueLocalDateTimeConverter converter() {
        return HttpHeaderValueLocalDateTimeConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "abc";
    }

    @Override
    String converterToString() {
        return LocalDateTime.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueLocalDateTimeConverter> type() {
        return HttpHeaderValueLocalDateTimeConverter.class;
    }
}

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
import walkingkooka.net.http.HttpHeaderName;

import java.time.LocalDateTime;

public final class LocalDateTimeHeaderValueConverterTest extends
        HeaderValueConverterTestCase<LocalDateTimeHeaderValueConverter, LocalDateTime> {

    @Override
    protected String requiredPrefix() {
        return LocalDateTime.class.getSimpleName();
    }

    private final static String TEXT = "Wed, 21 Oct 2015 07:28:00 GMT";
    private final static LocalDateTime VALUE = LocalDateTime.of(2015, 10, 21, 7, 28, 0);

    @Test
    public void testDate() {
        this.parseAndCheck(TEXT, VALUE);
    }

    @Test
    public void testLastModified() {
        this.toTextAndCheck(VALUE, TEXT);
    }

    @Override
    protected LocalDateTimeHeaderValueConverter converter() {
        return LocalDateTimeHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName name() {
        return HttpHeaderName.DATE;
    }

    @Override
    protected String invalidHeaderValue() {
        return "abc";
    }

    @Override
    protected LocalDateTime value() {
        return VALUE;
    }

    @Override
    protected String converterToString() {
        return LocalDateTime.class.getSimpleName();
    }

    @Override
    protected Class<LocalDateTimeHeaderValueConverter> type() {
        return LocalDateTimeHeaderValueConverter.class;
    }
}

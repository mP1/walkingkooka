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

import java.time.LocalDateTime;

public final class LocalDateTimeHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<LocalDateTimeHeaderValueHandler, LocalDateTime> {

    @Override
    public String typeNamePrefix() {
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
    LocalDateTimeHeaderValueHandler handler() {
        return LocalDateTimeHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName name() {
        return HttpHeaderName.DATE;
    }

    @Override
    String invalidHeaderValue() {
        return "abc";
    }

    @Override
    LocalDateTime value() {
        return VALUE;
    }

    @Override
    String valueType() {
        return this.valueType(LocalDateTime.class);
    }

    @Override
    String handlerToString() {
        return LocalDateTime.class.getSimpleName();
    }

    @Override
    public Class<LocalDateTimeHeaderValueHandler> type() {
        return LocalDateTimeHeaderValueHandler.class;
    }
}

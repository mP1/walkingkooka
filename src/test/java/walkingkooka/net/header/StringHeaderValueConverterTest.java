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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.net.http.HttpHeaderName;

public final class StringHeaderValueConverterTest extends
        HeaderValueConverterTestCase<StringHeaderValueConverter, String> {

    private final static String TEXT = "abc123";

    @Override
    protected String requiredPrefix() {
        return String.class.getSimpleName();
    }

    @Test
    @Ignore
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testValue() {
        this.parseAndToTextAndCheck(TEXT, TEXT);
    }

    @Override
    protected StringHeaderValueConverter converter() {
        return StringHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<String> name() {
        return HttpHeaderName.CACHE_CONTROL;
    }

    @Override
    protected String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String value() {
        return TEXT;
    }

    @Override
    protected String converterToString() {
        return String.class.getSimpleName();
    }

    @Override
    protected Class<StringHeaderValueConverter> type() {
        return StringHeaderValueConverter.class;
    }
}

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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.HttpHeaderName;

import static org.junit.Assert.assertEquals;

public final class CharsetNameHeaderValueConverterTest extends
        HeaderValueConverterTestCase<CharsetNameHeaderValueConverter, CharsetName> {
    @Override
    protected String requiredPrefix() {
        return CharsetName.class.getSimpleName();
    }

    @Test
    public void testContentType() {
        final String charset = "utf-8";
        this.parseAndToTextAndCheck(charset,
                CharsetName.with(charset));
    }

    @Test
    public void testUnknownCharset() {
        final String charset = "utf-1";
        final CharsetName charsetName = CharsetName.with(charset);
        assertEquals("charsetName must have no charset",
                CharsetName.NO_CHARSET,
                charsetName.charset());
        this.parseAndToTextAndCheck(charset,
                charsetName);
    }

    @Override
    protected CharsetNameHeaderValueConverter converter() {
        return CharsetNameHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<EmailAddress> name() {
        return HttpHeaderName.FROM;
    }

    @Override
    protected String invalidHeaderValue() {
        return "\0";
    }

    @Override
    protected CharsetName value() {
        return CharsetName.with("utf-8");
    }

    @Override
    protected String converterToString() {
        return CharsetName.class.getSimpleName();
    }

    @Override
    protected Class<CharsetNameHeaderValueConverter> type() {
        return CharsetNameHeaderValueConverter.class;
    }
}

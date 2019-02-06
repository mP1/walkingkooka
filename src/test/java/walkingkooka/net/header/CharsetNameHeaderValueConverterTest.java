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
import walkingkooka.net.email.EmailAddress;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CharsetNameHeaderValueConverterTest extends
        HeaderValueConverterTestCase<CharsetNameHeaderValueConverter, CharsetName> {
    @Override
    protected String requiredPrefix() {
        return CharsetName.class.getSimpleName();
    }

    @Test
    public void testContent() {
        final String charset = Charset.forName("utf-8").name();
        this.parseAndToTextAndCheck(charset,
                CharsetName.with(charset));
    }

    @Test
    public void testUnknownCharset() {
        final String charset = "utf-1";
        final CharsetName charsetName = CharsetName.with(charset);
        assertEquals(CharsetName.NO_CHARSET,
                charsetName.charset(),
                "charsetName must have no charset");
        this.parseAndToTextAndCheck(charset,
                charsetName);
    }

    @Override
    CharsetNameHeaderValueConverter converter() {
        return CharsetNameHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<EmailAddress> name() {
        return HttpHeaderName.FROM;
    }

    @Override
    String invalidHeaderValue() {
        return "\0";
    }

    @Override
    CharsetName value() {
        return CharsetName.with("utf-8");
    }

    @Override
    String valueType() {
        return this.valueType(CharsetName.class);
    }

    @Override
    String converterToString() {
        return CharsetName.class.getSimpleName();
    }

    @Override
    public Class<CharsetNameHeaderValueConverter> type() {
        return CharsetNameHeaderValueConverter.class;
    }
}

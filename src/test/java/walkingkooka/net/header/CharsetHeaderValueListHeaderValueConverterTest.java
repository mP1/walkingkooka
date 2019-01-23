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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.email.EmailAddress;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class CharsetHeaderValueListHeaderValueConverterTest extends
        HeaderValueConverterTestCase<CharsetHeaderValueListHeaderValueConverter,
                List<CharsetHeaderValue>> {
    @Override
    protected String requiredPrefix() {
        return CharsetHeaderValue.class.getSimpleName() + List.class.getSimpleName();
    }

    @Test
    public void testContentType() {
        final String charset = Charset.forName("utf8").name();
        this.parseAndToTextAndCheck(charset,
                Lists.of(CharsetHeaderValue.with(CharsetName.with(charset))));
    }

    @Test
    public void testUnknownCharset() {
        final String charset = "utf-1";
        final CharsetName charsetName = CharsetName.with(charset);
        assertEquals("charsetName must have no charset",
                CharsetName.NO_CHARSET,
                charsetName.charset());
        this.parseAndToTextAndCheck(charset,
                Lists.of(CharsetHeaderValue.with(CharsetName.with(charset))));
    }

    @Override
    CharsetHeaderValueListHeaderValueConverter converter() {
        return CharsetHeaderValueListHeaderValueConverter.INSTANCE;
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
    List<CharsetHeaderValue> value() {
        return Lists.of(CharsetHeaderValue.with(CharsetName.UTF_8));
    }

    @Override
    String valueType() {
        return this.listValueType(CharsetHeaderValue.class);
    }

    @Override
    String converterToString() {
        return "List<CharsetHeaderValue>";
    }

    @Override
    protected Class<CharsetHeaderValueListHeaderValueConverter> type() {
        return CharsetHeaderValueListHeaderValueConverter.class;
    }
}

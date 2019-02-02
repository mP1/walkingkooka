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
import walkingkooka.naming.Name;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CacheControlDirectiveExtensionHeaderValueConverterTest extends
        HeaderValueConverterTestCase<CacheControlDirectiveExtensionHeaderValueConverter, Object> {

    @Test
    public void testCheckLong() {
        CacheControlDirectiveExtensionHeaderValueConverter.INSTANCE.check(123L, this.name());
    }

    @Test
    public void testCheckString() {
        CacheControlDirectiveExtensionHeaderValueConverter.INSTANCE.check("abc123", this.name());
    }

    @Test
    public void testParseNumber() {
        this.parseAndCheck("123", 123L);
    }

    @Test
    public void testParseText() {
        this.parseAndCheck("abc", "abc");
    }

    @Test
    public void testToTextNumber() {
        this.toTextAndCheck(123L, "123");
    }

    @Test
    public void testToTextText() {
        this.toTextAndCheck("abc123", "abc123");
    }

    @Test
    public void testToTextInvalidValueFail() {
        assertThrows(HeaderValueException.class, () -> {
            CacheControlDirectiveExtensionHeaderValueConverter.INSTANCE
                    .toText(this,
                            CacheControlDirectiveName.MAX_STALE);
        });
    }

    @Override
    protected String requiredPrefix() {
        return "CacheControlDirectiveExtension";
    }

    @Override
    String invalidHeaderValue() {
        return ",";
    }

    @Override
    String converterToString() {
        return "CacheControlDirectiveExtension";
    }

    @Override
    CacheControlDirectiveExtensionHeaderValueConverter converter() {
        return CacheControlDirectiveExtensionHeaderValueConverter.INSTANCE;
    }

    @Override
    Name name() {
        return CacheControlDirectiveName.with("extension");
    }

    @Override
    Object value() {
        return 123L;
    }

    @Override
    String valueType() {
        return this.valueType(String.class);
    }

    @Override
    protected Class<CacheControlDirectiveExtensionHeaderValueConverter> type() {
        return CacheControlDirectiveExtensionHeaderValueConverter.class;
    }
}

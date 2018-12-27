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
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class EncodedTextTest extends HeaderValueTestCase<EncodedText> {

    @Test
    public void testWith() {
        final EncodedText encodedText = this.createHeaderValue();
        assertEquals("charset", this.charset(), encodedText.charset());
        assertEquals("language", this.language(), encodedText.language());
        assertEquals("value", this.value(), encodedText.value());
    }

    @Test
    public void testHeaderText() {
        this.toHeaderTextAndCheck("UTF-8'en'abc123");
    }

    @Test
    public void testHeaderTextWithoutLanguage() {
        this.toHeaderTextAndCheck(EncodedText.with(this.charset(),
                EncodedText.NO_LANGUAGE,
                "abc123"),
                "UTF-8''abc123");
    }

    @Test
    public void testHeaderTextEncodesPercent() {
        this.toHeaderTextAndCheck2("abc%", "UTF-8'en'abc%25");
    }

    @Test
    public void testHeaderTextEncodesWhitespace() {
        this.toHeaderTextAndCheck2("abc\t\r\n ", "UTF-8'en'abc%09%0d%0a%20");
    }

    private void toHeaderTextAndCheck2(final String value, final String headerText) {
        this.toHeaderTextAndCheck(this.createHeaderValue(value), headerText);
    }

    @Override
    protected EncodedText createHeaderValue() {
        return this.createHeaderValue(this.value());
    }

    private EncodedText createHeaderValue(final String value) {
        return EncodedText.with(this.charset(), this.language(), value);
    }

    private CharsetName charset() {
        return CharsetName.UTF_8;
    }

    private Optional<LanguageTagName> language() {
        return Optional.of(LanguageTagName.with("en"));
    }

    private String value() {
        return "abc123";
    }

    @Override
    protected boolean isMultipart() {
        return true;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<EncodedText> type() {
        return EncodedText.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}

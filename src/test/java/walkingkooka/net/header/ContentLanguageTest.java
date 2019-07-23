/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentLanguageTest extends HeaderValueTestCase<ContentLanguage>
        implements ParseStringTesting<ContentLanguage> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentLanguage.with(null);
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue(), this.value());
    }

    @Test
    public void testWith2() {
        final List<LanguageName> languages = Lists.of(LanguageName.with("unknown"));
        this.checkValue(ContentLanguage.with(languages), languages);
    }

    private void checkValue(final ContentLanguage contentLanguage,
                            final List<LanguageName> languages) {
        assertEquals(languages, contentLanguage.value(), "value");
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("en, fr",
                this.createHeaderValue(this.en(), this.fr()));
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("en,  fr,  gr",
                this.createHeaderValue(this.en(), this.fr(), this.gr()));
    }

    @Test
    public void testHeaderText() {
        final String text = "en";
        this.toHeaderTextAndCheck(ContentLanguage.parse(text), text);
    }

    @Test
    public void testHeaderText2() {
        final String text = "en-GB"; // Locale.EN_GB is "en-GB".
        this.toHeaderTextAndCheck(ContentLanguage.with(Lists.of(LanguageName.with(text))), text);
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ContentLanguage.with(Lists.of(LanguageName.with("hu"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), "en, fr");
    }

    @Override
    public ContentLanguage createHeaderValue() {
        return ContentLanguage.with(this.value());
    }

    private ContentLanguage createHeaderValue(final LanguageName...languages) {
        return ContentLanguage.with(Lists.of(languages));
    }

    private List<LanguageName> value() {
        return Lists.of(this.en(), this.fr());
    }
    
    private LanguageName en() {
        return LanguageName.with("en");
    }

    private LanguageName fr() {
        return LanguageName.with("fr");
    }

    private LanguageName gr() {
        return LanguageName.with("gr");
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ContentLanguage> type() {
        return ContentLanguage.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public ContentLanguage parse(final String text) {
        return ContentLanguage.parse(text);
    }
}

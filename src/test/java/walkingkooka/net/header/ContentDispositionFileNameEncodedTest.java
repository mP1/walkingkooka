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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ContentDispositionFileNameEncodedTest extends ContentDispositionFileNameTestCase<ContentDispositionFileNameEncoded> {

    private final static String FILENAME = "filename 123";

    @Test
    public void testWith() {
        this.check(this.createHeaderValue(),
                FILENAME,
                Optional.of(this.encodedText().charset()),
                this.encodedText().language());
    }

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck("UTF-8'en'filename%20123");
    }

    @Test
    public void testEqualsDifferentCharsetName() {
        this.checkNotEquals(ContentDispositionFileNameEncoded.with(
                EncodedText.with(CharsetName.UTF_16,
                        this.language(),
                        FILENAME)));
    }

    @Test
    public void testEqualsDifferentLanguage() {
        this.checkNotEquals(ContentDispositionFileNameEncoded.with(
                EncodedText.with(this.charsetName(),
                        this.language("fr"),
                        FILENAME)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createHeaderValue(), FILENAME);
    }

    @Override
    protected ContentDispositionFileNameEncoded createHeaderValue() {
        return ContentDispositionFileNameEncoded.with(this.encodedText());
    }

    private EncodedText encodedText() {
        return EncodedText.with(this.charsetName(), this.language(), FILENAME);
    }

    @Override
    public Class<ContentDispositionFileNameEncoded> type() {
        return ContentDispositionFileNameEncoded.class;
    }

    private CharsetName charsetName() {
        return CharsetName.UTF_8;
    }

    private Optional<LanguageTagName> language() {
        return this.language("en");
    }

    private Optional<LanguageTagName> language(final String language) {
        return Optional.of(LanguageTagName.with(language));
    }
}

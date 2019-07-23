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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ContentDispositionFileNameEncodedTest extends ContentDispositionFileNameTestCase<ContentDispositionFileNameEncoded> {

    private final static String FILENAME = "filename 123";

    @Test
    public void testWith() {
        final ContentDispositionFileNameEncoded contentDisposition = this.createHeaderValue();
        this.check(contentDisposition,
                FILENAME,
                Optional.of(this.encodedText().charset()),
                this.encodedText().language());
        this.checkWithoutPath(contentDisposition, null);
    }

    @Test
    public void testWithoutPathUnnecessary() {
        final String value = "filename123";

        final ContentDispositionFileNameEncoded contentDisposition = ContentDispositionFileNameEncoded.with(encodedText(value));
        assertSame(contentDisposition, contentDisposition.withoutPath());
        this.checkWithoutPath(contentDisposition, contentDisposition);
    }

    @Test
    public void testWithoutPathRemoved() {
        final String value = "/path/filename123";

        final ContentDispositionFileNameEncoded contentDisposition = ContentDispositionFileNameEncoded.with(encodedText(value));
        assertNotSame(contentDisposition, contentDisposition.withoutPath());
        this.checkWithoutPath(contentDisposition, ContentDispositionFileNameEncoded.with(encodedText("filename123")));
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
    public ContentDispositionFileNameEncoded createHeaderValue() {
        return ContentDispositionFileNameEncoded.with(this.encodedText());
    }

    private EncodedText encodedText() {
        return this.encodedText(FILENAME);
    }

    private EncodedText encodedText(final String filename) {
        return EncodedText.with(this.charsetName(), this.language(), filename);
    }

    @Override
    public Class<ContentDispositionFileNameEncoded> type() {
        return ContentDispositionFileNameEncoded.class;
    }

    private CharsetName charsetName() {
        return CharsetName.UTF_8;
    }

    private Optional<LanguageName> language() {
        return this.language("en");
    }

    private Optional<LanguageName> language(final String language) {
        return Optional.of(LanguageName.with(language));
    }
}

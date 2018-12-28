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

import java.util.Optional;

public final class ContentDispositionFileNameEncodedEqualityTest extends ContentDispositionFileNameEqualityTestCase<ContentDispositionFileNameEncoded> {

    @Test
    public void testDifferentCharsetName() {
        this.checkNotEquals(ContentDispositionFileNameEncoded.with(
                EncodedText.with(CharsetName.UTF_16,
                        this.language(),
                        FILENAME)));
    }

    @Test
    public void testDifferentLanguage() {
        this.checkNotEquals(ContentDispositionFileNameEncoded.with(
                EncodedText.with(this.charsetName(),
                        this.language("fr"),
                        FILENAME)));
    }

    @Override
    ContentDispositionFileNameEncoded createFileName(final String name) {
        return ContentDispositionFileNameEncoded.with(EncodedText.with(this.charsetName(), this.language(), name));
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

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
import walkingkooka.InvalidCharacterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentDispositionFileNameNotEncodedTest extends ContentDispositionFileNameTestCase<ContentDispositionFileNameNotEncoded> {

    private final static String FILENAME = "filename123";

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionFileNameNotEncoded.with("");
        });
    }

    @Test
    public void testWithInvalidCharFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionFileNameNotEncoded.with("\0");
        });
    }

    @Test
    public void testWithForwardSlashFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            ContentDispositionFileNameNotEncoded.with("/path/to/file");
        });
    }

    @Test
    public void testWithBackSlashFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            ContentDispositionFileNameNotEncoded.with("\\path\\to\\file");
        });
    }

    @Test
    public void testWith() {
        final String value = "filename123";
        this.check(ContentDispositionFileNameNotEncoded.with(value),
                value,
                ContentDispositionFileNameNotEncoded.NO_CHARSET,
                ContentDispositionFileNameNotEncoded.NO_LANGUAGE);
    }

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(FILENAME);
    }

    @Test
    public void testDifferentFileName() {
        this.checkNotEquals(ContentDispositionFileNameNotEncoded.with("different"));
    }

    @Test
    public void testFileNameCaseSensitive() {
        this.checkNotEquals(ContentDispositionFileNameNotEncoded.with(FILENAME.toUpperCase()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(ContentDispositionFileNameNotEncoded.with(FILENAME), FILENAME);
    }

    @Override
    protected ContentDispositionFileNameNotEncoded createHeaderValue() {
        return ContentDispositionFileNameNotEncoded.with(FILENAME);
    }

    @Override
    public Class<ContentDispositionFileNameNotEncoded> type() {
        return ContentDispositionFileNameNotEncoded.class;
    }
}

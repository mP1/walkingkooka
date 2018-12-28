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
import walkingkooka.InvalidCharacterException;

import static org.junit.Assert.assertEquals;

public final class ContentDispositionFileNameNotEncodedTest extends ContentDispositionFileNameTestCase<ContentDispositionFileNameNotEncoded> {

    private final static String FILENAME = "filename123";

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        ContentDispositionFileNameNotEncoded.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharFails() {
        ContentDispositionFileNameNotEncoded.with("\0");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testWithForwardSlashFails() {
        ContentDispositionFileNameNotEncoded.with("/path/to/file");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testWithBackSlashFails() {
        ContentDispositionFileNameNotEncoded.with("\\path\\to\\file");
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

    private void checkValue(final ContentDispositionFileName filename, final String value) {
        assertEquals("value", value, filename.value());
    }

    @Test
    public void testToString() {
        assertEquals(FILENAME, ContentDispositionFileNameNotEncoded.with(FILENAME).toString());
    }

    @Override
    protected ContentDispositionFileNameNotEncoded createHeaderValue() {
        return ContentDispositionFileNameNotEncoded.with(FILENAME);
    }

    @Override
    protected Class<ContentDispositionFileNameNotEncoded> type() {
        return ContentDispositionFileNameNotEncoded.class;
    }
}

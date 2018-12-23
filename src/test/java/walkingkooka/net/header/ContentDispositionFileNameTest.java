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

public final class ContentDispositionFileNameTest extends HeaderValueTestCase<ContentDispositionFileName> {

    private final static String FILENAME = "filename123";

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        ContentDispositionFileName.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharFails() {
        ContentDispositionFileName.with("\0");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testWithForwardSlashFails() {
        ContentDispositionFileName.with("/path/to/file");
    }

    @Test(expected = InvalidCharacterException.class)
    public void testWithBackSlashFails() {
        ContentDispositionFileName.with("\\path\\to\\file");
    }

    @Test
    public void testWith() {
        final String value = "filename123";
        this.checkValue(ContentDispositionFileName.with(value), value);
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
        assertEquals(FILENAME, ContentDispositionFileName.with(FILENAME).toString());
    }

    @Override
    protected ContentDispositionFileName createHeaderValue() {
        return ContentDispositionFileName.with(FILENAME);
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
    protected Class<ContentDispositionFileName> type() {
        return ContentDispositionFileName.class;
    }
}

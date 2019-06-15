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

package walkingkooka.net.http.server;

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.io.file.FileExtension;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FileResponseTest implements ToStringTesting<FileResponse> {

    @Test
    public void testWithNullFilenameFails() {
        assertThrows(NullPointerException.class, () -> {
            FileResponse.with(null, this.binary());
        });
    }

    @Test
    public void testWithNullBinaryFails() {
        assertThrows(NullPointerException.class, () -> {
            FileResponse.with(this.filename(), null);
        });
    }

    @Test
    public void testWith() {
        final String filename = this.filename();
        final Binary binary = this.binary();

        final FileResponse response = FileResponse.with(filename, binary);
        assertSame(filename, response.filename());
        assertSame(binary, response.binary());
    }

    @Test
    public void testFileExtensionPresent() {
        this.fileExtensionAndCheck("file.txt");
    }

    @Test
    public void testFileExtensionAbsent() {
        this.fileExtensionAndCheck("file-extension-absent");
    }

    @Test
    public void testFileExtensionEmpty() {
        this.fileExtensionAndCheck("file.");
    }

    private void fileExtensionAndCheck(final String filename) {
        assertEquals(FileExtension.extract(filename),
                this.createFileResponse(filename).fileExtension(),
                () -> CharSequences.quoteAndEscape(filename) + " file extension");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createFileResponse(), "\"file.txt\" [0, 11, 22]");
    }

    private FileResponse createFileResponse() {
        return this.createFileResponse(this.filename());
    }

    private FileResponse createFileResponse(final String filename) {
        return FileResponse.with(filename, this.binary());
    }

    private String filename() {
        return "file.txt";
    }

    private Binary binary() {
        return Binary.with(new byte[]{00, 11, 22});
    }

    @Override
    public Class<FileResponse> type() {
        return FileResponse.class;
    }
}

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

package walkingkooka.io.file;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FileExtensionTest implements ComparableTesting2<FileExtension>,
        ToStringTesting<FileExtension> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            FileExtension.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileExtension.with("");
        });
    }

    @Test
    public void testWithContainsDotFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            FileExtension.with("a.b");
        });
    }

    @Test
    public void testExtractNullFails() {
        assertThrows(NullPointerException.class, () -> {
            FileExtension.extract(null);
        });
    }

    @Test
    public void testExtractEmptyFilename() {
        this.fileExtensionAndCheck("");
    }

    @Test
    public void testExtractPresent() {
        this.fileExtensionAndCheck("file.txt", "txt");
    }

    @Test
    public void testExtractAbsent() {
        this.fileExtensionAndCheck("file-extension-absent");
    }

    @Test
    public void testExtractEmpty() {
        this.fileExtensionAndCheck("file.");
    }

    private void fileExtensionAndCheck(final String filename) {
        this.fileExtensionAndCheck0(filename, Optional.empty());
    }

    private void fileExtensionAndCheck(final String filename, final String fileExtension) {
        this.fileExtensionAndCheck0(filename, Optional.of(FileExtension.with(fileExtension)));
    }

    private void fileExtensionAndCheck0(final String filename, final Optional<FileExtension> fileExtension) {
        assertEquals(fileExtension,
                FileExtension.extract(filename),
                () -> CharSequences.quoteAndEscape(filename) + " file extension");
    }

    @Test
    public void testWith() {
        final String value = "txt";
        final FileExtension fileExtension = FileExtension.with(value);
        assertEquals(value, fileExtension.value());
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(FileExtension.with("different"));
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEquals(FileExtension.with("TXT"));
    }

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(FileExtension.with("z"));
    }

    @Test
    public void testCompareLessCaseInsensitive() {
        this.compareToAndCheckLess(FileExtension.with("Z"));
    }

    @Test
    public void testCompareToArraySort() {
        final FileExtension txt = FileExtension.with("txt");
        final FileExtension bin = FileExtension.with("bin");
        final FileExtension exe = FileExtension.with("exe");
        final FileExtension png = FileExtension.with("png");

        this.compareToArraySortAndCheck(txt, exe, png, bin,
                bin, exe, png, txt);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createComparable(), "txt");
    }

    @Override
    public FileExtension createComparable() {
        return FileExtension.with("txt");
    }

    @Override
    public Class<FileExtension> type() {
        return FileExtension.class;
    }
}

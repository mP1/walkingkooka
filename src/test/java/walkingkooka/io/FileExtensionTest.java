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

package walkingkooka.io;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HasValueTesting;
import walkingkooka.InvalidCharacterException;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.text.CharSequences;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FileExtensionTest implements ComparableTesting2<FileExtension>,
    ConstantsTesting<FileExtension>,
    HasValueTesting,
    ToStringTesting<FileExtension>,
    CanBeEmptyTesting,
    PredicateTesting {

    // constants........................................................................................................

    @Override
    public Set<FileExtension> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // extract..........................................................................................................

    @Test
    public void testExtractNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> FileExtension.extract(null)
        );
    }

    @Test
    public void testExtractEmptyFilenameWithoutFileExtension() {
        this.fileExtensionAndCheck("");
    }

    @Test
    public void testExtractEmptyFilenameWithFileExtension() {
        this.fileExtensionAndCheck(
            ".txt",
            FileExtension.TXT
        );
    }

    @Test
    public void testExtractWithFileExtension() {
        this.fileExtensionAndCheck(
            "file.txt",
            "txt"
        );
    }

    @Test
    public void testExtractWithFileExtension2() {
        this.fileExtensionAndCheck(
            "file.hello.txt",
            FileExtension.with("hello")
                .append(FileExtension.TXT)
        );
    }

    @Test
    public void testExtractWithoutFileExtension() {
        this.fileExtensionAndCheck("file-extension-absent");
    }

    @Test
    public void testExtractEmpty() {
        this.fileExtensionAndCheck(
            "file.",
            Optional.of(
                FileExtension.with("")
            )
        );
    }

    private void fileExtensionAndCheck(final String filename) {
        this.fileExtensionAndCheck(
            filename,
            Optional.empty()
        );
    }

    private void fileExtensionAndCheck(final String filename,
                                       final String fileExtension) {
        this.fileExtensionAndCheck(
            filename,
            Optional.of(
                FileExtension.with(fileExtension)
            )
        );
    }

    private void fileExtensionAndCheck(final String filename,
                                       final FileExtension fileExtension) {
        this.fileExtensionAndCheck(
            filename,
            Optional.of(fileExtension)
        );
    }

    private void fileExtensionAndCheck(final String filename,
                                       final Optional<FileExtension> fileExtension) {
        final Optional<FileExtension> actual = FileExtension.extract(filename);

        this.checkEquals(
            fileExtension,
            actual,
            () -> CharSequences.quoteAndEscape(filename) + " file extension"
        );

        if (fileExtension.isPresent()) {
            final int dot = filename.indexOf('.');
            this.valueAndCheck(
                fileExtension.get(),
                filename.substring(dot + 1)
            );
        }
    }

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> FileExtension.with(null)
        );
    }

    @Test
    public void testWithContainsDotFails() {
        assertThrows(
            InvalidCharacterException.class,
            () -> FileExtension.with("a.b")
        );
    }

    @Test
    public void testWithEmpty() {
        assertSame(
            FileExtension.EMPTY,
            FileExtension.with("")
        );
    }

    @Test
    public void testWithExpression() {
        assertSame(
            FileExtension.EXPRESSION,
            FileExtension.with("expression.txt")
        );
    }

    @Test
    public void testWithJson() {
        assertSame(
            FileExtension.JSON,
            FileExtension.with("jsON")
        );
    }

    @Test
    public void testWithProperties() {
        assertSame(
            FileExtension.PROPERTIES,
            FileExtension.with("properties")
        );
    }

    @Test
    public void testWithTxt() {
        assertSame(
            FileExtension.TXT,
            FileExtension.with("Txt")
        );
    }

    @Test
    public void testWith() {
        final String value = "bin";
        final FileExtension fileExtension = FileExtension.with(value);
        this.valueAndCheck(
            fileExtension,
            value
        );

        this.parentAndCheck(fileExtension);
    }

    @Test
    public void testConstantJson() {
        this.parentAndCheck(
            FileExtension.JSON
        );
    }

    @Test
    public void testConstantProperties() {
        this.parentAndCheck(
            FileExtension.PROPERTIES
        );
    }

    @Test
    public void testConstantTxt() {
        this.parentAndCheck(
            FileExtension.TXT
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
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

        this.compareToArraySortAndCheck(
            txt, exe, png, bin,
            bin, exe, png, txt
        );
    }

    @Override
    public FileExtension createComparable() {
        return FileExtension.with("txt");
    }

    // CanBeEmpty.......................................................................................................

    @Test
    public void testCanBeEmptyEmpty() {
        this.isEmptyAndCheck(
            FileExtension.with(""),
            true
        );
    }

    @Test
    public void testCanBeEmptyNotEmpty() {
        this.isEmptyAndCheck(
            FileExtension.with("txt"),
            false
        );
    }

    // append...........................................................................................................

    @Test
    public void testAppendWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> FileExtension.TXT.append(null)
        );
    }

    @Test
    public void testAppend() {
        this.valueAndCheck(
            FileExtension.with("style")
                .append(FileExtension.TXT),
            "style.txt"
        );
    }

    // parent...........................................................................................................

    private void parentAndCheck(final FileExtension fileExtension) {
        this.parentAndCheck(
            fileExtension,
            FileExtension.NO_PARENT
        );
    }

    private void parentAndCheck(final FileExtension fileExtension,
                                final FileExtension parent) {
        this.parentAndCheck(
            fileExtension,
            Optional.of(parent)
        );
    }

    private void parentAndCheck(final FileExtension fileExtension,
                                final Optional<FileExtension> expected) {
        this.checkEquals(
            expected,
            fileExtension.parent(),
            fileExtension::toString
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createComparable(),
            "txt"
        );
    }

    @Test
    public void testToStringDoubleFileExtension() {
        this.toStringAndCheck(
            FileExtension.extract("file.tar.gz")
                .get(),
            "tar.gz"
        );
    }

    // Predicate........................................................................................................

    @Test
    public void testTestNullFalse() {
        this.testAndCheck(
            FileExtension.TXT,
            null,
            false
        );
    }

    @Test
    public void testTestTxtTestSame() {
        this.testAndCheck2(
            "file1.txt",
            "file2.txt",
            true
        );
    }

    @Test
    public void testTestTxtTestDifferent() {
        this.testAndCheck2(
            "file3.txt",
            "file4.exe",
            false
        );
    }

    @Test
    public void testTestTxtTestMoreSame() {
        this.testAndCheck2(
            "file5.txt",
            "file6.jan.txt",
            true
        );
    }

    @Test
    public void testTestTxtTestLessSame() {
        this.testAndCheck2(
            "file5.jan.txt",
            "file6.dec.txt",
            false
        );
    }

    @Test
    public void testTestTxtTestMoreDifferent() {
        this.testAndCheck2(
            "file1.txt",
            "file2.diff1.diff2",
            false
        );
    }

    private void testAndCheck2(final String left,
                              final String right,
                              final boolean expected) {
        this.testAndCheck(
            FileExtension.extract(left)
                .get(),
            FileExtension.extract(right)
                .get(),
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<FileExtension> type() {
        return FileExtension.class;
    }
}

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

import walkingkooka.CanBeEmpty;
import walkingkooka.Cast;
import walkingkooka.HasValue;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents the file extension with a filename.
 */
public final class FileExtension implements
    Comparable<FileExtension>,
    HasValue<String>,
    CanBeEmpty,
    Predicate<FileExtension> {

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Extracts the file extension if present from the given filename.
     */
    public static Optional<FileExtension> extract(final String filename) {
        Objects.requireNonNull(filename, "filename");

        FileExtension fileExtension = null;

        final int start = filename.lastIndexOf(SEPARATOR);
        if (-1 != start) {
            fileExtension = extractNext(
                filename,
                start,
                new FileExtension(
                    filename.substring(start + 1), // after DOT to end of filename
                    NO_PARENT
                )
            );
        }

        return Optional.ofNullable(fileExtension);
    }

    private static FileExtension extractNext(final String filename,
                                             final int end,
                                             final FileExtension parent) {
        final int start = filename.lastIndexOf(
            SEPARATOR,
            end - 1
        );

        return -1 == start ?
            parent :
            extractNext(
                filename,
                start,
                new FileExtension(
                    filename.substring(
                        start + 1,
                        end
                    ),
                    Optional.of(parent)
                )
            );
    }

    public final static Optional<FileExtension> NO_PARENT = Optional.empty();

    private final static Map<String, FileExtension> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    public final static FileExtension EMPTY = registerConstant("");

    public final static FileExtension CLASS = registerConstant("class");

    public final static FileExtension CSV = registerConstant("csv");

    public final static FileExtension JSON = registerConstant("json");

    public final static FileExtension PROPERTIES = registerConstant("properties");

    public final static FileExtension TAB = registerConstant("tab");

    public final static FileExtension TSV = registerConstant("tsv");

    public final static FileExtension TXT = registerConstant("txt");

    public final static FileExtension EXPRESSION = registerConstant(
        new FileExtension(
            "expression",
            Optional.of(TXT)
        )
    );

    private static FileExtension registerConstant(final String string) {
        return registerConstant(
            new FileExtension(
                string,
                NO_PARENT
            )
        );
    }

    private static FileExtension registerConstant(final FileExtension fileExtension) {
        CONSTANTS.put(
            fileExtension.toString(),
            fileExtension
        );
        return fileExtension;
    }

    /**
     * Factory that creates a {@link FileExtension}
     */
    public static FileExtension parse(final String value) {
        Objects.requireNonNull(value, "value");

        return parseWithParent(
            value,
            NO_PARENT
        );
    }

    private static FileExtension parseWithParent(final String value,
                                                 final Optional<FileExtension> parent) {
        Objects.requireNonNull(value, "value");

        FileExtension fileExtension = CONSTANTS.get(value);
        if (null == fileExtension) {
            final int dot = value.indexOf(SEPARATOR);
            if (dot != -1) {
                fileExtension = parseWithParent(
                    value.substring(
                        0,
                        dot
                    ),
                    Optional.of(
                        parse(
                            value.substring(
                                dot + 1
                            )
                        )
                    )
                );
            } else {
                fileExtension = new FileExtension(
                    value,
                    parent
                );
            }
        }

        return fileExtension;
    }

    public final static char SEPARATOR = '.';

    /**
     * Private ctor use public static methods.
     */
    private FileExtension(final String value,
                          final Optional<FileExtension> parent) {
        super();
        this.value = value;
        this.parent = parent;
    }

    /**
     * Appends the given {@link FileExtension} to this one.
     * <pre>
     * style + txt -> style.txt
     * </pre>
     */
    public FileExtension append(final FileExtension extension) {
        Objects.requireNonNull(extension, "extension");

        return new FileExtension(
            this.value,
            Optional.of(extension)
        );
    }

    public Optional<FileExtension> parent() {
        return this.parent;
    }

    private final Optional<FileExtension> parent;

    // Value...........................................................................................................

    @Override
    public String value() {
        final String value = this.value;
        final String parentOrNull = this.parent.map(FileExtension::value)
            .orElse(null);

        return null != parentOrNull ?
            value + SEPARATOR + parentOrNull :
            value;
    }

    private final String value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            CASE_SENSITIVITY.hash(this.value),
            this.parent
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof FileExtension &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final FileExtension other) {
        return 0 == this.compareTo(other);
    }

    /**
     * Dumps the complete file extension, equivalent to {@link #value()}.
     */
    @Override
    public String toString() {
        return this.value();
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final FileExtension other) {
        return CASE_SENSITIVITY.comparator()
            .compare(
                this.value(),
                other.value()
            );
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.value().isEmpty();
    }

    // Predicate........................................................................................................

    /**
     * Used to test if this {@link FileExtension} matches another.
     * <pre>
     * FileExtension.extract("file1.txt)
     *   .test(FileExtension.extract("file2.txt))
     * true
     *
     * FileExtension.extract("file3.txt)
     *   .test(FileExtension.extract("file4.dec.txt))
     * true
     *
     * FileExtension.extract("file5.jan.txt)
     *   .test(FileExtension.extract("file6.txt))
     * false
     *
     * FileExtension.extract("file7.jan.txt)
     *   .test(FileExtension.extract("file8.dec.txt))
     * false
     *
     * </pre>
     */
    @Override
    public boolean test(final FileExtension fileExtension) {
        boolean test = null != fileExtension;

        if(test) {
            final int parentCount = this.parentCount();
            int fileExtensionParentCount = fileExtension.parentCount();
            FileExtension temp = fileExtension;

            test = parentCount <= fileExtensionParentCount;

            if (test) {
                while (parentCount != fileExtensionParentCount) {
                    fileExtensionParentCount--;
                    temp = temp.parent.orElse(null);
                }

                test = CASE_SENSITIVITY.equals(
                    this.value(),
                    temp.value()
                );
            }
        }

        return test;
    }

    private int parentCount() {
        int count = 0;

        FileExtension parentOrNull = this.parent.orElse(null);
        while (null != parentOrNull) {
            count++;

            parentOrNull = parentOrNull.parent.orElse(null);
            if (null == parentOrNull) {
                break;
            }
        }

        return count;
    }
}

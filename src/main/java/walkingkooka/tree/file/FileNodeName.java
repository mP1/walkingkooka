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

package walkingkooka.tree.file;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.io.File;

/**
 * A file or directory name. Note case-sensitivity matches the same rules of the underlying filesystem.
 */
public final class FileNodeName implements Name, Comparable<FileNodeName>, HashCodeEqualsDefined {

    static CaseSensitivity fileSystemCaseSensitivity(){
        final String name = "lowercase";
        File lowercase = new File(name);
        File uppercase = new File(name.toUpperCase());
        return lowercase.equals(uppercase) ?
                CaseSensitivity.INSENSITIVE :
                CaseSensitivity.SENSITIVE;
    }

    /**
     * Matches the file system case sensitivity. The equals and compare methods use this to do their tests.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = fileSystemCaseSensitivity();

    /**
     * Factory that creates a new {@link FileNodeName}
     */
    public static FileNodeName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        return new FileNodeName(name);
    }

    private FileNodeName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Object..................................................................................................

    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof FileNodeName &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final FileNodeName other) {
        return CASE_SENSITIVITY.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FileNodeName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }
}

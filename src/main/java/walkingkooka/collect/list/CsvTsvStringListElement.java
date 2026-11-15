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

package walkingkooka.collect.list;

import java.util.Objects;

/**
 * Wraps a single {@link String} belonging to a {@link CsvStringList} or {@link TsvStringList}.
 */
abstract class CsvTsvStringListElement {

    static CsvTsvStringListElement string(final String string) {
        return new CsvTsvStringListElementString(string);
    }

    static CsvTsvStringListElement stringLineBreak(final String string) {
        return new CsvTsvStringListElementStringLineBreak(string);
    }

    CsvTsvStringListElement(final String string) {
        super();
        this.string = Objects.requireNonNull(string);
    }

    final String string;

    final CsvTsvStringListElement setString(final String string) {
        return this.string.equals(string) ?
            this :
            this.replaceString(string);
    }

    abstract CsvTsvStringListElement replaceString(final String string);

    /**
     * Tests if this {@link String} is followed by a line break.
     */
    final boolean isLineBreak() {
        return this instanceof CsvTsvStringListElementStringLineBreak;
    }

    final CsvTsvStringListElement setLineBreak() {
        return this.isLineBreak() ?
            this :
            stringLineBreak(this.string);
    }

    final CsvTsvStringListElement removeLineBreak() {
        return this.isLineBreak() ?
            string(this.string) :
            this;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            null != other && this.getClass() == other.getClass() &&
                this.string.equals(
                    ((CsvTsvStringListElement) other).string
                );
    }

    @Override
    public String toString() {
        return this.string;
    }
}

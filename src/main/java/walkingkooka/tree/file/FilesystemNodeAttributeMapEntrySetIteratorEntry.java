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

import java.util.Map.Entry;
import java.util.Objects;

/**
 * The {@link Entry} for {@link FilesystemNodeAttributeMapEntrySetIterator}
 */
final class FilesystemNodeAttributeMapEntrySetIteratorEntry implements Entry<FilesystemNodeAttributeName, String> {

    static FilesystemNodeAttributeMapEntrySetIteratorEntry with(final FilesystemNodeAttributeName key, final String value) {
        return new FilesystemNodeAttributeMapEntrySetIteratorEntry(key, value);
    }

    private FilesystemNodeAttributeMapEntrySetIteratorEntry(final FilesystemNodeAttributeName key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public FilesystemNodeAttributeName getKey() {
        return this.key;
    }

    private final FilesystemNodeAttributeName key;

    @Override
    public String getValue() {
        return this.value;
    }

    private final String value;

    /**
     * Attributes are immutable, therefore setValue will throw {@link UnsupportedOperationException}.
     */
    @Override
    public String setValue(final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this ==other ||
               other instanceof Entry &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final Entry<?, ?> other){
        return this.key.equals(other.getKey()) &&
               this.value.equals(other.getValue());
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
}

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

package walkingkooka.tree.file;

import walkingkooka.collect.set.Sets;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A readonly {@link Set} view of entries belonging to the attribute Map view for a {@link FilesystemNode}.
 */
final class FilesystemNodeAttributeMapEntrySet extends AbstractSet<Entry<FilesystemNodeAttributeName, String>> {

    static {
        Sets.registerImmutableType(FilesystemNodeAttributeMapEntrySet.class);
    }

    static FilesystemNodeAttributeMapEntrySet with(final FilesystemNode node) {
        return new FilesystemNodeAttributeMapEntrySet(node);
    }

    private FilesystemNodeAttributeMapEntrySet(final FilesystemNode node) {
        this.node = node;
    }

    @Override
    public Iterator<Entry<FilesystemNodeAttributeName, String>> iterator() {
        return FilesystemNodeAttributeMapEntrySetIterator.with(this.node);
    }

    @Override
    public int size() {
        return this.node.attributeNames().size();
    }

    private final FilesystemNode node;
}

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

import walkingkooka.collect.map.Maps;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * The {@link Iterator} returned by {@link FilesystemNodeAttributeMapEntrySet#iterator()}
 */
final class FilesystemNodeAttributeMapEntrySetIterator implements Iterator<Entry<FilesystemNodeAttributeName, String>> {

    static FilesystemNodeAttributeMapEntrySetIterator with(final FilesystemNode node) {
        return new FilesystemNodeAttributeMapEntrySetIterator(node);
    }

    private FilesystemNodeAttributeMapEntrySetIterator(final FilesystemNode node) {
        this.node = node;
        this.names = node.attributeNames().iterator();
    }

    @Override
    public boolean hasNext() {
        return this.names.hasNext();
    }

    @Override
    public Entry<FilesystemNodeAttributeName, String> next() {
        final FilesystemNodeAttributeName name = this.names.next();
        return Maps.entry(name, name.read(this.node));
    }

    private final Iterator<FilesystemNodeAttributeName> names;
    private final FilesystemNode node;

    public String toString() {
        return this.node.toString();
    }
}

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

import java.util.AbstractMap;
import java.util.Set;

/**
 * A {@link java.util.Map} that holds the attributes for both files and directories.
 */
final class FilesystemNodeAttributeMap extends AbstractMap<FilesystemNodeAttributeName, String> {

    /**
     * Factory only called by the {@link FilesystemNode} ctor.
     */

    static FilesystemNodeAttributeMap with(final FilesystemNode node) {
        return new FilesystemNodeAttributeMap(node);
    }

    private FilesystemNodeAttributeMap(final FilesystemNode node) {
        this.fileNode = node;
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.keySet().contains(key);
    }

    @Override
    public Set<Entry<FilesystemNodeAttributeName, String>> entrySet() {
        return FilesystemNodeAttributeMapEntrySet.with(this.fileNode);
    }

    @Override
    public String get(final Object key) {
        return key instanceof FilesystemNodeAttributeName ?
                this.get0(Cast.to(key)) :
                null;
    }

    private String get0(final FilesystemNodeAttributeName key) {
        return key.read(this.fileNode);
    }

    /**
     * An attribute map always has some names, thus is never empty.
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public final Set<FilesystemNodeAttributeName> keySet() {
        return this.fileNode.attributeNames();
    }

    /**
     * Simply count the attribute names for this file node.
     */
    @Override
    public int size() {
        return this.fileNode.attributeNames().size();
    }

    @Override
    public final int hashCode() {
        return this.fileNode.hashCode();
    }

    private final FilesystemNode fileNode;
}

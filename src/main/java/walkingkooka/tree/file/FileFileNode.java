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

import walkingkooka.collect.list.Lists;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Represents file {@link FileNode}
 */
final class FileFileNode extends FileNode{

    /**
     * Factory that creates a new {@link FileFileNode}. This should only be called by a {@link FileNodeContext#file(Path)}
     */
    static FileFileNode with(final Path path, final FileNodeContext context) {
        check(path, context);

        return new FileFileNode(path, context);
    }

    /**
     * Private ctor use factory.
     */
    private FileFileNode(final Path path, final FileNodeContext context) {
        super(path, context);
    }

    /**
     * Files return an empty list, while directories return {@link FileNode} for each and every entry in their directory.
     */
    @Override
    public List<FileNode> children() {
        return Lists.empty();
    }

    @Override
    public final boolean isDirectory() {
        return false;
    }

    @Override
    public final boolean isFile() {
        return true;
    }

    // Attributes......................................................................................................

    @Override
    Set<FileNodeAttributeName> attributeNames() {
        return ATTRIBUTE_NAMES;
    }

    // VisibleForTesting
    final static Set<FileNodeAttributeName> ATTRIBUTE_NAMES = EnumSet.allOf(FileNodeAttributeName.class);

    @Override
    String size() {
        if(null != this.size && this.mustLoad(FileNodeCacheAtom.SIZE)){
            this.size = null;
        }

        if(null==this.size){
            this.size = String.valueOf(this.path.toFile().length());
        }
        return this.size;
    }

    private String size;

    @Override
    String text() {
        if(null != this.text && this.mustLoad(FileNodeCacheAtom.TEXT)){
            this.text = null;
        }

        if(null==this.text){
            try {
                this.text = this.context.text(this.path);
            } catch ( final Exception cause) {
                throw this.exception("Failed to get text for " + pathToString(), cause);
            }
        }
        return this.text;
    }

    private String text;

    @Override
    String type() {
        return FILE_TYPE;
    }
}

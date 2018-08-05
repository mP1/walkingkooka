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
import walkingkooka.collect.set.Sets;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Represents directory {@link FileNode}.
 */
final class DirectoryFileNode extends FileNode{

    /**
     * Factory that creates a new {@link DirectoryFileNode}. This should only be called by a {@link FileNodeContext#directory(Path)}
     */
    static DirectoryFileNode with(final Path path, final FileNodeContext context) {
        check(path, context);

        return new DirectoryFileNode(path, context);
    }

    /**
     * Private ctor use factory.
     */
    private DirectoryFileNode(final Path path, final FileNodeContext context) {
        super(path, context);
        this.children = null;
    }

    /**
     * Directories return {@link FileNode} for each and every entry in their directory.
     */
    @Override
    public List<FileNode> children() {
        if(null != this.children && this.mustLoad(FileNodeCacheAtom.CHILDREN)){
            this.children = null;
        }
        if(null == this.children) {
            this.children = this.makeFileNodesFromDirectoryListing();
        }
        return this.children;
    }

    private List<FileNode> children;

    /**
     * Lists the immediate directory fetching nodes for each of the entries.
     */
    private List<FileNode> makeFileNodesFromDirectoryListing() {
        final List<FileNode> children = Lists.array();
        final File[] files = this.path.toFile().listFiles();

        if(null != files) {
            for (File file : files) {
                final Path path = file.toPath();
                if(file.isDirectory()) {
                    children.add(this.context.directory(path));
                }
                if(file.isFile()) {
                    children.add(this.context.file(path));
                }
            }
        }

        return Lists.readOnly(children);
    }

    @Override
    public final boolean isDirectory() {
        return true;
    }

    @Override
    public final boolean isFile() {
        return false;
    }

    @Override
    Set<FileNodeAttributeName> attributeNames() {
        return ATTRIBUTE_NAMES;
    }

    // VisibleForTesting
    final static Set<FileNodeAttributeName> ATTRIBUTE_NAMES = Sets.of(FileNodeAttributeName.CREATED,
            FileNodeAttributeName.HIDDEN,
            FileNodeAttributeName.LAST_ACCESSED,
            FileNodeAttributeName.LAST_MODIFIED,
            FileNodeAttributeName.OWNER,
            FileNodeAttributeName.TYPE);

    @Override
    String size() {
        return "";
    }

    @Override
    String text() {
        return "";
    }

    @Override
    String type() {
        return DIRECTORY_TYPE;
    }
}

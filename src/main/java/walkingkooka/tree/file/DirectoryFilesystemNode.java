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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Represents directory {@link FilesystemNode}.
 */
final class DirectoryFilesystemNode extends FilesystemNode {

    /**
     * Factory that creates a new {@link DirectoryFilesystemNode}. This should only be called by a {@link FilesystemNodeContext#directory(Path)}
     */
    static DirectoryFilesystemNode with(final Path path, final FilesystemNodeContext context) {
        check(path, context);

        return new DirectoryFilesystemNode(path, context);
    }

    /**
     * Private ctor use factory.
     */
    private DirectoryFilesystemNode(final Path path, final FilesystemNodeContext context) {
        super(path, context);
        this.children = null;
    }

    /**
     * Directories return {@link FilesystemNode} for each and every entry in their directory.
     */
    @Override
    public List<FilesystemNode> children() {
        if (null != this.children && this.mustLoad(FilesystemNodeCacheAtom.CHILDREN)) {
            this.children = null;
        }
        if (null == this.children) {
            this.children = this.makeFileNodesFromDirectoryListing();
        }
        return this.children;
    }

    private List<FilesystemNode> children;

    /**
     * Lists the immediate directory fetching nodes for each of the entries.
     */
    private List<FilesystemNode> makeFileNodesFromDirectoryListing() {
        final List<FilesystemNode> children = Lists.array();
        final File[] files = this.path.toFile().listFiles();

        if (null != files) {
            for (File file : files) {
                final Path path = file.toPath();
                if (file.isDirectory()) {
                    children.add(this.context.directory(path));
                }
                if (file.isFile()) {
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
    Set<FilesystemNodeAttributeName> attributeNames() {
        return ATTRIBUTE_NAMES;
    }

    // VisibleForTesting
    final static Set<FilesystemNodeAttributeName> ATTRIBUTE_NAMES = Sets.of(FilesystemNodeAttributeName.CREATED,
            FilesystemNodeAttributeName.HIDDEN,
            FilesystemNodeAttributeName.LAST_ACCESSED,
            FilesystemNodeAttributeName.LAST_MODIFIED,
            FilesystemNodeAttributeName.OWNER,
            FilesystemNodeAttributeName.TYPE);

    @Override
    String size() {
        return "";
    }

    @Override
    public String text() {
        return "";
    }

    @Override
    String type() {
        return DIRECTORY_TYPE;
    }
}

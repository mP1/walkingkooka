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
import walkingkooka.Value;
import walkingkooka.naming.PathSeparator;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.select.NodeSelectorBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link FileNode} represents a file or directory under a path.
 */
public abstract class FileNode implements Node<FileNode, FileNodeName, FileNodeAttributeName, String>, Value<Path> {

    public final static String DIRECTORY_TYPE = "DIRECTORY";
    public final static String FILE_TYPE = "FILE";

    /**
     * Creates a {@link FileNode} for a directory.
     */
    public static FileNode directory(final Path path, final FileNodeContext context) {
        return DirectoryFileNode.with(path, context);
    }

    /**
     * Creates a {@link FileNode} for a file.
     */
    public static FileNode file(final Path path, final FileNodeContext context) {
        return FileFileNode.with(path, context);
    }

    /**
     * Parameter checks
     */
    static void check(final Path path, final FileNodeContext context) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(context, "context");
    }

    /**
     * Absolute {@see NodeSelectorBuilder}
     */
    public static NodeSelectorBuilder<FileNode, FileNodeName, FileNodeAttributeName, String> absoluteNodeSelectorBuilder() {
        return NodeSelectorBuilder.absolute(PathSeparator.requiredAtStart('/'));
    }

    /**
     * Absolute {@see NodeSelectorBuilder}
     */
    public static NodeSelectorBuilder<FileNode, FileNodeName, FileNodeAttributeName, String> relativeNodeSelectorBuilder() {
        return NodeSelectorBuilder.relative(PathSeparator.requiredAtStart('/'));
    }
    
    /**
     * Package private to limit sub classing.
     */
    FileNode(final Path path, final FileNodeContext context) {
        this.path = path;
        this.context = context;
        this.attributes = new FileNodeAttributeMap<>(this);
    }

    @Override
    public final FileNodeName name() {
        if(null==this.name){
            // only want the filename and not a path including parent directories etc.
            this.name = FileNodeName.with(this.path.getFileName().toString());
        }
        return this.name;
    }

    private FileNodeName name;

    /**
     * Returns the parent of empty if this node is the root as determined by {@link FileNodeContext#rootPath()}
     */
    @Override
    public final Optional<FileNode> parent() {
        if(null==this.parent) {
            final Path path = this.path;
            final Path root = this.context.rootPath();
            this.parent = root.equals(path) ?
                    Optional.empty() :
                    Optional.of(this.context.directory(path.getParent()));
        }
        return this.parent;
    }

    private Optional<FileNode> parent;

    /**
     * Updating children is not supported.
     */
    @Override
    public final FileNode setChildren(final List<FileNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    // attributes...........................................................................................

    public final Map<FileNodeAttributeName, String> attributes() {
        return this.attributes;
    }

    private final Map<FileNodeAttributeName, String> attributes;

    /**
     * The fixed set of available attribute names.
     */
    abstract Set<FileNodeAttributeName> attributeNames();

    /**
     * Update attributes is not supported.
     */
    @Override
    public final FileNode setAttributes(final Map<FileNodeAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        throw new UnsupportedOperationException();
    }

    final String created() {
        if(null == this.created && this.mustLoad(FileNodeCacheAtom.CREATED)) {
            this.created = null;
        }

        if(null==this.created) {
            this.basicFileAttributes();
            this.created = this.timeToString(this.basicFileAttributes.creationTime());
        }
        return this.created;
    }

    private String created;

    final String hidden() {
        if(null == this.hidden && this.mustLoad(FileNodeCacheAtom.HIDDEN)) {
            this.hidden = null;
        }

        if(null==this.hidden) {
            try {
                this.hidden = String.valueOf(Files.isHidden(this.path));
                return this.hidden;
            } catch (final IOException cause) {
                throw exception("Failed to test whether " + this.pathToString() + " is hidden", cause);
            }
        }
        return this.hidden;
    }

    private String hidden;

    final String lastAccessed() {
        if(null == this.lastAccessed && this.mustLoad(FileNodeCacheAtom.LAST_ACCESSED)) {
            this.lastAccessed = null;
        }

        if(null==this.lastAccessed) {
            this.basicFileAttributes();
            this.lastAccessed = this.timeToString(this.basicFileAttributes.lastAccessTime());
        }
        return this.lastAccessed;
    }

    private String lastAccessed;

    final String lastModified() {
        if(null == this.lastModified && this.mustLoad(FileNodeCacheAtom.LAST_MODIFIED)) {
            this.lastModified = null;
        }

        if(null==this.lastModified) {
            this.basicFileAttributes();
            this.lastModified = this.timeToString(this.basicFileAttributes.lastAccessTime());
        }
        return this.lastModified;
    }

    private String lastModified;

    final String owner() {
        if(null == this.owner && this.mustLoad(FileNodeCacheAtom.OWNER)) {
            this.owner = null;
        }

        if(null==this.owner) {
            try {
                this.owner = this.fileAttributesView(FileOwnerAttributeView.class).getOwner().getName();
            } catch (final IOException cause) {
                throw exception("Failed to get owner attribute for " + pathToString(), cause);
            }
        }
        return this.owner;
    }

    private String owner;

    abstract String size();

    abstract String text();

    abstract String type();

    private void basicFileAttributes() {
        try {
            this.basicFileAttributes = fileAttributesView(BasicFileAttributeView.class).readAttributes();
        } catch (final IOException cause) {
            throw exception("Failed to get created, lastAccessed, lastModified attributes for " + pathToString(), cause);
        }
    }

    private BasicFileAttributes basicFileAttributes;

    private <V extends FileAttributeView> V fileAttributesView(final Class<V> view) {
        return Files.getFileAttributeView(this.path, view);
    }

    static FileNodeException exception(final String message, final Exception cause) {
        return new FileNodeException(message + ", message: " + cause.getMessage(), cause);
    }

    /**
     * Queries the context to determine if a atom belonging to the {@link FileNode} must be loaded.
     */
    final boolean mustLoad(final FileNodeCacheAtom atom) {
        return this.context.mustLoad(this, atom);
    }

    final CharSequence pathToString() {
        return CharSequences.quote(this.path.toString());
    }

    private final String timeToString(final FileTime fileTime) {
        return ISO_FORMATTER.format(LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault()));
    }

    private final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    final Path path;
    final FileNodeContext context;

    // Value..........................................................................................................

    /**
     * Returns the {@link Path} for this {@link FileNode}.
     */
    @Override
    public final Path value() {
        return this.path;
    }

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof FileNode &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final FileNode other) {
        return this.path.equals(other.path);
    }

    /**
     * Returns the full path for this file as a {@link String}
     */
    @Override
    public String toString() {
        return this.path.toString();
    }
}

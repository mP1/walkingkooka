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
import walkingkooka.text.HasText;
import walkingkooka.tree.Node;

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
 * A {@link FilesystemNode} represents a file or directory under a path.
 */
public abstract class FilesystemNode implements Node<FilesystemNode, FilesystemNodeName, FilesystemNodeAttributeName, String>,
        HasText,
        Value<Path> {

    /**
     * Creates a {@link FilesystemNode} for a directory.
     */
    public static FilesystemNode directory(final Path path, final FilesystemNodeContext context) {
        return DirectoryFilesystemNode.with(path, context);
    }

    /**
     * Creates a {@link FilesystemNode} for a file.
     */
    public static FilesystemNode file(final Path path, final FilesystemNodeContext context) {
        return FileFilesystemNode.with(path, context);
    }

    /**
     * Parameter checks
     */
    static void check(final Path path, final FilesystemNodeContext context) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(context, "context");
    }

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.requiredAtStart('/');
    
    /**
     * Package private to limit sub classing.
     */
    FilesystemNode(final Path path, final FilesystemNodeContext context) {
        this.path = path;
        this.context = context;
        this.attributes = FilesystemNodeAttributeMap.with(this);
    }

    @Override
    public final FilesystemNodeName name() {
        if(null==this.name){
            // only want the filename and not a path including parent directories etc.
            this.name = FilesystemNodeName.with(this.path.getFileName().toString());
        }
        return this.name;
    }

    private FilesystemNodeName name;

    @Override
    public final boolean hasUniqueNameAmongstSiblings() {
        return true;
    }

    /**
     * Returns the parent of empty if this node is the root as determined by {@link FilesystemNodeContext#rootPath()}
     */
    @Override
    public final Optional<FilesystemNode> parent() {
        if(null==this.parent) {
            final Path path = this.path;
            final Path root = this.context.rootPath();
            this.parent = root.equals(path) ?
                    Optional.empty() :
                    Optional.of(this.context.directory(path.getParent()));
        }
        return this.parent;
    }

    private Optional<FilesystemNode> parent;

    /**
     * Updating children is not supported.
     */
    @Override
    public final FilesystemNode setChildren(final List<FilesystemNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    // attributes...........................................................................................

    public final Map<FilesystemNodeAttributeName, String> attributes() {
        return this.attributes;
    }

    private final Map<FilesystemNodeAttributeName, String> attributes;

    /**
     * The fixed set of available attribute names.
     */
    abstract Set<FilesystemNodeAttributeName> attributeNames();

    /**
     * Update attributes is not supported.
     */
    @Override
    public final FilesystemNode setAttributes(final Map<FilesystemNodeAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        throw new UnsupportedOperationException();
    }

    final String created() {
        if(null == this.created && this.mustLoad(FilesystemNodeCacheAtom.CREATED)) {
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
        if(null == this.hidden && this.mustLoad(FilesystemNodeCacheAtom.HIDDEN)) {
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
        if(null == this.lastAccessed && this.mustLoad(FilesystemNodeCacheAtom.LAST_ACCESSED)) {
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
        if(null == this.lastModified && this.mustLoad(FilesystemNodeCacheAtom.LAST_MODIFIED)) {
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
        if(null == this.owner && this.mustLoad(FilesystemNodeCacheAtom.OWNER)) {
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

    abstract public String text();

    abstract String type();

    final static String DIRECTORY_TYPE = "DIRECTORY";
    final static String FILE_TYPE = "FILE";

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

    static FilesystemNodeException exception(final String message, final Exception cause) {
        return new FilesystemNodeException(message + ", message: " + cause.getMessage(), cause);
    }

    /**
     * Queries the context to determine if a atom belonging to the {@link FilesystemNode} must be loaded.
     */
    final boolean mustLoad(final FilesystemNodeCacheAtom atom) {
        return this.context.mustLoad(this, atom);
    }

    final CharSequence pathToString() {
        return CharSequences.quote(this.path.toString());
    }

    private String timeToString(final FileTime fileTime) {
        return ISO_FORMATTER.format(LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault()));
    }

    private final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    final Path path;
    final FilesystemNodeContext context;

    // Value..........................................................................................................

    /**
     * Returns the {@link Path} for this {@link FilesystemNode}.
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
               other instanceof FilesystemNode &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final FilesystemNode other) {
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

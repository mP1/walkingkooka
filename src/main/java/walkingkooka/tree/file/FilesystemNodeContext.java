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

import walkingkooka.Context;

import java.nio.file.Path;

/**
 * A context that accompanies the process of reading the filesystem and creating and updating the {@link FilesystemNode} instances
 * created to match. Because some attributes of a file, such as the text can be costly its not ideal to constantly
 * process the file again to get the "text" attribute so the context is responsible for controlling / clearing
 * cached values which will then be reloaded.
 */
public interface FilesystemNodeContext extends Context {

    /**
     * The root path of the file system being represented as a {@link FilesystemNode} graph.
     */
    Path rootPath();

    /**
     * Factory that creates a {@link FilesystemNode}. Implementations should use a cache of some sort matching paths to filenodes.
     */
    FilesystemNode directory(final Path path);

    /**
     * Factory that creates a {@link FilesystemNode}. Implementations should use a cache of some sort matching paths to filenodes.
     */
    FilesystemNode file(final Path path);

    /**
     * This is called prior to any attribute or children being returned.
     */
    boolean mustLoad(final FilesystemNode node, final FilesystemNodeCacheAtom atom);

    /**
     * Returns the text form for the given path. This might be as simple as reading a text file, returning nothing if
     * too many binary type characters/bytes are found, or doing something a little extra such as identifying the file to be
     * a PDF and extracting text from that.
     */
    String text(final Path path) throws Exception;
}

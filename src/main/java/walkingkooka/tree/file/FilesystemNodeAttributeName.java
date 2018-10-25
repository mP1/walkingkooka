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

import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;

/**
 * A file or directory name. Note case-sensitivity matches the same rules of the underlying filesystem.
 * A directory has the following attributes: CREATED, GROUP, HIDDEN, LAST_ACCESSED, LAST_MODIFIED, OWNER, TYPE
 * A file has the following attributes: CREATED, GROUP, HIDDEN, LAST_ACCESSED, LAST_MODIFIED, OWNER, SIZE, TEXT, TYPE
 */
public enum FilesystemNodeAttributeName implements Name, Comparable<FilesystemNodeAttributeName>, HashCodeEqualsDefined {

    /**
     * An ISO_FORMATTED timestamp of the creation, taken from {@link java.nio.file.attribute.BasicFileAttributes}
     * {@see java.time.format.DateTimeFormatter}
     */
    CREATED{
        @Override
        String read(final FilesystemNode node){
            return node.created();
        }
    },

    /**
     * A String holding either the boolean value of true of false using {@link java.nio.file.Files#isHidden}.
     */
    HIDDEN{
        @Override
        String read(final FilesystemNode node){
            return node.hidden();
        }
    },

    /**
     * An ISO_FORMATTED timestamp of the last access, taken from {@link java.nio.file.attribute.BasicFileAttributes}
     * {@see java.time.format.DateTimeFormatter}
     */
    LAST_ACCESSED{
        @Override
        String read(final FilesystemNode node){
            return node.lastAccessed();
        }
    },

    /**
     * An ISO_FORMATTED timestamp of the last modification, taken from {@link java.nio.file.attribute.BasicFileAttributes}
     * {@see java.time.format.DateTimeFormatter}
     */
    LAST_MODIFIED{
        @Override
        String read(final FilesystemNode node){
            return node.lastModified();
        }
    },

    /**
     * The owner, taken from {@link java.nio.file.attribute.PosixFileAttributes}
     */
    OWNER{
        @Override
        String read(final FilesystemNode node){
            return node.owner();
        }
    },

    /**
     * The size of the file in bytes
     * (File only attribute)
     */
    SIZE{
        @Override
        String read(final FilesystemNode node){
            return node.size();
        }
    },

    /**
     * The text attribute holds the text for a given file.
     * (File only attribute)
     */
    TEXT{
        @Override
        String read(final FilesystemNode node){
            return node.text();
        }
    },

    /**
     * A String value that currently holds either: FILE or DIRECTORY.
     */
    TYPE{
        @Override
        String read(final FilesystemNode node){
            return node.type();
        }
    };

    abstract String read(final FilesystemNode node);

    @Override
    public final String value() {
        return this.toString();
    }
}

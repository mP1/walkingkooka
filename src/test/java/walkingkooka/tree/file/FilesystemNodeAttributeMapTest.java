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

import walkingkooka.collect.map.MapTestCase;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FilesystemNodeAttributeMapTest extends MapTestCase<FilesystemNodeAttributeMap,
        FilesystemNodeAttributeName,
        String> {

    @Override
    protected FilesystemNodeAttributeMap createMap() {
        return FilesystemNodeAttributeMap.with(this.createNode());
    }

    private FilesystemNode createNode() {
        final Path home = Paths.get(".");
        return FilesystemNode.directory(home, new FilesystemNodeContext() {

            @Override
            public Path rootPath() {
                return home;
            }

            @Override
            public FilesystemNode directory(final Path path) {
                throw new UnsupportedOperationException();
            }

            @Override
            public FilesystemNode file(final Path path) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean mustLoad(final FilesystemNode node, final FilesystemNodeCacheAtom atom) {
                return true;
            }

            @Override
            public String text(final Path path) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Override
    public Class<FilesystemNodeAttributeMap> type() {
        return FilesystemNodeAttributeMap.class;
    }
}

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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class FilesystemNodeCacheAtomTest implements ClassTesting2<FilesystemNodeCacheAtom> {

    @Test
    public void testAtoms() {
        final Set<String> atoms = names(FilesystemNodeCacheAtom.class);
        final Set<String> names = names(FilesystemNodeAttributeName.class);
        names.add(FilesystemNodeCacheAtom.CHILDREN.name());

        assertEquals(atoms, names);
    }

    private <E extends Enum<E>> Set<String> names(final Class<E> constants) {
        final Set<E> all = EnumSet.allOf(constants);
        return all.stream()
                .map(e -> e.name())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Class<FilesystemNodeCacheAtom> type() {
        return FilesystemNodeCacheAtom.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

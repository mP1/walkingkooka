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

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class FileNodeCacheAtomTest extends PublicClassTestCase<FileNodeCacheAtom> {

    @Test
    public void testAtoms() {
        final Set<String> atoms = names(FileNodeCacheAtom.class);
        final Set<String> names = names(FileNodeAttributeName.class);
        names.add(FileNodeCacheAtom.CHILDREN.name());

        assertEquals(atoms, names);
    }

    private Set<String> names(final Class<? extends Enum> constants) {
        final Set<? extends Enum> all = EnumSet.allOf(constants);
        return all.stream()
                .map( e -> ((Enum) e).name())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    protected Class<FileNodeCacheAtom> type() {
        return FileNodeCacheAtom.class;
    }
}

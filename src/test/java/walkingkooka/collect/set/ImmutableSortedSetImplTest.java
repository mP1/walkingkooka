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

package walkingkooka.collect.set;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.TreeSet;

public final class ImmutableSortedSetImplTest implements ImmutableSortedSetTesting<ImmutableSortedSetImpl<String>, String>,
        IteratorTesting {

    @Test
    public void testConcat() {
        this.concatAndCheck(
                this.createSet(),
                "appended3",
                ImmutableSortedSetImpl.with(
                        new TreeSet<>(
                                Sets.of(
                                        "1a",
                                        "2b",
                                        "appended3"
                                )
                        )
                )
        );
    }

    @Test
    public void testContains() {
        this.containsAndCheck(
                this.createSet(),
                "1a"
        );
    }

    @Test
    public void testContainsAbsent() {
        this.containsAndCheckAbsent(
                this.createCollection(),
                "absent"
        );
    }

    @Test
    public void testDelete() {
        this.deleteAndCheck(
                this.createSet(),
                "2b",
                ImmutableSortedSetImpl.with(
                        new TreeSet<>(
                                Sets.of(
                                        "1a"
                                )
                        )
                )
        );
    }

    @Test
    public void testReplace() {
        this.replaceAndCheck(
                this.createSet(),
                "2b",
                "replaced2b",
                ImmutableSortedSetImpl.with(
                        new TreeSet<>(
                                Sets.of(
                                        "1a",
                                        "replaced2b"
                                )
                        )
                )
        );
    }

    @Test
    public void testReplaceOldMissing() {
        this.replaceAndCheck(
                this.createSet(),
                "*missing*",
                "replaced2b"
        );
    }

    @Override
    public ImmutableSortedSetImpl<String> createSet() {
        return Cast.to(
                ImmutableSortedSetImpl.with(
                        new TreeSet<>(
                                Sets.of(
                                        "1a",
                                        "2b"
                                )
                        )
                )
        );
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableSortedSetImpl<String>> type() {
        return Cast.to(ImmutableSortedSetImpl.class);
    }
}

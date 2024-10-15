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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ImmutableSetImplTestCase2<S extends ImmutableSetImpl<String>> extends ImmutableSetImplTestCase<S>
        implements SetTesting2<S, String>,
        HashCodeEqualsDefinedTesting2<S>,
        IteratorTesting,
        ImmutableSetTesting<S, String> {

    ImmutableSetImplTestCase2() {
        super();
    }

    @Test
    public final void testContainsAbsent() {
        this.containsAndCheckAbsent(this.createSet(), "!absent!");
    }

    @Test
    public final void testIteratorRemoveFails() {
        final Iterator<String> iterator = this.createSet().iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Override
    public final S createObject() {
        return this.createSet();
    }
}

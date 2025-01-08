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

package walkingkooka.collect.iterator;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.Predicates;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PredicatedIteratorTest extends IteratorTestCase<PredicatedIterator<String>, String> {

    private final static Iterator<String> ITERATOR = Iterators.fake();

    // with.........................................................................................................

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(
            NullPointerException.class,
            () -> PredicatedIterator.with(
                null,
                Predicates.fake()
            )
        );
    }

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(
            NullPointerException.class,
            () -> PredicatedIterator.with(
                ITERATOR,
                null
            )
        );
    }

    // iterator.........................................................................................................

    @Test
    public void testKeepsAll() {
        this.iterateAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b",
                    "3c"
                ).iterator(),
                Predicates.always()
            ),
            "1a",
            "2b",
            "3c"
        );
    }

    @Test
    public void testKeepsAll2() {
        this.iterateUsingHasNextAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b",
                    "3c"
                ).iterator(),
                Predicates.always()
            ),
            "1a",
            "2b",
            "3c"
        );
    }

    @Test
    public void testKeepsNone() {
        this.iterateAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b",
                    "3c"
                ).iterator(),
                Predicates.never()
            )
        );
    }

    @Test
    public void testKeepNone2() {
        this.iterateUsingHasNextAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b",
                    "3c"
                ).iterator(),
                Predicates.never()
            )
        );
    }

    @Test
    public void testKeepSame() {
        this.iterateAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b!",
                    "3c",
                    "4d!"
                ).iterator(),
                (s) -> false == s.contains("!")
            ),
            "1a",
            "3c"
        );
    }

    @Test
    public void testKeepSame2() {
        this.iterateUsingHasNextAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b!",
                    "3c",
                    "4d!"
                ).iterator(),
                (s) -> false == s.contains("!")
            ),
            "1a",
            "3c"
        );
    }

    @Test
    public void testKeepSame3() {
        this.iterateAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b!",
                    "3c",
                    "4d!",
                    "5e!",
                    "6f"
                ).iterator(),
                (s) -> false == s.contains("!")
            ),
            "1a",
            "3c",
            "6f"
        );
    }

    @Test
    public void testKeepSame4() {
        this.iterateUsingHasNextAndCheck(
            PredicatedIterator.with(
                Lists.of(
                    "1a",
                    "2b!",
                    "3c",
                    "4d!",
                    "5e!",
                    "6f"
                ).iterator(),
                (s) -> false == s.contains("!")
            ),
            "1a",
            "3c",
            "6f"
        );
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveFails() {
        this.removeUnsupportedFails(
            this.createIterator()
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createIterator(),
            ITERATOR.toString()
        );
    }

    @Override
    public PredicatedIterator<String> createIterator() {
        return PredicatedIterator.with(
            ITERATOR,
            Predicates.fake()
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<PredicatedIterator<String>> type() {
        return Cast.to(PredicatedIterator.class);
    }
}

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

final public class IteratorOneTest extends IteratorTestCase<IteratorOne<String>, String> {

    // constants

    private final static String NULL = null;

    private final static String VALUE = "*Value*";

    // tests

    @Test
    public void testNullValueHasNext() {
        this.iterateUsingHasNextAndCheck(IteratorOne.with(NULL),
            NULL);
    }

    @Test
    public void testNullValueNext() {
        this.iterateAndCheck(IteratorOne.with(NULL), NULL);
    }

    @Test
    public void testNonNullValueHasNext() {
        this.iterateUsingHasNextAndCheck(IteratorOne.with(VALUE),
            VALUE);
    }

    @Test
    public void testNonNullValueNext() {
        this.iterateAndCheck(IteratorOne.with(VALUE), VALUE);
    }

    @Test
    public void testNonNullValueToString() {
        final IteratorOne<String> iterator = IteratorOne.with(VALUE);
        this.checkEquals(VALUE, iterator.toString());
    }

    @Test
    public void testEmptyToString() {
        final IteratorOne<String> iterator = IteratorOne.with(null);
        iterator.next();
        this.checkEquals("<empty>", iterator.toString());
    }

    @Test
    public void testWhenEmptyNextFails() {
        final IteratorOne<String> iterator = IteratorOne.with(null);
        iterator.next();
        this.nextFails(iterator);
    }

    @Test
    public void testRemoveFails() {
        final IteratorOne<String> iterator = IteratorOne.with(null);
        this.removeUnsupportedFails(iterator);
    }

    @Override
    public IteratorOne<String> createIterator() {
        return null;
    }

    @Override
    public Class<IteratorOne<String>> type() {
        return Cast.to(IteratorOne.class);
    }
}

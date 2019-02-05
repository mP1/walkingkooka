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
 */

package walkingkooka.collect.iterator;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class OneIteratorTest extends IteratorTestCase<OneIterator<String>, String> {

    // constants

    private final static String NULL = null;

    private final static String VALUE = "*Value*";

    // tests

    @Test
    public void testNullValueHasNext() {
        this.iterateUsingHasNextAndCheck(OneIterator.with(NULL),
                NULL);
    }

    @Test
    public void testNullValueNext() {
        this.iterateAndCheck(OneIterator.with(NULL), NULL);
    }

    @Test
    public void testNonNullValueHasNext() {
        this.iterateUsingHasNextAndCheck(OneIterator.with(VALUE),
                VALUE);
    }

    @Test
    public void testNonNullValueNext() {
        this.iterateAndCheck(OneIterator.with(VALUE), VALUE);
    }

    @Test
    public void testNonNullValueToString() {
        final OneIterator<String> iterator = OneIterator.with(VALUE);
        assertEquals(VALUE, iterator.toString());
    }

    @Test
    public void testEmptyToString() {
        final OneIterator<String> iterator = OneIterator.with(null);
        iterator.next();
        assertEquals("<empty>", iterator.toString());
    }

    @Test
    public void testWhenEmptyNextFails() {
        final OneIterator<String> iterator = OneIterator.with(null);
        iterator.next();
        this.checkNextFails(iterator);
    }

    @Test
    public void testRemoveFails() {
        final OneIterator<String> iterator = OneIterator.with(null);
        this.checkRemoveUnsupportedFails(iterator);
    }

    @Override public OneIterator<String> createIterator() {
        return null;
    }

    @Override
    protected Class<OneIterator<String>> type() {
        return Cast.to(OneIterator.class);
    }
}

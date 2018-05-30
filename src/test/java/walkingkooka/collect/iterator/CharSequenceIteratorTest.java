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

import org.junit.Assert;
import org.junit.Test;

final public class CharSequenceIteratorTest
        extends IteratorTestCase<CharSequenceIterator, Character> {

    // constants

    private final static CharSequence CHARS = new StringBuilder("ABC\t");

    // tests

    @Test
    public void testWithNullFails() {
        try {
            CharSequenceIterator.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testConsumeEmpty() {
        this.iterateAndCheck(CharSequenceIterator.with(""));
    }

    @Test
    public void testConsume() {
        this.iterateAndCheck('A', 'B', 'C', '\t');
    }

    @Test
    public void testRemoveFails() {
        final CharSequenceIterator iterator = this.createIterator();
        iterator.next();
        this.checkRemoveFails(iterator);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("\"ABC\\t\"", this.createIterator().toString());
    }

    @Override
    protected CharSequenceIterator createIterator() {
        return CharSequenceIterator.with(CharSequenceIteratorTest.CHARS);
    }

    @Override
    protected Class<CharSequenceIterator> type() {
        return CharSequenceIterator.class;
    }
}

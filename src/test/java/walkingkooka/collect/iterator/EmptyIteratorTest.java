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
import walkingkooka.Cast;

final public class EmptyIteratorTest extends IteratorTestCase<EmptyIterator<Void>, Void> {

    @Test
    public void testHasNext() {
        this.checkHasNextFalse(EmptyIterator.instance());
    }

    @Test
    public void testNext() {
        this.checkNextFails(EmptyIterator.instance());
    }

    @Test
    public void testRemove() {
        this.checkRemoveWithoutNextFails(EmptyIterator.instance());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("<empty>", EmptyIterator.instance().toString());
    }

    @Override
    protected EmptyIterator<Void> createIterator() {
        return EmptyIterator.instance();
    }

    @Override
    protected Class<EmptyIterator<Void>> type() {
        return Cast.to(EmptyIterator.class);
    }
}

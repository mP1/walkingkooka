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

package walkingkooka.collect.enumeration;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Enumeration;

final public class ArrayEnumerationTest
        extends EnumerationTestCase<ArrayEnumeration<String>, String> {

    @Test
    public void testWithNullArrayFails() {
        try {
            ArrayEnumeration.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testConsumedUsingHasNextNext() {
        this.enumerateUsingHasMoreElementsAndCheck(this.createEnumeration(), "A", "B", "C");
    }

    @Test
    public void testConsumedUsingNext() {
        this.enumerateAndCheck(this.createEnumeration(), "A", "B", "C");
    }

    @Test
    public void testArrayCopied() {
        final String[] items = new String[]{"A", "B", "C"};
        final Enumeration<String> enumerator = ArrayEnumeration.with(items);
        items[0] = "D";
        items[1] = "E";
        items[2] = "F";

        this.enumerateUsingHasMoreElementsAndCheck(enumerator, "A", "B", "C");
    }

    @Test
    public void testToString() {
        final Enumeration<String> enumerator = this.createEnumeration();
        Assert.assertEquals(Lists.of("A", "B", "C").toString(), enumerator.toString());
    }

    @Test
    public void testToStringPartiallyConsumed() {
        final Enumeration<String> enumerator = this.createEnumeration();
        enumerator.nextElement();

        Assert.assertEquals(Lists.of("B", "C").toString(), enumerator.toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final Enumeration<String> enumerator = this.createEnumeration();
        enumerator.nextElement();
        enumerator.nextElement();
        enumerator.nextElement();
        Assert.assertEquals(Lists.of().toString(), enumerator.toString());
    }

    @Override
    protected ArrayEnumeration<String> createEnumeration() {
        return ArrayEnumeration.with(new Object[]{"A", "B", "C"});
    }

    @Override
    protected Class<ArrayEnumeration<String>> type() {
        return Cast.to(ArrayEnumeration.class);
    }
}

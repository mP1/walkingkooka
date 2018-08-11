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
import java.util.NoSuchElementException;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

final public class EnumerationChainTest
        extends EnumerationTestCase<EnumerationChain<String>, String> {

    // constants

    private final static Enumeration<String> FIRST = Enumerations.fake();

    private final static Enumeration<String> SECOND = Enumerations.fake();

    // tests

    @Override
    @Test
    public void testNaming() {
        this.checkNamingStartAndEnd(Enumeration.class, "Chain");
    }

    @Test
    public void testNullFirstFails() {
        this.wrapFails(null, EnumerationChainTest.SECOND);
    }

    @Test
    public void testNullSecondFails() {
        this.wrapFails(EnumerationChainTest.FIRST, null);
    }

    private void wrapFails(final Enumeration<String> first, final Enumeration<String> second) {
        try {
            EnumerationChain.wrap(first, second);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapEqualFirstAndSecond() {
        assertSame(EnumerationChainTest.FIRST,
                EnumerationChain.wrap(EnumerationChainTest.FIRST, EnumerationChainTest.FIRST));
    }

    @Test
    public void testConsume() {
        final Vector<String> first = Lists.vector();
        first.add("1");
        first.add("2");
        final Vector<String> second = Lists.vector();
        second.add("3");
        second.add("4");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        Assert.assertTrue("hasMoreElements from 1st enumerator", enumerator.hasMoreElements());
        assertSame("next from 1st enumerator", "1", enumerator.nextElement());

        Assert.assertTrue("hasMoreElements from 1st enumerator", enumerator.hasMoreElements());
        assertSame("next from 1st enumerator", "2", enumerator.nextElement());

        Assert.assertTrue("hasMoreElements from last enumerator", enumerator.hasMoreElements());
        assertSame("next from last enumerator", "3", enumerator.nextElement());

        Assert.assertTrue("hasMoreElements from last enumerator", enumerator.hasMoreElements());
        assertSame("next from last enumerator", "4", enumerator.nextElement());

        Assert.assertFalse("hasMoreElements should be false when empty",
                enumerator.hasMoreElements());
        try {
            enumerator.nextElement();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testConsumeWithoutHasNext() {
        final Vector<String> first = Lists.vector();
        first.add("1");
        first.add("2");
        final Vector<String> second = Lists.vector();
        second.add("3");
        second.add("4");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        assertSame("next from 1st enumerator", "1", enumerator.nextElement());
        assertSame("next from 1st enumerator", "2", enumerator.nextElement());
        assertSame("next from last enumerator", "3", enumerator.nextElement());
        assertSame("next from last enumerator", "4", enumerator.nextElement());
    }

    @Test
    public void testNextWhenEmpty() {
        final Vector<String> first = Lists.vector();
        first.add("1");
        first.add("2");
        final Vector<String> second = Lists.vector();
        second.add("3");
        second.add("4");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        enumerator.nextElement();
        enumerator.nextElement();
        enumerator.nextElement();
        enumerator.nextElement();

        this.checkNextElementFails(enumerator);
    }

    @Test
    public void testConsumeEverythingFromEmptyEnumerations() {
        final Vector<String> first = Lists.vector();
        final Vector<String> second = Lists.vector();

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        Assert.assertFalse("hasMoreElements from 1st enumerator", enumerator.hasMoreElements());
        try {
            enumerator.nextElement();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testFirstEnumerationEmpty() {
        final Vector<String> first = Lists.vector();
        final Vector<String> second = Lists.vector();
        second.add("1");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        Assert.assertTrue("hasMoreElements from 2nd enumerator", enumerator.hasMoreElements());
        assertSame("next from 2nd enumerator", "1", enumerator.nextElement());

        Assert.assertFalse("hasMoreElements from empty 2nd enumerator",
                enumerator.hasMoreElements());
        this.checkNextElementFails(enumerator);
    }

    @Test
    public void testFirstEnumerationEmptyWithoutHasNext() {
        final Vector<String> first = Lists.vector();
        final Vector<String> second = Lists.vector();
        second.add("1");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        assertSame("next from 2nd enumerator", "1", enumerator.nextElement());

        Assert.assertFalse("hasMoreElements from empty 2nd enumerator",
                enumerator.hasMoreElements());
        this.checkNextElementFails(enumerator);
    }

    @Test
    public void testToString() {
        assertEquals(EnumerationChainTest.FIRST + "...",
                this.createEnumeration().toString());
    }

    @Test
    public void testToStringAfterConsumingFirstEnumeration() {
        final Vector<String> first = Lists.vector();
        first.add("1");
        final Vector<String> second = Lists.vector();
        second.add("2");
        second.add("3");

        final Enumeration<String> secondEnumeration = second.elements();
        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                secondEnumeration);
        enumerator.nextElement();
        enumerator.hasMoreElements();

        assertEquals(secondEnumeration.toString(), enumerator.toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final Vector<String> first = Lists.vector();
        final Vector<String> second = Lists.vector();
        second.add("1");

        final EnumerationChain<String> enumerator = this.createEnumeration(first.elements(),
                second.elements());
        enumerator.nextElement();
        enumerator.hasMoreElements();

        assertEquals("", enumerator.toString());
    }

    @Override
    protected EnumerationChain<String> createEnumeration() {
        return this.createEnumeration(EnumerationChainTest.FIRST, EnumerationChainTest.SECOND);
    }

    private EnumerationChain<String> createEnumeration(final Enumeration<String> first,
                                                       final Enumeration<String> second) {
        return Cast.to(EnumerationChain.wrap(first, second));
    }

    @Override
    protected Class<EnumerationChain<String>> type() {
        return Cast.to(EnumerationChain.class);
    }
}

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

package walkingkooka.compare;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class RangeEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<Range<Integer>> {

    private final static int VALUE = 123;

    // all.....................................................................................

    @Test
    public void testAllSingleton() {
        this.checkNotEquals(Range.singleton(VALUE));
    }

    @Test
    public void testAllLessThan() {
        this.checkNotEquals(Range.lessThan(VALUE));
    }

    @Test
    public void testAllLessThanEquals() {
        this.checkNotEquals(Range.lessThanEquals(VALUE));
    }

    @Test
    public void testAllGreaterThan() {
        this.checkNotEquals(Range.greaterThan(VALUE));
    }

    @Test
    public void testAllGreaterThanEquals() {
        this.checkNotEquals(Range.greaterThanEquals(VALUE));
    }

    // lessThan.....................................................................................

    @Test
    public void testLessThanAll() {
        this.checkNotEquals(lessThan(), Range.all());
    }

    @Test
    public void testLessThanSingleton() {
        this.checkNotEquals(lessThan(), Range.singleton(VALUE));
    }

    @Test
    public void testLessThanLessThan() {
        this.checkEquals(lessThan(), Range.lessThan(VALUE));
    }

    @Test
    public void testLessThanLessThanEquals() {
        this.checkNotEquals(lessThan(), Range.lessThanEquals(VALUE));
    }

    @Test
    public void testLessThanGreaterThan() {
        this.checkNotEquals(lessThan(), Range.greaterThan(VALUE));
    }

    @Test
    public void testLessThanGreaterThanEquals() {
        this.checkNotEquals(lessThan(), Range.greaterThanEquals(VALUE));
    }

    private Range<Integer> lessThan() {
        return Range.lessThan(VALUE);
    }

    // lessThan...........................................................................

    @Test
    public void testLessThanEqualsAll() {
        this.checkNotEquals(lessThanEquals(), Range.all());
    }

    @Test
    public void testLessThanEqualsSingleton() {
        this.checkNotEquals(lessThanEquals(), Range.singleton(VALUE));
    }

    @Test
    public void testLessThanEqualsLessThan() {
        this.checkNotEquals(lessThanEquals(), Range.lessThan(VALUE));
    }

    @Test
    public void testLessThanEqualsLessThanEquals() {
        this.checkEquals(lessThanEquals(), Range.lessThanEquals(VALUE));
    }

    @Test
    public void testLessThanEqualsGreaterThan() {
        this.checkNotEquals(lessThanEquals(), Range.greaterThan(VALUE));
    }

    @Test
    public void testLessThanEqualsGreaterThanEquals() {
        this.checkNotEquals(lessThanEquals(), Range.greaterThanEquals(VALUE));
    }

    private Range<Integer> lessThanEquals() {
        return Range.lessThanEquals(VALUE);
    }

    // factory ...........................................................................................

    @Override
    protected Range<Integer> createObject() {
        return Range.all();
    }
}

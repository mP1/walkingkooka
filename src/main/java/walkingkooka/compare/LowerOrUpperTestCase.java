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

import static org.junit.Assert.assertSame;

public abstract class LowerOrUpperTestCase<C extends LowerOrUpper<C>> extends ComparableTestCase<C> {

    @Test(expected = NullPointerException.class)
    public final void testLowerNullFails() {
        this.createComparable().lower(null);
    }

    @Test
    public final void testLowerSame() {
        final C object = this.createComparable();
        assertSame(object, object.lower(object));
    }

    @Test(expected = NullPointerException.class)
    public final void testUpperNullFails() {
        this.createComparable().upper(null);
    }

    @Test
    public final void testUpperSame() {
        final C object = this.createComparable();
        assertSame(object, object.lower(object));
    }

    protected final C createComparable() {
        return this.createLowerOrUpper();
    }

    abstract protected C createLowerOrUpper();
}

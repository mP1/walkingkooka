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

package walkingkooka.util;

import org.junit.Test;
import walkingkooka.test.PublicStaticHelperTestCase;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

final public class WaiterTest extends PublicStaticHelperTestCase<Waiter> {

    @Test
    public void testOneMillisecond() {
        final long before = System.currentTimeMillis();
        Waiter.waitAtLeast(0);
        final long after = System.currentTimeMillis();
        final long waited = after - before;
        assertTrue("Did not wait 1 milliseconds", waited > 1);
    }

    @Test
    public void testOneHundredMilliseconds() {
        final long before = System.currentTimeMillis();
        Waiter.waitAtLeast(100);
        final long after = System.currentTimeMillis();
        final long waited = after - before;
        assertTrue("Did not wait 100 milliseconds", waited > 100);
    }

    @Override
    protected Class<Waiter> type() {
        return Waiter.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}

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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

final public class WaiterTest implements ClassTesting2<Waiter>,
        PublicStaticHelperTesting<Waiter> {

    @Test
    public void testOneMillisecond() {
        final long before = System.currentTimeMillis();
        Waiter.waitAtLeast(0);
        final long after = System.currentTimeMillis();
        final long waited = after - before;
        assertTrue(waited > 1, "Did not wait 1 milliseconds");
    }

    @Test
    public void testOneHundredMilliseconds() {
        final long before = System.currentTimeMillis();
        Waiter.waitAtLeast(100);
        final long after = System.currentTimeMillis();
        final long waited = after - before;
        assertTrue(waited > 100, "Did not wait 100 milliseconds");
    }

    @Override
    public Class<Waiter> type() {
        return Waiter.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

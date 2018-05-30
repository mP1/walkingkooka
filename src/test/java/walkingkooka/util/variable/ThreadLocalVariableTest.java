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

package walkingkooka.util.variable;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.util.Waiter;

final public class ThreadLocalVariableTest extends VariableTestCase<ThreadLocalVariable<Object>, Object> {

    @Override
    @Test
    public void testWith() {
        final ThreadLocalVariable<Object> variable = ThreadLocalVariable.createWithThreadLocal();
        Assert.assertNull(variable.get());
    }

    @Test
    public void testManyThreads() throws InterruptedException {
        final IntegerVariable counter1 = IntegerVariable.create();
        final IntegerVariable counter2 = IntegerVariable.create();
        final ThreadLocalVariable<Integer> variable = ThreadLocalVariable.createWithThreadLocal();

        final T thread1 = new T(counter1, variable);
        final T thread2 = new T(counter2, variable);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Assert.assertEquals(10, counter1.value());
        Assert.assertEquals(10, counter2.value());
    }

    private static class T extends Thread {

        private T(final IntegerVariable integer, final ThreadLocalVariable<Integer> variable) {
            super();
            this.integer = integer;
            this.variable = variable;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                this.variable.set(i);
                Waiter.waitAtLeast(10);
                assertEquals(i, this.variable.get());
                this.integer.increment();
            }
        }

        private final IntegerVariable integer;
        private final ThreadLocalVariable<Integer> variable;
    }

    @Test
    public void testToString() {
        final Object value = "value";
        final ThreadLocalVariable<Object> variable = ThreadLocalVariable.createWithThreadLocal();
        variable.set(value);
        assertEquals(value, variable.toString());
    }

    @Override
    protected ThreadLocalVariable<Object> createVariable() {
        return ThreadLocalVariable.createWithThreadLocal();
    }

    @Override
    protected Object createValue() {
        return new Object();
    }
}

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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

final public class AtomicReferenceVariableTest extends VariableTestCase<AtomicReferenceVariable<Object>, Object> {

    @Override
    @Test
    public void testWith() {
        final AtomicReferenceVariable<Object> variable = AtomicReferenceVariable.create();
        assertNull(variable.get());
    }

    @Test
    public void testSetAndGet() {
        final Object value = "value";
        final AtomicReferenceVariable<Object> variable = AtomicReferenceVariable.create();
        variable.set(value);
        assertSame(value, variable.get());
    }

    @Test
    public void testToString() {
        final Object value = "value";
        final AtomicReferenceVariable<Object> variable = AtomicReferenceVariable.create();
        variable.set(value);
        assertEquals(value, variable.toString());
    }

    @Override
    protected AtomicReferenceVariable<Object> createVariable() {
        return AtomicReferenceVariable.create();
    }

    @Override
    protected Object createValue() {
        return new Object();
    }
}

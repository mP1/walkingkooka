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


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class SetOnceVariableTest extends VariableTestCase<SetOnceVariable<Object>, Object> {

    @Test
    public void testWrapNullVariableFails() {
        assertThrows(NullPointerException.class, () -> {
            SetOnceVariable.wrap(null);
        });
    }

    @Override
    @Test
    public void testWith() {
        final Object value = "value";
        assertEquals(value, SetOnceVariable.wrap(Variables.with(value)).get());
    }

    @Test
    public void testWrapSetOnce() {
        final SetOnceVariable<Object> variable = SetOnceVariable.wrap(Variables.fake());
        assertSame(variable, SetOnceVariable.wrap(variable));
    }

    @Override
    @Test
    public void testSet() {
        final Object value = "*value*";
        final Variable<Object> wrapped = Variables.with(null);
        final SetOnceVariable<Object> variable = SetOnceVariable.wrap(wrapped);
        variable.set(value);
        assertSame(value, wrapped.get());
    }

    @Test
    public void testReplace() {
        final Object value = "*value*";
        final Variable<Object> wrapped = Variables.with(value);
        final SetOnceVariable<Object> variable = SetOnceVariable.wrap(wrapped);
        variable.set(value);

        assertThrows(UnsupportedOperationException.class, () -> {
            variable.set("**lost**");
        });

        assertSame(value, wrapped.get());
    }

    @Override
    @Test
    public void testReplaceWithNull() {
        // nop
    }

    @Test
    public void testReplaceTwiceFails() {
        final Object value = "*value*";
        final Variable<Object> wrapped = Variables.with(value);
        final SetOnceVariable<Object> variable = SetOnceVariable.wrap(wrapped);
        variable.set(value);

        assertThrows(UnsupportedOperationException.class, () -> {
            variable.set("twice");
        });
        assertSame(value, wrapped.get());
    }

    @Test
    public void testToString() {
        final Object value = "value";
        assertEquals(value.toString(), SetOnceVariable.wrap(Variables.with(value)).toString());
    }

    @Override
    protected SetOnceVariable<Object> createVariable() {
        return SetOnceVariable.wrap(Variables.with(null));
    }

    @Override
    protected Object createValue() {
        return new Object();
    }
}

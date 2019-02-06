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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LabelledVariableTest extends VariableTestCase<LabelledVariable<Object>, Object> {

    private final static String NAME = "name";
    private final static String VALUE = "*VALUE*";
    private final static Variable<String> VARIABLE = Variables.fake();

    @Test
    public void testWrapNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            LabelledVariable.wrap(null, VARIABLE);
        });
    }

    @Test
    public void testWrapEmptyNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LabelledVariable.wrap("", VARIABLE);
        });
    }

    @Test
    public void testWrapWhitespaceOnlyNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LabelledVariable.wrap(" \t", VARIABLE);
        });
    }

    @Override
    @Test
    public void testWith() {
        final Object value = "value";
        assertEquals(value, LabelledVariable.wrap(NAME, Variables.with(value)).get());
    }

    @Test
    public void testWrapNamed() {
        final LabelledVariable<Object> variable = this.createVariable();
        assertNotSame(variable, LabelledVariable.wrap("different", variable));
        assertSame(VALUE, variable.get());
    }

    @Test
    public void testGet() {
        final LabelledVariable<Object> variable = this.createVariable();
        assertSame(VALUE, variable.get());
    }

    @Override
    @Test
    public void testSet() {
        final LabelledVariable<Object> variable = this.createVariable();
        final String updated = "updated";
        variable.set(updated);
        assertSame(updated, variable.get());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(LabelledVariable.wrap(NAME, VARIABLE),
                NAME + "=" + VARIABLE);
    }

    @Test
    public void testToStringOriginalNamedVariable() {
        this.toStringAndCheck(LabelledVariable.wrap(NAME,
                LabelledVariable.wrap("should not be present in wrapped toString", VARIABLE)),
                NAME + "=" + VARIABLE);
    }

    @Override
    protected LabelledVariable<Object> createVariable() {
        return LabelledVariable.wrap(NAME, Variables.with(VALUE));
    }

    @Override
    protected Object createValue() {
        return new Object();
    }
}

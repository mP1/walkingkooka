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

final public class LabelledVariableTest extends VariableTestCase<LabelledVariable<Object>, Object> {

    private final static String NAME = "name";
    private final static String VALUE = "*VALUE*";
    private final static Variable<String> VARIABLE = Variables.fake();

    @Test
    public void testWrapNullName() {
        this.wrapFails(null, LabelledVariableTest.VARIABLE);
    }

    @Test
    public void testWrapEmptyName() {
        this.wrapFails("", LabelledVariableTest.VARIABLE);
    }

    @Test
    public void testWrapWhitespaceOnlyName() {
        this.wrapFails("  \t", LabelledVariableTest.VARIABLE);
    }

    private void wrapFails(final String name, final Variable<String> variable) {
        try {
            LabelledVariable.wrap(name, variable);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Override
    @Test
    public void testWith() {
        final Object value = "value";
        assertEquals(value, LabelledVariable.wrap(LabelledVariableTest.NAME, Variables.with(value)).get());
    }

    @Test
    public void testWrapNamed() {
        final LabelledVariable<Object> variable = this.createVariable();
        Assert.assertNotSame(variable, LabelledVariable.wrap("different", variable));
        assertSame(LabelledVariableTest.VALUE, variable.get());
    }

    @Test
    public void testGet() {
        final LabelledVariable<Object> variable = this.createVariable();
        assertSame(LabelledVariableTest.VALUE, variable.get());
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
        Assert.assertEquals(LabelledVariableTest.NAME + "=" + LabelledVariableTest.VARIABLE,
                LabelledVariable.wrap(LabelledVariableTest.NAME, LabelledVariableTest.VARIABLE).toString());
    }

    @Test
    public void testToStringOriginalNamedVariable() {
        Assert.assertEquals(LabelledVariableTest.NAME + "=" + LabelledVariableTest.VARIABLE,
                LabelledVariable.wrap(LabelledVariableTest.NAME,
                        LabelledVariable.wrap("should not be present in wrapped toString", LabelledVariableTest.VARIABLE))
                        .toString());
    }

    @Override
    protected LabelledVariable<Object> createVariable() {
        return LabelledVariable.wrap(LabelledVariableTest.NAME, Variables.with(LabelledVariableTest.VALUE));
    }

    @Override
    protected Object createValue() {
        return new Object();
    }
}

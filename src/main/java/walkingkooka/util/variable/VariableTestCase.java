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
import walkingkooka.Cast;
import walkingkooka.test.PackagePrivateClassTestCase;

/**
 * Base class for testing a {@link Variable}.
 */
abstract public class VariableTestCase<V extends Variable<T>, T> extends PackagePrivateClassTestCase<V> {

    protected VariableTestCase() {
        super();
    }

    // tests

    @Test
    public void testNaming() {
        this.checkNaming(Variable.class);
    }

    @Test
    public void testWith() {
        Assert.assertNotNull(this.createVariable());
    }

    @Test
    public void testSet() {
        final V variable = this.createVariable();
        final T value = this.createValue();
        variable.set(value);
        assertSame(value, variable.get());
    }

    @Test
    public void testReplaceWithNull() {
        final V variable = this.createVariable();
        variable.set(this.createValue());
        variable.set(null);
        Assert.assertNull(variable.get());
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected V createVariable();

    @Override final protected Class<V> type() {
        return Cast.to(this.createVariable().getClass());
    }

    abstract protected T createValue();
}

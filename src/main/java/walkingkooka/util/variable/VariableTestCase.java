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
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Base class for testing a {@link Variable}.
 */
abstract public class VariableTestCase<V extends Variable<T>, T> extends ClassTestCase<V>
        implements ToStringTesting<V>,
        TypeNameTesting<V> {

    protected VariableTestCase() {
        super();
    }

    // tests

    @Test
    public void testWith() {
        assertNotNull(this.createVariable());
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
        assertNull(variable.get());
    }

    abstract protected V createVariable();

    @Override
    final public Class<V> type() {
        return Cast.to(this.createVariable().getClass());
    }

    abstract protected T createValue();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Variable.class.getSimpleName();
    }
}

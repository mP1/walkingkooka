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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class IntegerVariableTest extends ClassTestCase<IntegerVariable>
        implements ToStringTesting<IntegerVariable> {

    @Test
    public void testCreate() {
        final IntegerVariable variable = IntegerVariable.create();
        assertEquals(0, variable.value());
    }

    @Test
    public void testSet() {
        final IntegerVariable variable = IntegerVariable.create();
        variable.set(123);
        assertEquals(123, variable.value());
    }

    @Test
    public void testIncrement() {
        final IntegerVariable variable = IntegerVariable.create();
        variable.increment();
        variable.increment();
        variable.increment();
        assertEquals(3, variable.value());
    }

    @Test
    public void testAdd() {
        final IntegerVariable variable = IntegerVariable.create();
        variable.add(1);
        variable.add(2);
        variable.add(3);
        assertEquals(1 + 2 + 3, variable.value());
    }

    @Test
    public void testString() {
        final IntegerVariable variable = IntegerVariable.create();
        variable.set(123);
        assertEquals("123", variable.toString());
    }

    @Override
    public Class<IntegerVariable> type() {
        return IntegerVariable.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

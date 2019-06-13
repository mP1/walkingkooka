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
 *
 */

package walkingkooka.type;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class FieldAttributesTest implements ClassTesting2<FieldAttributes> {

    @Test
    public void testFinal() throws Exception {
        fieldAndCheck("finalField", FieldAttributes.FINAL);
    }

    private final Object finalField = null;

    @Test
    public void testNone() throws Exception {
        fieldAndCheck("noneField");
    }

    private Object noneField = null;

    @Test
    public void testStatic() throws Exception {
        fieldAndCheck("staticField", FieldAttributes.STATIC);
    }

    private static Object staticField = null;

    @Test
    public void testStaticTransientVolatile() throws Exception {
        fieldAndCheck("staticTransientVolatileField", FieldAttributes.STATIC, FieldAttributes.TRANSIENT, FieldAttributes.VOLATILE);
    }

    private static transient volatile Object staticTransientVolatileField = null;

    @Test
    public void testTransient() throws Exception {
        fieldAndCheck("transientField", FieldAttributes.TRANSIENT);
    }

    private transient Object transientField = null;

    @Test
    public void testVolatile() throws Exception {
        fieldAndCheck("volatileField", FieldAttributes.VOLATILE);
    }

    private volatile Object volatileField = null;

    private void fieldAndCheck(final String name, final FieldAttributes... attributes) throws Exception {
        final Field field = this.getClass().getDeclaredField(name);
        assertEquals(Sets.of(attributes), FieldAttributes.get(field));
    }

    @Override
    public Class<FieldAttributes> type() {
        return FieldAttributes.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.type;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TypesTest implements ClassTesting2<Types>,
        PublicStaticHelperTesting<Types> {

    @Test
    public void testIsPrimitiveOrWrapperBooleanPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Boolean.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperBooleanWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Boolean.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperBytePrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Byte.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperByteWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Byte.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperCharacterPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Character.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperCharacterWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Character.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperDoublePrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Double.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperDoubleWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Double.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperFloatPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Float.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperFloatWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Float.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperIntegerPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Integer.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperIntegerWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Integer.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperLongPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Long.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperLongWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Long.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperNumber() {
        assertTrue(Types.isPrimitiveOrWrapper(Number.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperShortPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Short.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperShortWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Short.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperVoidPrimitive() {
        assertTrue(Types.isPrimitiveOrWrapper(Void.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperVoidWrapper() {
        assertTrue(Types.isPrimitiveOrWrapper(Void.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperObject() {
        assertFalse(Types.isPrimitiveOrWrapper(Object.class));
    }

    @Override
    public Class<Types> type() {
        return Types.class;
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

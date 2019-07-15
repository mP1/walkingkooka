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
import walkingkooka.test.PublicStaticHelperTesting;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ClassesTest implements PublicStaticHelperTesting<Classes> {

    @Test
    public void testIsPrimitiveOrWrapperBooleanPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Boolean.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperBooleanWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Boolean.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperBytePrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Byte.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperByteWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Byte.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperCharacterPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Character.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperCharacterWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Character.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperDoublePrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Double.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperDoubleWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Double.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperFloatPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Float.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperFloatWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Float.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperIntegerPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Integer.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperIntegerWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Integer.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperLongPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Long.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperLongWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Long.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperNumber() {
        assertTrue(Classes.isPrimitiveOrWrapper(Number.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperShortPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Short.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperShortWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Short.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperVoidPrimitive() {
        assertTrue(Classes.isPrimitiveOrWrapper(Void.TYPE));
    }

    @Test
    public void testIsPrimitiveOrWrapperVoidWrapper() {
        assertTrue(Classes.isPrimitiveOrWrapper(Void.class));
    }

    @Test
    public void testIsPrimitiveOrWrapperObject() {
        assertFalse(Classes.isPrimitiveOrWrapper(Object.class));
    }

    @Override
    public Class<Classes> type() {
        return Classes.class;
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

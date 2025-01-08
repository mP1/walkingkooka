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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Optional;

public final class ClassesTest implements PublicStaticHelperTesting<Classes> {

    // primitive........................................................................................................

    @Test
    public void testPrimitiveBooleanType() {
        this.primitiveAndCheck(Boolean.TYPE, null);
    }

    @Test
    public void testPrimitiveBooleanWrapper() {
        this.primitiveAndCheck(Boolean.class, Boolean.TYPE);
    }

    @Test
    public void testPrimitiveByteType() {
        this.primitiveAndCheck(Byte.TYPE, null);
    }

    @Test
    public void testPrimitiveByteWrapper() {
        this.primitiveAndCheck(Byte.class, Byte.TYPE);
    }

    @Test
    public void testPrimitiveCharacterType() {
        this.primitiveAndCheck(Character.TYPE, null);
    }

    @Test
    public void testPrimitiveCharacterWrapper() {
        this.primitiveAndCheck(Character.class, Character.TYPE);
    }

    @Test
    public void testPrimitiveDoubleType() {
        this.primitiveAndCheck(Double.TYPE, null);
    }

    @Test
    public void testPrimitiveDoubleWrapper() {
        this.primitiveAndCheck(Double.class, Double.TYPE);
    }

    @Test
    public void testPrimitiveFloatType() {
        this.primitiveAndCheck(Float.TYPE, null);
    }

    @Test
    public void testPrimitiveFloatWrapper() {
        this.primitiveAndCheck(Float.class, Float.TYPE);
    }

    @Test
    public void testPrimitiveIntegerType() {
        this.primitiveAndCheck(Integer.TYPE, null);
    }

    @Test
    public void testPrimitiveIntegerWrapper() {
        this.primitiveAndCheck(Integer.class, Integer.TYPE);
    }

    @Test
    public void testPrimitiveLongType() {
        this.primitiveAndCheck(Long.TYPE, null);
    }

    @Test
    public void testPrimitiveLongWrapper() {
        this.primitiveAndCheck(Long.class, Long.TYPE);
    }

    @Test
    public void testPrimitiveObject() {
        this.primitiveAndCheck(Object.class, null);
    }

    @Test
    public void testPrimitiveObject2() {
        this.primitiveAndCheck(this.getClass(), null);
    }

    @Test
    public void testPrimitiveShortType() {
        this.primitiveAndCheck(Short.TYPE, null);
    }

    @Test
    public void testPrimitiveShortWrapper() {
        this.primitiveAndCheck(Short.class, Short.TYPE);
    }

    private void primitiveAndCheck(final Class<?> classs,
                                   final Class<?> expected) {
        this.checkEquals(Optional.ofNullable(expected),
            Classes.primitive(classs),
            classs::getName);
    }

    // primitive........................................................................................................

    @Test
    public void testWrapperBooleanType() {
        this.wrapperAndCheck(Boolean.TYPE, Boolean.class);
    }

    @Test
    public void testWrapperBooleanWrapper() {
        this.wrapperAndCheck(Boolean.class, null);
    }

    @Test
    public void testWrapperByteType() {
        this.wrapperAndCheck(Byte.TYPE, Byte.class);
    }

    @Test
    public void testWrapperByteWrapper() {
        this.wrapperAndCheck(Byte.class, null);
    }

    @Test
    public void testWrapperCharacterType() {
        this.wrapperAndCheck(Character.TYPE, Character.class);
    }

    @Test
    public void testWrapperCharacterWrapper() {
        this.wrapperAndCheck(Character.class, null);
    }

    @Test
    public void testWrapperDoubleType() {
        this.wrapperAndCheck(Double.TYPE, Double.class);
    }

    @Test
    public void testWrapperDoubleWrapper() {
        this.wrapperAndCheck(Double.class, null);
    }

    @Test
    public void testWrapperFloatType() {
        this.wrapperAndCheck(Float.TYPE, Float.class);
    }

    @Test
    public void testWrapperFloatWrapper() {
        this.wrapperAndCheck(Float.class, null);
    }

    @Test
    public void testWrapperIntegerType() {
        this.wrapperAndCheck(Integer.TYPE, Integer.class);
    }

    @Test
    public void testWrapperIntegerWrapper() {
        this.wrapperAndCheck(Integer.class, null);
    }

    @Test
    public void testWrapperLongType() {
        this.wrapperAndCheck(Long.TYPE, Long.class);
    }

    @Test
    public void testWrapperLongWrapper() {
        this.wrapperAndCheck(Long.class, null);
    }

    @Test
    public void testWrapperObject() {
        this.wrapperAndCheck(Object.class, null);
    }

    @Test
    public void testWrapperObject2() {
        this.wrapperAndCheck(this.getClass(), null);
    }

    @Test
    public void testWrapperShortType() {
        this.wrapperAndCheck(Short.TYPE, Short.class);
    }

    @Test
    public void testWrapperShortWrapper() {
        this.wrapperAndCheck(Short.class, null);
    }

    private void wrapperAndCheck(final Class<?> classs,
                                 final Class<?> expected) {

        this.checkEquals(Optional.ofNullable(expected),
            Classes.wrapper(classs),
            classs::getName);
    }

    // ClassTesting.....................................................................................................

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

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

package walkingkooka.tree.pojo;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public final class ReflectionPojoNodeContextTest implements ClassTesting2<ReflectionPojoNodeContext>,
        PojoNodeContextTesting<ReflectionPojoNodeContext> {

    private final static PojoName X = PojoName.property("x");
    private final static String STRING = "abc";
    private final static String STRING2 = "xyz";
    private final static boolean BOOLEAN = true;
    private final static boolean BOOLEAN2 = false;

    // Object

    @Test
    public void testPropertiesGetter() {
        this.propertiesAndCheck(TestGetter.class, "x");
    }

    @Test
    public void testPropertiesGetGetter() {
        this.propertiesAndCheck(TestGetGetter.class, "x");
    }

    @Test
    public void testPropertiesGetterAndSetter() {
        this.propertiesAndCheck(TestGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesGetterAndSetter2() {
        this.propertiesAndCheck(TestGetterAndSetter2.class, "x");
    }

    // Boolean

    @Test
    public void testPropertiesBooleanPrimitiveGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanPrimitiveGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesBooleanPrimitiveGetGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanPrimitiveGetGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesBooleanPrimitiveIsGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanPrimitiveIsGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesBooleanWrapperGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanWrapperGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesBooleanWrapperGetGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanWrapperGetGetterAndSetter.class, "x");
    }

    @Test
    public void testPropertiesBooleanWrapperIsGetterAndSetter() {
        this.propertiesAndCheck(TestBooleanWrapperIsGetterAndSetter.class, "x");
    }

    // Get

    @Test
    public void testGetter() {
        this.getAndCheck(new TestGetter(), X, STRING);
    }

    @Test
    public void testGetGetter() {
        this.getAndCheck(new TestGetGetter(), X, STRING);
    }

    @Test
    public void testGetterSet() {
        this.setAndCheck(new TestGetterAndSetter(), X, STRING2);
    }

    @Test
    public void testGetterAndSetter() {
        this.setAndGetCheck(new TestGetterAndSetter(), X, STRING2);
    }

    @Test
    public void testGetterAndSetter2() {
        final TestGetterAndSetter2 instance = new TestGetterAndSetter2();
        final TestGetterAndSetter2 result = this.setAndGetCheck(instance, X, STRING2);
        assertNotSame(instance, result);
        assertEquals(STRING, instance.x, "original property was changed");
    }

    // Get BooleanPrimitive

    @Test
    public void testBooleanPrimitiveGetter() {
        this.getAndCheck(new TestBooleanPrimitiveGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanPrimitiveGetGetter() {
        this.getAndCheck(new TestBooleanPrimitiveGetGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanPrimitiveIsGetter() {
        this.setAndCheck(new TestBooleanPrimitiveIsGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanPrimitiveGetterSetter() {
        this.setAndCheck(new TestBooleanPrimitiveGetterAndSetter(), X, BOOLEAN2);
    }

    @Test
    public void testBooleanPrimitiveGetGetterSetter() {
        this.setAndCheck(new TestBooleanPrimitiveGetGetterAndSetter(), X, BOOLEAN2);
    }

    @Test
    public void testBooleanPrimitiveIsGetterSetter() {
        this.setAndCheck(new TestBooleanPrimitiveIsGetterAndSetter(), X, BOOLEAN2);
    }

    // Get BooleanWrapper

    @Test
    public void testBooleanWrapperGetter() {
        this.getAndCheck(new TestBooleanWrapperGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanWrapperGetGetter() {
        this.getAndCheck(new TestBooleanWrapperGetGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanWrapperIsGetter() {
        this.setAndCheck(new TestBooleanWrapperIsGetterAndSetter(), X, BOOLEAN);
    }

    @Test
    public void testBooleanWrapperGetterSetter() {
        this.setAndCheck(new TestBooleanWrapperGetterAndSetter(), X, BOOLEAN2);
    }

    @Test
    public void testBooleanWrapperGetGetterSetter() {
        this.setAndCheck(new TestBooleanWrapperGetGetterAndSetter(), X, BOOLEAN2);
    }

    @Test
    public void testBooleanWrapperIsGetterSetter() {
        this.setAndCheck(new TestBooleanWrapperIsGetterAndSetter(), X, BOOLEAN2);
    }

    // createList...........................................................................................

    @Test
    public void testCreateListArrayList() {
        this.createListAndCheck(Lists.array());
    }

    @Test
    public void testCreateListLinkedList() {
        this.createListAndCheck(Lists.linkedList());
    }

    @Test
    public void testCreateListUnmodifiableListDefaultsToArrayList() {
        final List<Object> list = Lists.array();
        this.createListAndCheck(Lists.readOnly(list), Lists.array().getClass());
    }

    private void createListAndCheck(final List<Object> list) {
        this.createListAndCheck(list,
                list.getClass());
    }

    private void createListAndCheck(final List<Object> list, final Class<?> expected) {
        this.createAndCheck(list,
                (c) -> this.createContext().createList(c),
                expected);
    }

    // createSet .........................................................................................

    @Test
    public void testCreateSetArraySet() {
        this.createSetAndCheck(Sets.hash());
    }

    @Test
    public void testCreateSetLinkedSet() {
        this.createSetAndCheck(Sets.ordered());
    }

    @Test
    public void testCreateSetUnmodifiableSetDefaultsToArraySet() {
        final Set<Object> set = Sets.hash();
        this.createSetAndCheck(Sets.readOnly(set), Sets.ordered().getClass());
    }

    private void createSetAndCheck(final Set<Object> set) {
        this.createSetAndCheck(set,
                set.getClass());
    }

    private void createSetAndCheck(final Set<Object> set, final Class<?> expected) {
        this.createAndCheck(set,
                (c) -> this.createContext().createSet(c),
                expected);
    }

    // createMap...........................................................................................

    @Test
    public void testCreateMapArrayMap() {
        this.createMapAndCheck(Maps.hash());
    }

    @Test
    public void testCreateMapLinkedMap() {
        this.createMapAndCheck(Maps.ordered());
    }

    @Test
    public void testCreateMapUnmodifiableMapDefaultsToArrayMap() {
        final Map<Object, Object> map = Maps.ordered();
        this.createMapAndCheck(Maps.readOnly(map), Maps.ordered().getClass());
    }

    private void createMapAndCheck(final Map<Object, Object> map) {
        this.createMapAndCheck(map,
                map.getClass());
    }

    private void createMapAndCheck(final Map<Object, Object> map, final Class<?> expected) {
        this.createAndCheck(map,
                (c) -> this.createContext().createMap(c),
                expected);
    }

    private <T> void createAndCheck(final T collection, final Function<Class, T> creator, final Class<?> expected) {
        assertEquals(expected, creator.apply(collection.getClass()).getClass());
    }

    // helpers......................................................................................

    @Override
    public ReflectionPojoNodeContext createContext() {
        return new ReflectionPojoNodeContext();
    }

    @Override
    public Class<ReflectionPojoNodeContext> type() {
        return ReflectionPojoNodeContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    static class TestGetter {

        final String x = STRING;

        public String x() {
            return x;
        }
    }

    static class TestGetGetter {

        final String x = STRING;

        public String getX() {
            return x;
        }
    }

    static class TestSetGetter {

        String x = STRING;

        public void setX(final String x) {
            this.x = x;
        }
    }

    static class TestGetterAndSetter {

        String x = STRING;

        public String getX() {
            return x;
        }

        public void setX(final String x) {
            this.x = x;
        }
    }

    static class TestGetterAndSetter2 {

        String x = STRING;

        public String getX() {
            return x;
        }

        public TestGetterAndSetter2 setX(final String x) {
            final TestGetterAndSetter2 t = new TestGetterAndSetter2();
            t.x = x;
            return t;
        }
    }

    static class TestBooleanPrimitiveGetterAndSetter {

        boolean x = BOOLEAN;

        public boolean x() {
            return x;
        }

        public void setX(final boolean x) {
            this.x = x;
        }
    }

    static class TestBooleanPrimitiveGetGetterAndSetter {

        boolean x = BOOLEAN;

        public boolean getX() {
            return x;
        }

        public void setX(final boolean x) {
            this.x = x;
        }
    }

    static class TestBooleanPrimitiveIsGetterAndSetter {

        boolean x = BOOLEAN;

        public boolean isX() {
            return x;
        }

        public void setX(final boolean x) {
            this.x = x;
        }
    }

    static class TestBooleanWrapperGetterAndSetter {

        Boolean x = BOOLEAN;

        public Boolean x() {
            return x;
        }

        public void setX(final Boolean x) {
            this.x = x;
        }
    }

    static class TestBooleanWrapperGetGetterAndSetter {

        Boolean x = BOOLEAN;

        public Boolean getX() {
            return x;
        }

        public void setX(final Boolean x) {
            this.x = x;
        }
    }

    static class TestBooleanWrapperIsGetterAndSetter {

        Boolean x = BOOLEAN;

        public Boolean isX() {
            return x;
        }

        public void setX(final Boolean x) {
            this.x = x;
        }
    }
}

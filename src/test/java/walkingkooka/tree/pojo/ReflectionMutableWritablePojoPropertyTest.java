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
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReflectionMutableWritablePojoPropertyTest implements ClassTesting2<ReflectionMutableWritablePojoProperty>,
        PojoPropertyTesting<ReflectionMutableWritablePojoProperty> {

    private final static PojoName X = PojoName.property("x");
    private final static String STRING = "abc1";
    private final static String STRING2 = "xyz2";

    @Test
    public void testGet() {
        assertEquals(STRING, this.createPojoProperty().get(new TestBean()));
    }

    @Test
    public void testSetSame() {
        final TestBean instance = new TestBean();
        assertEquals(null, this.createPojoProperty().set(instance, STRING), "setter didnt return null");
    }

    @Test
    public void testSetDifferent() {
        final TestBean instance = new TestBean();
        assertEquals(null, this.createPojoProperty().set(instance, STRING2), "setter didnt return null");
        assertEquals(STRING2, instance.x);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPojoProperty(), "x");
    }

    @Override
    public ReflectionMutableWritablePojoProperty createPojoProperty() {
        try {
            return new ReflectionMutableWritablePojoProperty(X,
                    TestBean.class.getMethod("getX"),
                    TestBean.class.getMethod("setX", String.class));
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    @Override
    public Class<ReflectionMutableWritablePojoProperty> type() {
        return ReflectionMutableWritablePojoProperty.class;
    }

    static class TestBean {
        String x = STRING;

        public String getX() {
            return this.x;
        }

        public void setX(final String x) {
            this.x = x;
        }

        public String toString() {
            return "=" + STRING;
        }
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}

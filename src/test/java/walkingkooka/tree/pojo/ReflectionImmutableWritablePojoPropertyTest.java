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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReflectionImmutableWritablePojoPropertyTest implements ClassTesting2<ReflectionImmutableWritablePojoProperty>,
        PojoPropertyTesting<ReflectionImmutableWritablePojoProperty> {

    private final static PojoName X = PojoName.property("x");
    private final static String STRING = "abc1";
    private final static String STRING2 = "xyz2";

    @Test
    public void testGet() {
        this.getAndCheck(new TestBean(STRING), STRING);
    }

    @Test
    public void testSetSame() {
        final TestBean instance = new TestBean(STRING);
        assertSame(instance, this.setAndCheck(instance, STRING));
    }

    @Test
    public void testSetDifferent() {
        final TestBean instance = new TestBean(STRING);
        final TestBean result = (TestBean) this.setAndCheck(instance, STRING2);
        assertNotSame(instance, result);
        assertEquals(STRING2, result.x);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPojoProperty(), "x");
    }

    @Override
    public ReflectionImmutableWritablePojoProperty createPojoProperty() {
        try {
            return new ReflectionImmutableWritablePojoProperty(X,
                    TestBean.class.getMethod("getX"),
                    TestBean.class.getMethod("setX", String.class));
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    @Override
    public Class<ReflectionImmutableWritablePojoProperty> type() {
        return ReflectionImmutableWritablePojoProperty.class;
    }

    static class TestBean {
        final String x;

        TestBean(final String x) {
            this.x = x;
        }

        public String getX() {
            return this.x;
        }

        public TestBean setX(final String x) {
            return this.x.equals(x) ? this : new TestBean(x);
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

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
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReflectionReadOnlyPojoPropertyTest implements ClassTesting2<ReflectionReadOnlyPojoProperty>,
        PojoPropertyTesting<ReflectionReadOnlyPojoProperty> {

    private final static PojoName X = PojoName.property("x");
    private final static String STRING = "abc1";

    @Test
    public void testGet() {
        this.getAndCheck(new TestBean(), STRING);
    }

    @Test
    public void testIsReadOnly() {
        assertEquals(true, this.createPojoProperty().isReadOnly());
    }

    @Test
    public void testSetSame() {
        this.createPojoProperty().set(new TestBean(), STRING);
    }

    @Test
    public void testSetDifferent() {
        assertThrows(ReflectionPojoException.class, () -> {
            this.createPojoProperty().set(new TestBean(), "different");
        });
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPojoProperty(), "x");
    }

    @Override
    public ReflectionReadOnlyPojoProperty createPojoProperty() {
        try {
            return new ReflectionReadOnlyPojoProperty(X, TestBean.class.getMethod("getX"));
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    @Override
    public Class<ReflectionReadOnlyPojoProperty> type() {
        return ReflectionReadOnlyPojoProperty.class;
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

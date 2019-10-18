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

package walkingkooka.util.systemproperty;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;
import walkingkooka.test.ToStringTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class SystemPropertyTest implements ClassTesting2<SystemProperty>,
        ConstantsTesting<SystemProperty>,
        HashCodeEqualsDefinedTesting2<SystemProperty>,
        ToStringTesting<SystemProperty> {

    @Test
    public void testGetNullFails() {
        assertThrows(NullPointerException.class, () -> SystemProperty.get(null));
    }

    @Test
    public void testGetEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> SystemProperty.get(""));
    }

    @Test
    public void testGet() {
        final String name = "name";
        final SystemProperty property = SystemProperty.get(name);
        assertEquals("name", property.value());
    }

    @Test
    public void testKnownProperty() {
        final SystemProperty name = SystemProperty.FILE_ENCODING;
        assertSame(name, SystemProperty.get(name.value()));
    }

    @Test
    public void testValue() {
        assertNotNull(SystemProperty.FILE_SEPARATOR.propertyValue());
    }

    @Test
    public void testUnknown() {
        assertNull(SystemProperty.get(this.getClass().getName() + ".unknown")
                .propertyValue());
    }

    @Test
    public void testRequiredValue() {
        final SystemProperty property = SystemProperty.FILE_SEPARATOR;
        assertEquals(property.propertyValue(), property.requiredPropertyValue(), "value");
    }

    @Test
    public void testMissingRequiredValueFails() {
        final SystemProperty name = SystemProperty.get(this.getClass().getName());
        name.clear();
        assertThrows(MissingSystemPropertyException.class, name::requiredPropertyValue);
    }

    @Test
    public void testSet() {
        final String name = this.getClass().getName() + ".testSet";
        final SystemProperty property = SystemProperty.get(name);
        final String value = "123";

        try {
            property.set(value);
            assertEquals(value, property.propertyValue());
        } catch (final SecurityException ignore) {
        }
    }

    @Test
    public void testClear() {
        final String name = this.getClass().getName() + ".testClear";
        final SystemProperty property = SystemProperty.get(name);
        final String value = "123";

        try {
            property.set(value);
            property.clear();
            assertNull(property.propertyValue());
        } catch (final SecurityException ignore) {
        }
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(SystemProperty.FILE_ENCODING_PKG);
    }

    @Test
    public void testToString() {
        final String name = "name";
        this.toStringAndCheck(SystemProperty.get(name), name);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SystemProperty> type() {
        return SystemProperty.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting2.....................................................................................

    @Override
    public SystemProperty createObject() {
        return SystemProperty.FILE_SYSTEM_CASE_SENSITIVITY;
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Set<SystemProperty> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}

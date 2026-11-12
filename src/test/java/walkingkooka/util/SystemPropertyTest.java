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

package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.PublicClassTesting;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class SystemPropertyTest implements PublicClassTesting<SystemProperty>,
    ConstantsTesting<SystemProperty>,
    HashCodeEqualsDefinedTesting2<SystemProperty>,
    HasValueTesting,
    ToStringTesting {

    @Test
    public void testGetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SystemProperty.get(null)
        );
    }

    @Test
    public void testGetWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SystemProperty.get("")
        );
    }

    @Test
    public void testGet() {
        final String name = "name";
        final SystemProperty property = SystemProperty.get(name);

        this.valueAndCheck(
            property,
            "name"
        );
    }

    @Test
    public void testKnownProperty() {
        final SystemProperty name = SystemProperty.FILE_ENCODING;
        assertSame(
            name,
            SystemProperty.get(name.value())
        );
    }

    @Test
    public void testValue() {
        this.checkNotEquals(
            null,
            SystemProperty.FILE_SEPARATOR.propertyValue()
        );
    }

    @Test
    public void testUnknown() {
        this.propertyValueAndCheck(
            SystemProperty.get(this.getClass().getName() + ".unknown")

        );
    }

    @Test
    public void testRequiredValue() {
        final SystemProperty property = SystemProperty.FILE_SEPARATOR;
        this.checkEquals(
            property.propertyValue()
                .orElse("FAIL!"),
            property.requiredPropertyValue(),
            () -> "value of " + property
        );
    }

    @Test
    public void testMissingRequiredValueFails() {
        final SystemProperty name = SystemProperty.get(this.getClass().getName());
        name.clear();

        assertThrows(
            MissingSystemPropertyException.class,
            name::requiredPropertyValue
        );
    }

    @Test
    public void testSet() {
        final String name = this.getClass().getName() + ".testSet";
        final SystemProperty property = SystemProperty.get(name);
        final String value = "123";

        try {
            property.set(value);
            this.propertyValueAndCheck(
                property,
                value
            );
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
            this.propertyValueAndCheck(
                property
            );
        } catch (final SecurityException ignore) {
        }
    }

    private void propertyValueAndCheck(final SystemProperty systemProperty) {
        this.propertyValueAndCheck(
            systemProperty,
            Optional.empty()
        );
    }

    private void propertyValueAndCheck(final SystemProperty systemProperty,
                                       final String expected) {
        this.propertyValueAndCheck(
            systemProperty,
            Optional.of(expected)
        );
    }

    private void propertyValueAndCheck(final SystemProperty systemProperty,
                                       final Optional<String> expected) {
        this.checkEquals(
            expected,
            systemProperty.propertyValue()
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(SystemProperty.FILE_ENCODING_PKG);
    }

    @Override
    public SystemProperty createObject() {
        return SystemProperty.FILE_SYSTEM_CASE_SENSITIVITY;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final String name = "name";
        this.toStringAndCheck(SystemProperty.get(name), name);
    }

    // Class............................................................................................................

    @Override
    public Class<SystemProperty> type() {
        return SystemProperty.class;
    }


    // ConstantsTesting.................................................................................................

    @Override
    public Set<SystemProperty> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}

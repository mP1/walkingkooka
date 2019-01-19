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
 */

package walkingkooka.util.systemproperty;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

final public class SystemPropertyTest extends ClassTestCase<SystemProperty>
        implements ConstantsTesting<SystemProperty>, HashCodeEqualsDefinedTesting<SystemProperty> {

    @Test
    public void testGetNullFails() {
        this.getFails(null);
    }

    @Test
    public void testGetEmptyFails() {
        this.getFails("");
    }

    private void getFails(final String name) {
        try {
            SystemProperty.get(name);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testGet() {
        final String name = "name";
        final SystemProperty property = SystemProperty.get(name);
        assertEquals("name", property.value());
    }

    @Test
    public void testGetCached() {
        final String name = "name";
        assertSame(SystemProperty.get(name), SystemProperty.get(name));
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
        assertEquals("value", property.propertyValue(), property.requiredPropertyValue());
    }

    @Test
    public void testMissingRequiredValue() {
        final SystemProperty name = SystemProperty.get(this.getClass().getName());
        name.clear();
        try {
            name.requiredPropertyValue();
        } catch (final MissingSystemPropertyException expected) {
        }
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
        assertEquals(name, SystemProperty.get(name).toString());
    }

    @Test
    public void testToStringNeedsQuotes() {
        final String name = "needs quotes";
        assertEquals(CharSequences.quoteIfNecessary(name).toString(),
                SystemProperty.get(name).toString());
    }

    @Override
    public Class<SystemProperty> type() {
        return SystemProperty.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public SystemProperty createObject() {
        return SystemProperty.FILE_SYSTEM_CASE_SENSITIVITY;
    }

    @Override
    public Set<SystemProperty> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}

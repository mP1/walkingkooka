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
import walkingkooka.test.Fake;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A testing mixin that tests that a type has the required prefix and suffix. One standard includes the addition
 * of the implementing interface as a required suffix.
 */
public interface TypeNameTesting<T> extends Testing {

    /**
     * Verifies that this type has been named and includes the required prefix and suffix.
     */
    @Test
    default void testTypeNaming() {
        final Class<T> type = this.type();

        // Allow some FakeXXX classes dropping the Fake...this allows the Fakes to be used in some tests.
        final String name = type.getSimpleName();

        final String prefix = this.typeNamePrefix();
        final String suffix = this.typeNameSuffix();

        if (prefix.isEmpty() && suffix.isEmpty()) {
            fail("Both prefix and suffix are empty");
        }

        if (!typeNamePrefix().isEmpty() &&
            false == name.startsWith(Fake.class.getSimpleName() + prefix) &&
            false == name.startsWith(prefix)) {
            fail("type name " + CharSequences.quote(name) + " missing required prefix " + CharSequences.quote(prefix));
        }

        if (!typeNameSuffix().isEmpty() &&
            false == name.endsWith(Fake.class.getSimpleName() + suffix) &&
            false == name.endsWith(suffix)) {
            fail("type name " + CharSequences.quote(name) + " missing required suffix " + CharSequences.quote(suffix));
        }
    }

    /**
     * Returns the text after substracting the {@link #typeNamePrefix()} from the reflect.
     */
    default String subtractTypeNamePrefix() {
        final String name = this.type().getSimpleName();
        final String prefix = this.typeNamePrefix();
        if (!name.startsWith(prefix)) {
            fail("Type name " + CharSequences.quote(name) + " doesnt start with prefix " + CharSequences.quote(prefix));
        }

        return name.substring(prefix.length());
    }

    /**
     * The require prefix for this type or empty string.
     */
    String typeNamePrefix();

    /**
     * Returns the text after substracting the {@link #typeNameSuffix()} from the reflect.
     */
    default String subtractTypeNameSuffix() {
        final String name = this.type().getSimpleName();
        final String suffix = this.typeNameSuffix();
        if (!name.endsWith(suffix)) {
            fail("Type name " + CharSequences.quote(name) + " doesnt end with suffix " + CharSequences.quote(suffix));
        }

        return name.substring(0, name.length() - suffix.length());
    }

    /**
     * The require suffix for this type or empty string.
     */
    String typeNameSuffix();

    /**
     * The type being tested.
     */
    Class<T> type();
}

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.Testing;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base class for testing public constants declared in a class.
 */
public interface ConstantsTesting<T> extends Testing {

    /**
     * Asserts that a field is public static and final.
     */
    default void fieldPublicStaticCheck(final Class<?> enclosingType,
                                        final String name,
                                        final Class<?> fieldType) {
        Field field = null;
        try {
            field = enclosingType.getDeclaredField(name);
        } catch (final Exception cause) {
            Assertions.fail("Cannot find public constant field of type " + enclosingType + " called "
                + name);
        }

        final Field field2 = field;
        this.checkEquals(fieldType, field.getType(), "The field " + name + " is wrong the reflect");
        this.checkEquals(
            true,
            FieldAttributes.STATIC.is(field),
            () -> "The field " + name + " must be static =" + field2
        );
        assertSame(JavaVisibility.PUBLIC, JavaVisibility.of(field), () -> "The field " + name + " must be public =" + field2);
        this.checkEquals(
            true,
            FieldAttributes.FINAL.is(field),
            () -> "The field " + name + " must be final=" + field2
        );
    }

    /**
     * Scans for public static fields and verifies that all are unique and not a duplicate of
     * another.
     */
    @Test
    default void testConstantsAreUnique() throws Exception {
        final Set<Object> unique = Sets.hash();
        final Set<T> duplicateExceptionsSet = this.intentionalDuplicateConstants();

        final Class<T> type = this.type();
        for (final Field constant : type.getDeclaredFields()) {
            if (false == constant.getType().equals(type)) {
                continue;
            }
            if (false == FieldAttributes.STATIC.is(constant)) {
                continue;
            }
            if (false == FieldAttributes.FINAL.is(constant)) {
                continue;
            }
            assertSame(JavaVisibility.PUBLIC,
                JavaVisibility.of(constant),
                () -> "Constant must be public " + constant.toGenericString());
            final T value = Cast.to(constant.get(null));

            // intentional duplicate ignore...
            if (duplicateExceptionsSet.contains(value)) {
                continue;
            }

            if (false == unique.add(value)) {
                fail("Duplicate constant=" + value);
            }
        }
    }

    Class<T> type();

    Set<T> intentionalDuplicateConstants();
}

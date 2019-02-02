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
 *
 */

package walkingkooka.test;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base class for testing public constants declared in a class.
 */
public interface ConstantsTesting<T> extends Testing {

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
            assertSame(MemberVisibility.PUBLIC,
                    MemberVisibility.get(constant),
                    "Constant must be public");
            if (false == FieldAttributes.STATIC.is(constant)) {
                fail("Constant is not static=" + constant.getName());
            }
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

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
import walkingkooka.collect.list.Lists;
import walkingkooka.type.ClassAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Mixin with additional tests for {@link ClassTesting}
 */
public interface ClassTesting2<T> extends ClassTesting<T> {

    @Test
    default void testIfClassIsFinalIfAllConstructorsArePrivate() {
        final Class<T> type = this.type();
        if (!Fake.class.isAssignableFrom(type)) {
            if (!ClassAttributes.ABSTRACT.is(type)) {
                boolean mustBeFinal = true;
                for (final Constructor<?> constructor : type.getDeclaredConstructors()) {
                    if (false == MemberVisibility.PRIVATE.is(constructor)) {
                        mustBeFinal = false;
                        break;
                    }
                }

                if (mustBeFinal) {
                    if (false == ClassAttributes.FINAL.is(type)) {
                        fail("All constructors are private so class should be final="
                                + type.getName());
                    }
                }
            }
        }
    }

    /**
     * Constructor is private if this class is final, otherwise they are package private.
     */
    @Test
    default void testAllConstructorsVisibility() {
        final Class<T> type = this.type();

        final MemberVisibility visibility = Fake.class.isAssignableFrom(type) ?
                MemberVisibility.PUBLIC :
                ClassAttributes.FINAL.is(type) ?
                        MemberVisibility.PRIVATE :
                        MemberVisibility.PACKAGE_PRIVATE;
        assertEquals(Lists.empty(),
                Arrays.stream(this.type().getConstructors())
                .filter(c -> false == visibility.is(c))
                .collect(Collectors.toList()),
                () -> "Found several constructors that are not " + visibility + " for type " + type.getName());
    }
}

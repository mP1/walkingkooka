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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Mixin with additional constructor related tests for {@link ClassTesting}
 */
public interface ClassTesting2<T> extends ClassTesting<T> {

    @Test
    default void testIfClassIsFinalIfAllConstructorsArePrivate() {
        final Class<T> type = this.type();
        if (!Fake.class.isAssignableFrom(type)) {
            if (!ClassAttributes.ABSTRACT.is(type) && ClassAttributes.FINAL.is(type)) {
                this.checkEquals("",
                    Arrays.stream(type.getDeclaredConstructors())
                        .filter(c -> JavaVisibility.PRIVATE != JavaVisibility.of(c))
                        .map(Constructor::toGenericString)
                        .collect(Collectors.joining(",")),
                    () -> "All ctors must be private when class " + type.getName() + " is not abstract");
            }
        }
    }

    /**
     * Constructor is private if this class is final, otherwise they are package private.
     */
    @Test
    default void testAllConstructorsVisibility() {
        final Class<T> type = this.type();

        final JavaVisibility sameOrLess = Fake.class.isAssignableFrom(type) ?
            JavaVisibility.PUBLIC :
            ClassAttributes.FINAL.is(type) ?
                JavaVisibility.PRIVATE :
                JavaVisibility.PACKAGE_PRIVATE;
        this.checkEquals(
            "",
            Arrays.stream(this.type().getDeclaredConstructors())
                .filter(c -> false == JavaVisibility.of(c).isOrLess(sameOrLess))
                .map(Constructor::toGenericString)
                .collect(Collectors.joining(", ")),
            () -> "Found several constructors that are not " + sameOrLess + " for type " + type.getName()
        );
    }
}

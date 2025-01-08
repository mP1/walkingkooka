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

import java.util.Arrays;

/**
 * An interface with default methods which may be mixed into a test.
 */
public interface ThrowableTesting2<T extends Throwable> extends ThrowableTesting,
    ClassTesting2<T> {

    /**
     * The no args ctor must be protected.
     */
    @Test
    default void testNoDefaultArgumentProtected() {
        Arrays.stream(this.type().getDeclaredConstructors())
            .filter(c -> c.getParameterTypes().length == 0)
            .forEach(c -> checkEquals(JavaVisibility.PROTECTED, JavaVisibility.of(c),
                () -> "ctor visibility incorrect " + c));
    }

    /**
     * Checks that all ctors are protected.
     */
    @Test
    default void testAllConstructorsVisibility() {
        final Class<T> type = this.type();
        Arrays.stream(type.getDeclaredConstructors())
            .filter(c -> c.getParameterTypes().length != 0)
            .forEach(c ->
                this.checkEquals(this.typeVisibility(),
                    JavaVisibility.of(c),
                    () -> "ctor visibility incorrect " + c));
    }
}

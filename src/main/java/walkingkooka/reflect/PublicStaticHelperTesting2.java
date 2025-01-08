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

import walkingkooka.collect.list.Lists;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Interface with default methods implementing tests and other test helpers.
 */
final class PublicStaticHelperTesting2 {

    static void methodFilterAndCheckNone(final Class<?> type,
                                         final Predicate<Method> predicate,
                                         final String message) {
        assertEquals(
            Lists.empty(),
            Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.getName().startsWith("$")) // filter out any special methods like Jacoco's
                .filter(predicate)
                .collect(Collectors.toList()),
            message
        );
    }
}

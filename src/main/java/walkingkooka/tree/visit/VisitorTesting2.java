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

package walkingkooka.tree.visit;

import walkingkooka.collect.list.Lists;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class VisitorTesting2 {

    static void visitMethodsProtectedCheck(final String name, final Class<?> type) {
        final List<Method> wrong = allMethods(type)
                .stream()
                .filter(m -> !MethodAttributes.STATIC.is(m)) // only interested in instance methods.
                .filter(m -> m.getName().startsWith(name))
                .filter(m -> !MemberVisibility.PROTECTED.is(m))
                .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        assertEquals(Lists.empty(), wrong, () -> "all " + name + " methods in " + type.getName() + " should be protected=" + wrong);
    }

    static void visitMethodsSingleParameterCheck(final String name,
                                                 final Class<?> type) {
        final List<Method> wrong = allMethods(type)
                .stream()
                .filter(m -> !MethodAttributes.STATIC.is(m)) // only interested in instance methods.
                .filter(m -> m.getName().startsWith(name))
                .filter(m -> MemberVisibility.PROTECTED.is(m))
                .filter(m -> m.getParameterTypes().length != 1)
                .filter(m -> !MemberVisibility.PUBLIC.is(m.getParameterTypes()[0])) // only parameter must be public type
                .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        assertEquals(Lists.empty(), wrong, () -> "all " + name + " methods in " + type.getName() + " should have 1 parameter=" + wrong);
    }

    static List<Method> allMethods(final Class<?> type) {
        final List<Method> all = Lists.array();

        Class<?> current = type;
        do {
            all.addAll(Lists.of(type.getMethods()));

            current = current.getSuperclass();
        } while (current != Object.class);

        return all;
    }

    private VisitorTesting2() {
        throw new UnsupportedOperationException();
    }
}

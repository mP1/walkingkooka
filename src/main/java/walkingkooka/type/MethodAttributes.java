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

package walkingkooka.type;

import walkingkooka.collect.set.Sets;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public enum MethodAttributes {
    ABSTRACT {
        boolean testModifiers(final int modifiers) {
            return Modifier.isAbstract(modifiers);
        }
    },
    NATIVE {
        boolean testModifiers(final int modifiers) {
            return Modifier.isNative(modifiers);
        }
    },
    STATIC {
        boolean testModifiers(final int modifiers) {
            return Modifier.isStatic(modifiers);
        }
    },
    FINAL {
        boolean testModifiers(final int modifiers) {
            return Modifier.isFinal(modifiers);
        }
    };

    public final boolean is(final Method method) {
        Objects.requireNonNull(method, "method");
        return this.testModifiers(method.getModifiers());
    }

    abstract boolean testModifiers(final int modifiers);

    public static Set<MethodAttributes> get(final Method method) {
        Objects.requireNonNull(method, "method");

        Set<MethodAttributes> result = EnumSet.noneOf(MethodAttributes.class);

        final int modifiers = method.getModifiers();
        for (MethodAttributes possible : values()) {
            if (possible.testModifiers(modifiers)) {
                result.add(possible);
            }
        }

        return Sets.readOnly(result);
    }
}

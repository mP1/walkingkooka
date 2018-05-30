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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public enum MemberVisibility {
    PUBLIC {
        boolean testModifiers(final int modifiers) {
            return this.isPublic(modifiers);
        }
    },
    PROTECTED {
        boolean testModifiers(final int modifiers) {
            return this.isProtected(modifiers);
        }
    },
    PACKAGE_PRIVATE {
        boolean testModifiers(final int modifiers) {
            return !(this.isPublic(modifiers) ||
                    this.isProtected(modifiers) ||
                    this.isPrivate(modifiers));
        }
    },
    PRIVATE {
        boolean testModifiers(final int modifiers) {
            return this.isPrivate(modifiers);
        }
    };

    public final boolean is(final Member member) {
        Objects.requireNonNull(member, "member");
        return this.testModifiers(member.getModifiers());
    }

    abstract boolean testModifiers(final int modifiers);

    final boolean isPublic(final int modifiers) {
        return Modifier.isPublic(modifiers);
    }

    final boolean isProtected(final int modifiers) {
        return Modifier.isProtected(modifiers);
    }

    final boolean isPrivate(final int modifiers) {
        return Modifier.isProtected(modifiers);
    }

    public static MemberVisibility get(final Class<?> classs) {
        Objects.requireNonNull(classs, "class");
        return get0(classs.getModifiers());
    }

    public static MemberVisibility get(final Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor");
        return get0(constructor.getModifiers());
    }

    public static MemberVisibility get(final Field field) {
        Objects.requireNonNull(field, "field");
        return get0(field.getModifiers());
    }

    public static MemberVisibility get(final Method method) {
        Objects.requireNonNull(method, "method");
        return get0(method.getModifiers());
    }

    private static MemberVisibility get0(final int modifiers) {
        MemberVisibility result = null;

        for (MemberVisibility possible : values()) {
            if (possible.testModifiers(modifiers)) {
                result = possible;
                break;
            }
        }

        return result;
    }
}

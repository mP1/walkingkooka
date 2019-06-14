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

import walkingkooka.tree.visit.Visitable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * The visibility of a {@link Class}, {@link Constructor}, {@link Method} or {@link Field}.
 */
public enum JavaVisibility implements Visitable {
    PUBLIC {
        boolean testModifiers(final int modifiers) {
            return this.isPublic(modifiers);
        }

        @Override
        void accept(final JavaVisibilityVisitor visitor) {
            visitor.visitPublic();
        }
    },
    PROTECTED {
        boolean testModifiers(final int modifiers) {
            return this.isProtected(modifiers);
        }

        @Override
        void accept(final JavaVisibilityVisitor visitor) {
            visitor.visitProtected();
        }
    },
    PACKAGE_PRIVATE {
        boolean testModifiers(final int modifiers) {
            return !(this.isPublic(modifiers) ||
                    this.isProtected(modifiers) ||
                    this.isPrivate(modifiers));
        }

        @Override
        void accept(final JavaVisibilityVisitor visitor) {
            visitor.visitPackagePrivate();
        }
    },
    PRIVATE {
        boolean testModifiers(final int modifiers) {
            return this.isPrivate(modifiers);
        }

        @Override
        void accept(final JavaVisibilityVisitor visitor) {
            visitor.visitPrivate();
        }
    };

    public final boolean is(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        return this.testModifiers(type.getModifiers());
    }

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

    public static JavaVisibility get(final Class<?> classs) {
        Objects.requireNonNull(classs, "class");
        return get0(classs.getModifiers());
    }

    public static JavaVisibility get(final Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "constructor");
        return get0(constructor.getModifiers());
    }

    public static JavaVisibility get(final Field field) {
        Objects.requireNonNull(field, "field");
        return get0(field.getModifiers());
    }

    public static JavaVisibility get(final Method method) {
        Objects.requireNonNull(method, "method");
        return get0(method.getModifiers());
    }

    private static JavaVisibility get0(final int modifiers) {
        JavaVisibility result = null;

        for (JavaVisibility possible : values()) {
            if (possible.testModifiers(modifiers)) {
                result = possible;
                break;
            }
        }

        return result;
    }

    // JavaVisibilityVisitor............................................................................................

    abstract void accept(final JavaVisibilityVisitor visitor);
}

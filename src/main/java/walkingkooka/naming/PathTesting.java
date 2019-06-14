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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Path} with mostly parameter checking tests.
 */
public interface PathTesting<P extends Path<P, N> & HashCodeEqualsDefined & Comparable<P>, N extends Name> extends ComparableTesting<P>,
        ConstantsTesting<P>,
        ToStringTesting<P>,
        TypeNameTesting<P> {

    @Test
    default void testSeparatorConstant() {
        this.fieldPublicStaticCheck(this.type(),
                "SEPARATOR",
                PathSeparator.class);
    }

    @Test
    default void testAppendNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPath().append((N) null);
        });
    }

    @Test
    default void testAppendNameToRoot() {
        final P parent = this.root();
        final N appended = this.createName(0);
        final P child = parent.append(appended);

        parentCheck(child, parent);
        rootNotCheck(child);
        nameCheck(child, appended);
        valueCheck(child);
    }

    @Test
    default void testAppendName() {
        final P parent = this.separator().isRequiredAtStart() ? this.root() : null;
        final N appended0 = this.createName(0);
        final P child0 = null != parent ?
                parent.append(appended0) :
                this.parsePath(appended0.value());

        final N appended1 = this.createName(1);
        final P child1 = child0.append(appended1);

        parentCheck(child1, child0);
        rootNotCheck(child1);
        nameCheck(child1, appended1);
        valueCheck(child1);
    }

    @SuppressWarnings("unchecked")
    @Test
    default void testPathWithFourComponents() {
        final N name1 = this.createName(0);
        final N name2 = this.createName(1);
        final N name3 = this.createName(2);
        final N name4 = this.createName(3);

        final String fullPath4 = this.concat(name1, name2, name3, name4);

        final P four = this.parsePath(fullPath4);
        this.rootNotCheck(four);
        this.nameCheck(four, name4);
        this.valueCheck(four, fullPath4);

        final P three = this.parentCheck(four);
        this.rootNotCheck(three);
        this.nameCheck(three, name3);
        this.valueCheck(three, concat(name1, name2, name3));

        final P two = this.parentCheck(three);
        this.rootNotCheck(two);
        this.nameCheck(two, name2);
        this.valueCheck(two, concat(name1, name2));

        final boolean required = this.separator().isRequiredAtStart();

        final P one = this.parentCheck(two);
        if (required) {
            this.rootNotCheck(one);
        } else {
            this.rootCheck(one);
        }
        this.nameCheck(one, name1);
        this.valueCheck(one, concat(name1));

        if (required) {
            final P root = this.parentCheck(one);
            this.rootCheck(root);
        }
    }

    @Test
    default void testAppendNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPath().append((P) null);
        });
    }

    @Test
    default void testIterable() {
        final N name1 = this.createName(0);
        final N name2 = this.createName(1);
        final N name3 = this.createName(2);
        final N name4 = this.createName(3);

        final String fullPath4 = this.concat(name1, name2, name3, name4);

        final P path = this.parsePath(fullPath4);

        final List<N> names = Lists.array();
        for (N name : path) {
            names.add(name);
        }

        final List<N> actualNames = Lists.array();
        for (N name : Iterables.iterator(path.iterator())) {
            actualNames.add(name);
        }

        assertEquals(names, actualNames, "names returned by iterator");
    }

    @Test
    default void testToString() {
        final P path = this.createPath();
        this.toStringAndCheck(path, path.value());
    }

    // factory

    P root();

    P createPath();

    P parsePath(String name);

    N createName(int n);

    PathSeparator separator();

    // helpers

    /**
     * Concatenates a full path composed by the given names components
     */
    default String concat(final N... names) {
        final PathSeparator separator = this.separator();
        final char separatorCharacter = separator.character();

        final StringBuilder path = new StringBuilder();
        boolean addSeparator = separator.isRequiredAtStart();

        for (final N name : names) {
            if (addSeparator) {
                path.append(separatorCharacter);
            }
            path.append(name.value());
            addSeparator = true;
        }
        return path.toString();
    }

    default void rootCheck(final Path<?, ?> path) {
        assertEquals(Optional.empty(), path.parent(), () -> "path must not be root=" + path);
        assertEquals(true, path.isRoot(), () -> "path must not be root=" + path);
    }

    default void rootNotCheck(final Path<?, ?> path) {
        assertNotEquals(Optional.empty(), path.parent(), () -> "path must not be root=" + path);
        assertEquals(false, path.isRoot(), () -> "path must not be root=" + path);
    }

    default void valueCheck(final Path<?, ?> path) {
        final List<String> names = Lists.array();
        Path<?, ?> p = path;
        while (!p.isRoot()) {
            names.add(p.name().value());
            p = p.parent().get();
        }

        Collections.reverse(names);

        final StringBuilder b = new StringBuilder();
        final PathSeparator separator = this.separator();
        boolean add = separator.isRequiredAtStart();
        for (String name : names) {
            if (add) {
                b.append(separator.character());
            }
            b.append(name);
            add = true;
        }
    }

    default void valueCheck(final P path, final String value) {
        assertEquals(value, path.value(), "path");
    }

    default void nameCheck(final P path, final N name) {
        assertEquals(path.name(), name, "name");
        this.nameCheck(path, name.value());
    }

    default void nameCheck(final P path, final String value) {
        assertEquals(path.name().value(), value, "name");
    }

    default P parentCheck(final P path) {
        final Optional<P> parent = path.parent();
        assertNotEquals(Optional.empty(), parent, "parent missing");
        return parent.get();
    }

    default void parentCheck(final P path, final P parent) {
        assertEquals(parentCheck(path), parent, "parent");
    }

    default void parentCheck(final P path, final String value) {
        assertEquals(parentCheck(path).value(), value, "parent");
    }

    default void parentSame(final P path, final P parent) {
        assertSame(parentCheck(path), parent, () -> "parent of " + path);
    }

    default void parentAbsentCheck(final Path<?, ?> path) {
        assertEquals(Optional.empty(), path.parent(), "parent");
    }

    default void nameSameCheck(final Path<?, ?> path, final Name name) {
        assertSame(name, path.name(), "parent");
    }

    // ComparableTesting .........................................................................................

    default P createObject() {
        return this.createPath();
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Path.class.getSimpleName();
    }
}

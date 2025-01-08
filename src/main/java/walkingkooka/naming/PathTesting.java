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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Path} with mostly parameter checking tests.
 */
public interface PathTesting<P extends Path<P, N> & Comparable<P>, N extends Name> extends ComparableTesting2<P>,
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
        assertThrows(NullPointerException.class, () -> this.createPath().append((N) null));
    }

    @Test
    default void testAppendNameToRoot() {
        final P parent = this.root();
        final N appended = this.createName(0);
        final P child = parent.append(appended);

        this.parentCheck(child, parent);
        this.rootNotCheck(child);
        this.nameCheck(child, appended);
        this.valueCheck(child);
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

        this.parentCheck(child1, child0);
        this.rootNotCheck(child1);
        this.nameCheck(child1, appended1);
        this.valueCheck(child1);
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
        assertThrows(NullPointerException.class, () -> this.createPath().append((P) null));
    }

    @Test
    default void testIterable() {
        final N name1 = this.createName(0);
        final N name2 = this.createName(1);
        final N name3 = this.createName(2);
        final N name4 = this.createName(3);

        @SuppressWarnings("unchecked") final String fullPath4 = this.concat(name1, name2, name3, name4);

        final P path = this.parsePath(fullPath4);

        final List<N> names = Lists.array();
        for (N name : path) {
            names.add(name);
        }

        final List<N> actualNames = Lists.array();
        for (N name : Iterables.iterator(path.iterator())) {
            actualNames.add(name);
        }

        this.checkEquals(names, actualNames, () -> "names returned by iterator for " + path);
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
    @SuppressWarnings("unchecked")
    default <NN extends Name> String concat(final NN... names) {
        final PathSeparator separator = this.separator();
        final char separatorCharacter = separator.character();

        final StringBuilder path = new StringBuilder();
        boolean addSeparator = separator.isRequiredAtStart();

        for (final NN name : names) {
            if (addSeparator) {
                path.append(separatorCharacter);
            }
            path.append(name.value());
            addSeparator = true;
        }
        return path.toString();
    }

    default void rootCheck(final Path<?, ?> path) {
        this.checkEquals(Optional.empty(), path.parent(), () -> "path must not be root=" + path);
        this.checkEquals(true, path.isRoot(), () -> "path must not be root=" + path);
    }

    default void rootNotCheck(final Path<?, ?> path) {
        this.checkNotEquals(Optional.empty(), path.parent(), () -> "path must not be root=" + path);
        this.checkEquals(false, path.isRoot(), () -> "path must not be root=" + path);
    }

    default void valueCheck(final Path<?, ?> path) {
        final List<String> names = Lists.array();
        Path<?, ?> p = path;
        while (!p.isRoot()) {
            names.add(p.name().value());
            p = p.parent().get();
        }
        if (!p.separator().isRequiredAtStart()) {
            names.add(p.name().value());
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
        this.valueCheck(path, b.toString());
    }

    default void valueCheck(final Path<?, ?> path, final String value) {
        this.checkEquals(value, path.value(), "value of " + path);
    }

    default void nameCheck(final P path, final N name) {
        this.checkEquals(name, path.name(), () -> "name of " + path);
        this.nameCheck(path, name.value());
    }

    default void nameCheck(final Path<?, ?> path, final String value) {
        this.checkEquals(value, path.name().value(), () -> "name of " + path);
    }

    default P parentCheck(final P path) {
        final Optional<P> parent = path.parent();
        this.checkNotEquals(Optional.empty(), parent, () -> "parent of " + path);
        return parent.get();
    }

    default void parentCheck(final P path, final P parent) {
        this.checkEquals(parent, parentCheck(path), () -> "parent of " + path);
    }

    default void parentCheck(final P path, final String value) {
        this.checkEquals(value, parentCheck(path).value(), () -> "parent of " + path);
    }

    default void parentSame(final P path, final P parent) {
        assertSame(parent, parentCheck(path), () -> "parent of " + path);
    }

    default void parentAbsentCheck(final Path<?, ?> path) {
        this.checkEquals(Optional.empty(), path.parent(), () -> "parent of " + path);
    }

    default void nameSameCheck(final Path<?, ?> path, final Name name) {
        assertSame(name, path.name(), "name of " + path);
    }

    // ComparableTesting2 ..............................................................................................

    default P createObject() {
        return this.createPath();
    }

    // TypeNameTesting .................................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Path.class.getSimpleName();
    }
}

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

package walkingkooka.naming;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.ShouldBeQuoted;
import walkingkooka.type.MemberVisibility;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Base class for testing a {@link Path} with mostly parameter checking tests.
 */
abstract public class PathTestCase<P extends Path<P, N>, N extends Name> extends ClassTestCase<P> {

    protected PathTestCase() {
        super();
    }

    @Test
    public final void testNaming() {
        this.checkNaming(Path.class);
    }

    @Test
    final public void testSeparatorConstant() {
        this.checkFieldIsPublicStaticFinal(this.type(), "SEPARATOR", PathSeparator.class);
    }

    @Test(expected = NullPointerException.class)
    final public void testParseNullFails() {
        this.parsePath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        this.parsePath("");
    }

    @Test(expected = NullPointerException.class)
    final public void testAppendNullNameFails() {
        this.createPath().append((N) null);
    }

    @Test
    public void testAppendNameToRoot() {
        final P parent = this.root();
        final N appended = this.createName(0);
        final P child = parent.append(appended);

        checkParent(child, parent);
        checkNotRoot(child);
        checkName(child, appended);
        checkValue(child);
    }

    @Test
    public void testAppendName() {
        final P parent = this.separator().isRequiredAtStart() ? this.root() : null;
        final N appended0 = this.createName(0);
        final P child0 = null != parent ?
                parent.append(appended0) :
                this.parsePath(appended0.value());

        final N appended1 = this.createName(1);
        final P child1 = child0.append(appended1);

        checkParent(child1, child0);
        checkNotRoot(child1);
        checkName(child1, appended1);
        checkValue(child1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPathWithFourComponents() {
        final N name1 = this.createName(0);
        final N name2 = this.createName(1);
        final N name3 = this.createName(2);
        final N name4 = this.createName(3);

        final String fullPath4 = this.concat(name1, name2, name3, name4);

        final P four = this.parsePath(fullPath4);
        this.checkNotRoot(four);
        this.checkName(four, name4);
        this.checkValue(four, fullPath4);

        final P three = this.checkParent(four);
        this.checkNotRoot(three);
        this.checkName(three, name3);
        this.checkValue(three, concat(name1, name2, name3));

        final P two = this.checkParent(three);
        this.checkNotRoot(two);
        this.checkName(two, name2);
        this.checkValue(two, concat(name1, name2));

        final boolean required = this.separator().isRequiredAtStart();

        final P one = this.checkParent(two);
        if (required) {
            this.checkNotRoot(one);
        } else {
            this.checkRoot(one);
        }
        this.checkName(one, name1);
        this.checkValue(one, concat(name1));

        if (required) {
            final P root = this.checkParent(one);
            this.checkRoot(root);
        }
    }

    @Test(expected = NullPointerException.class) final public void testAppendNullPathFails() {
        this.createPath().append((P) null);
    }

    @Test
    public void testIterable() {
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

        Assert.assertEquals("names returned by iterator", names, actualNames);
    }

    @Test
    public void testToString() {
        final P path = this.createPath();
        Assert.assertEquals(path instanceof ShouldBeQuoted ?
                        CharSequences.quote(path.value()).toString() :
                        path.value(),
                path.toString());
    }

    // factory

    abstract protected P root();

    abstract protected P createPath();

    abstract protected P parsePath(String name);

    abstract protected N createName(int n);

    abstract protected PathSeparator separator();

    // helpers

    /**
     * Concatenates a full path composed by the given names components
     */
    @SafeVarargs
    final String concat(final N... names) {
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

    protected void checkRoot(final Path<?, ?> path) {
        Assert.assertEquals("path must not be root=" + path, Optional.empty(), path.parent());
        Assert.assertEquals("path must not be root=" + path, true, path.isRoot());
    }

    protected void checkNotRoot(final Path<?, ?> path) {
        Assert.assertNotEquals("path must not be root=" + path, Optional.empty(), path.parent());
        Assert.assertEquals("path must not be root=" + path, false, path.isRoot());
    }

    protected void checkValue(final Path<?, ?> path) {
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

    protected void checkValue(final P path, final String value) {
        Assert.assertEquals("path", value, path.value());
    }

    protected void checkName(final P path, final N name) {
        Assert.assertEquals("name", path.name(), name);
        this.checkName(path, name.value());
    }

    protected void checkName(final P path, final String value) {
        Assert.assertEquals("name", path.name().value(), value);
    }

    protected P checkParent(final P path) {
        final Optional<P> parent = path.parent();
        Assert.assertNotEquals("parent missing", Optional.empty(), parent);
        return parent.get();
    }

    protected void checkParent(final P path, final P parent) {
        Assert.assertEquals("parent", checkParent(path), parent);
    }

    protected void checkParent(final P path, final String value) {
        Assert.assertEquals("parent", checkParent(path).value(), value);
    }

    protected void checkSameParent(final P path, final P parent) {
        Assert.assertSame("parent of " + path, checkParent(path), parent);
    }

    protected void checkWithoutParent(final Path<?, ?> path) {
        Assert.assertEquals("parent", Optional.empty(), path.parent());
    }

    protected void checkSameName(final Path<?, ?> path, final Name name) {
        Assert.assertSame("parent", name, path.name());
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.SerializationTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class StringPathTest extends PathTestCase<StringPath, StringName>
        implements ParseStringTesting<StringPath>,
        SerializationTesting<StringPath> {

    @Test
    public void testParseMissingRequiredLeadingSlashFails() {
        this.parseFails("without-leading-slash", IllegalArgumentException.class);
    }

    @Test
    public void testParseEmptyComponentFails() {
        this.parseFails("/before//after", IllegalArgumentException.class);
    }

    @Test
    public void testParseFlat() {
        final String value = "/path to";
        final StringPath path = StringPath.parse(value);
        this.checkValue(path, value);
        this.checkNotRoot(path);
        this.checkName(path, StringName.with("path to"));
        this.checkSameParent(path, StringPath.ROOT);
    }

    @Test
    public void testParseHierarchical() {
        final String value = "/path/to";
        final StringPath path = StringPath.parse(value);
        this.checkValue(path, value);
        this.checkNotRoot(path);
        this.checkName(path, StringName.with("to"));
        this.checkParent(path, "/path");
    }

    @Test
    public void testRoot() {
        final StringPath path = StringPath.ROOT;
        this.checkRoot(path);
        this.checkValue(path, "/");
        this.checkSameName(path, StringName.ROOT);
        this.checkWithoutParent(path);
    }

    @Test
    public void testEqualsDifferentPath() {
        this.checkNotEquals(StringPath.parse("/different"));
    }

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(StringPath.parse("/zebra"));
    }

    @Test
    public void testCompareMore() {
        this.compareToAndCheckMore(StringPath.parse("/before"));
    }

    @Test
    public void testSerializeRootIsSingleton() throws Exception {
        this.serializeSingletonAndCheck(StringPath.ROOT);
    }

    @Test
    public void testSerializeParentNameAndIsRoot() throws Exception {
        final StringPath path = this.cloneUsingSerialization(StringPath.parse("/one/two/three"));
        final StringPath parent = path.parent().get();

        assertEquals("/one/two", parent.value());

        assertFalse(parent.isRoot());
        assertSame(parent, path.parent().get());
        assertEquals(StringName.with("two"), parent.name());

        final StringPath grandParent = parent.parent().get();
        assertEquals("/one", grandParent.value());
        assertFalse(grandParent.isRoot());
        assertEquals(StringName.with("one"), grandParent.name());

        assertSame(StringPath.ROOT, grandParent.parent().get());
    }

    @Override
    protected StringPath root() {
        return StringPath.ROOT;
    }

    @Override
    protected StringPath createPath() {
        return StringPath.parse("/path");
    }

    @Override
    protected StringPath parsePath(final String path) {
        return StringPath.parse(path);
    }

    @Override
    protected StringName createName(final int n) {
        return StringName.with("string-name-" + n);
    }

    @Override
    protected PathSeparator separator() {
        return StringPath.SEPARATOR;
    }

    @Override
    public Class<StringPath> type() {
        return StringPath.class;
    }

    @Override
    public StringPath createComparable() {
        return StringPath.parse("/path");
    }

    // ConstantTesting ........................................................................................

    @Override
    public Set<StringPath> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // ParseStringTesting ........................................................................................

    @Override
    public StringPath parse(final String text) {
        return StringPath.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializationTesting ........................................................................................

    @Override
    public StringPath serializableInstance() {
        return StringPath.parse("/path");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}

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

import org.junit.Test;
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

final public class StringPathTest extends PathTestCase<StringPath, StringName>
        implements SerializationTesting<StringPath> {

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingRequiredLeadingSlash() {
        StringPath.parse("without-leading-slash");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyComponent() {
        StringPath.parse("/before//after");
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
    public StringPath serializableInstance() {
        return StringPath.parse("/path");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}

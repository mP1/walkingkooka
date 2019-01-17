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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class PropertiesPathTest extends PathTestCase<PropertiesPath, PropertiesName>
        implements SerializationTesting<PropertiesPath> {

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithLeadingDot() {
        PropertiesPath.parse(".with-leading-dot");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyComponent() {
        PropertiesPath.parse("before..after");
    }

    @Test
    public void testParseFlat() {
        final String value = "xyz";
        final PropertiesPath path = PropertiesPath.parse(value);
        this.checkValue(path, value);
        this.checkRoot(path);
        this.checkName(path, PropertiesName.with(value));
    }

    @Test
    public void testParseHierarchical() {
        final String value = "ab.cd";
        final PropertiesPath path = PropertiesPath.parse(value);
        this.checkValue(path, value);
        this.checkNotRoot(path);
        this.checkName(path, PropertiesName.with("cd"));
        this.checkParent(path, "ab");
    }

    @Test
    @Ignore
    public void testAppendNameToRoot() {
        // nop
    }

    @Test
    public void testGeneral() throws Exception {
        final PropertiesPath path = this.cloneUsingSerialization(PropertiesPath.parse("one.two.three"));
        final PropertiesPath parent = path.parent().get();

        assertEquals("one.two", parent.value());

        assertFalse(parent.isRoot());
        assertSame(parent, path.parent().get());
        assertEquals(PropertiesName.with("two"), parent.name());

        final PropertiesPath grandParent = parent.parent().get();
        assertEquals("one", grandParent.value());
        assertTrue(grandParent.isRoot());
        assertEquals(PropertiesName.with("one"), grandParent.name());
    }

    @Override
    protected PropertiesPath root() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected PropertiesPath createPath() {
        return PropertiesPath.parse("abc");
    }

    @Override
    protected PropertiesPath parsePath(final String path) {
        return PropertiesPath.parse(path);
    }

    @Override
    protected PropertiesName createName(final int n) {
        return PropertiesName.with("property-" + n);
    }

    @Override
    protected PathSeparator separator() {
        return PropertiesPath.SEPARATOR;
    }

    @Override
    public Class<PropertiesPath> type() {
        return PropertiesPath.class;
    }

    @Override
    public PropertiesPath serializableInstance() {
        return PropertiesPath.parse("abc.def");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}

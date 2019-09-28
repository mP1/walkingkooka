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
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ParseStringTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class PropertiesPathTest extends PathTestCase<PropertiesPath, PropertiesName>
        implements ParseStringTesting<PropertiesPath> {

    @Test
    public void testParseEmptyComponent() {
        this.parseStringFails("before..after", IllegalArgumentException.class);
    }

    @Test
    public void testParseFlat() {
        final String value = "xyz";
        final PropertiesPath path = PropertiesPath.parse(value);
        this.valueCheck(path, value);
        this.rootCheck(path);
        this.nameCheck(path, PropertiesName.with(value));
    }

    @Test
    public void testParseHierarchical() {
        final String value = "ab.cd";
        final PropertiesPath path = PropertiesPath.parse(value);
        this.valueCheck(path, value);
        this.rootNotCheck(path);
        this.nameCheck(path, PropertiesName.with("cd"));
        this.parentCheck(path, "ab");
    }

    @Override
    public void testAppendNameToRoot() {
        // nop
    }

    @Test
    public void testGeneral() {
        final PropertiesPath path = PropertiesPath.parse("one.two.three");
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

    @Test
    public void testEqualsDifferentPath() {
        this.checkNotEquals(PropertiesPath.parse("different.property"));
    }

    @Override
    public PropertiesPath root() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PropertiesPath createPath() {
        return PropertiesPath.parse("abc");
    }

    @Override
    public PropertiesPath parsePath(final String path) {
        return PropertiesPath.parse(path);
    }

    @Override
    public PropertiesName createName(final int n) {
        return PropertiesName.with("property-" + n);
    }

    @Override
    public PathSeparator separator() {
        return PropertiesPath.SEPARATOR;
    }

    @Override
    public Class<PropertiesPath> type() {
        return PropertiesPath.class;
    }

    @Override
    public PropertiesPath createComparable() {
        return PropertiesPath.parse("property-abc");
    }

    // ConstantTesting ........................................................................................

    @Override
    public Set<PropertiesPath> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // ParseStringTesting ........................................................................................

    @Override
    public PropertiesPath parseString(final String text) {
        return PropertiesPath.parse(text);
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }
}

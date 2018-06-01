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

final public class PropertiesPathTest extends PathTestCase<PropertiesPath, PropertiesName> {

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

    @Override PropertiesPath root() {
        throw new UnsupportedOperationException();
    }

    @Override protected PropertiesPath createPath() {
        return PropertiesPath.parse("abc");
    }

    @Override PropertiesPath parsePath(final String path) {
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
    protected Class<PropertiesPath> type() {
        return PropertiesPath.class;
    }
}

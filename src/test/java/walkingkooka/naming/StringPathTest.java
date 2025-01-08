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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;

import java.util.Set;

final public class StringPathTest implements PathTesting<StringPath, StringName>,
    ClassTesting2<StringPath>,
    ParseStringTesting<StringPath> {

    @Override
    public void testAllConstructorsVisibility() {
    }

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
    }

    @Test
    public void testParseMissingRequiredLeadingSlashFails() {
        this.parseStringFails("without-leading-slash", IllegalArgumentException.class);
    }

    @Test
    public void testParseEmptyComponentFails() {
        this.parseStringFails("/before//after", IllegalArgumentException.class);
    }

    @Test
    public void testParseSlash() {
        final String value = "/";
        final StringPath path = StringPath.parse(value);
        this.valueCheck(path, value);
        this.rootCheck(path);
        this.nameCheck(path, StringName.ROOT);
        this.parentAbsentCheck(path);
    }

    @Test
    public void testParseFlat() {
        final String value = "/path to";
        final StringPath path = StringPath.parse(value);
        this.valueCheck(path, value);
        this.rootNotCheck(path);
        this.nameCheck(path, StringName.with("path to"));
        this.parentSame(path, StringPath.ROOT);
    }

    @Test
    public void testParseHierarchical() {
        final String value = "/path/to";
        final StringPath path = StringPath.parse(value);
        this.valueCheck(path, value);
        this.rootNotCheck(path);
        this.nameCheck(path, StringName.with("to"));
        this.parentCheck(path, "/path");
    }

    @Test
    public void testRoot() {
        final StringPath path = StringPath.ROOT;
        this.rootCheck(path);
        this.valueCheck(path, "/");
        this.nameSameCheck(path, StringName.ROOT);
        this.parentAbsentCheck(path);
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

    @Override
    public StringPath root() {
        return StringPath.ROOT;
    }

    @Override
    public StringPath createPath() {
        return StringPath.parse("/path");
    }

    @Override
    public StringPath parsePath(final String path) {
        return StringPath.parse(path);
    }

    @Override
    public StringName createName(final int n) {
        return StringName.with("string-name-" + n);
    }

    @Override
    public PathSeparator separator() {
        return StringPath.SEPARATOR;
    }

    // ComparableTesting................................................................................................

    @Override
    public StringPath createComparable() {
        return StringPath.parse("/path");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<StringPath> type() {
        return StringPath.class;
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantTesting ..................................................................................................

    @Override
    public Set<StringPath> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // ParseStringTesting ..............................................................................................

    @Override
    public StringPath parseString(final String text) {
        return StringPath.parse(text);
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

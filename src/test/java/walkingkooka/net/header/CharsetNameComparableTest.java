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

package walkingkooka.net.header;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparableTestCase;
import walkingkooka.compare.Comparators;

import java.util.List;

import static org.junit.Assert.assertEquals;

public final class CharsetNameComparableTest extends ComparableTestCase<CharsetName> {

    private final static String CHARSET = "utf-16";

    @Test
    public void testDifferentCaseUnimportant() {
        this.checkEquals(CharsetName.with(CHARSET.toUpperCase()));
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(CharsetName.with("UTF-8"));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(CharsetName.with("utf-8"));
    }

    @Test
    public void testSort() {
        final CharsetName wildcard = CharsetName.WILDCARD_CHARSET;
        final CharsetName utf8 = CharsetName.UTF_8;
        final CharsetName unsupported = CharsetName.with("unsupported");

        final List<CharsetName> sorted = Lists.array();
        sorted.add(unsupported);
        sorted.add(utf8);
        sorted.add(wildcard);
        sorted.sort(Comparators.naturalOrdering());

        assertEquals(Lists.of(wildcard, unsupported, utf8), sorted);
    }

    @Override
    protected CharsetName createComparable() {
        return CharsetName.with(CHARSET);
    }
}

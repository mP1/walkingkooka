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

package walkingkooka.tree.search;

import org.junit.Test;
import walkingkooka.compare.ComparableTestCase;

public final class SearchNodeAttributeNameComparableTest extends ComparableTestCase<SearchNodeAttributeName> {

    @Test
    public void testDifferent() {
        this.checkNotEquals(SearchNodeAttributeName.with("abc"));
    }

    @Test
    public void testDifferentCase() {
        this.checkNotEquals(SearchNodeAttributeName.with("jkl"));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(SearchNodeAttributeName.with("XYZ"));
    }

    @Test
    public void testMore() {
        this.compareToAndCheckLess(SearchNodeAttributeName.with("abc"));
    }

    @Override
    protected SearchNodeAttributeName createComparable(){
        return SearchNodeAttributeName.with("JKL");
    }
}

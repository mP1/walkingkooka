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

package walkingkooka.net;

import org.junit.Test;
import walkingkooka.compare.ComparableTestCase;

public final class UrlSchemeComparableTest extends ComparableTestCase<UrlScheme> {

    @Test
    public void testLess() {
        this.compareToAndCheckLess(UrlScheme.with("z"));
    }

    @Test
    public void testLessIgnoresCase() {
        this.compareToAndCheckLess(UrlScheme.with("Z"));
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(UrlScheme.with("different"));
    }

    @Test
    public void testEqualityCaseIgnored() {
        this.checkEquals(UrlScheme.with("CUSTOM"));
    }

    @Override
    protected UrlScheme createComparable() {
        return UrlScheme.with("custom");
    }
}

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

package walkingkooka.net.http;


import org.junit.Test;
import walkingkooka.compare.ComparableTestCase;

final public class HttpHeaderNameComparableTest extends ComparableTestCase<HttpHeaderName> {

    @Test
    public void testBefore() {
        this.compareToAndCheckLess(HttpHeaderName.with("zzz"));
    }

    @Test
    public void testBeforeCaseUnimportant() {
        this.compareToAndCheckLess(HttpHeaderName.with("ZZZ"));
    }

    @Test
    public void testAfter() {
        this.compareToAndCheckMore(HttpHeaderName.with("aaa"));
    }

    @Test
    public void testAfterCaseUnimportant() {
        this.compareToAndCheckMore(HttpHeaderName.with("AAA"));
    }

    @Override
    protected HttpHeaderName createComparable() {
        return HttpHeaderName.with("mmmm");
    }
}

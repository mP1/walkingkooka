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
import walkingkooka.compare.ComparableTestCase;

final public class ContentDispositionParameterNameComparableTest extends ComparableTestCase<ContentDispositionParameterName<?>> {

    @Test
    public void testBefore() {
        this.compareToAndCheckLess(ContentDispositionParameterName.with("zzz"));
    }

    @Test
    public void testBeforeCaseUnimportant() {
        this.compareToAndCheckLess(ContentDispositionParameterName.with("ZZZ"));
    }

    @Test
    public void testAfter() {
        this.compareToAndCheckMore(ContentDispositionParameterName.with("aaa"));
    }

    @Test
    public void testAfterCaseUnimportant() {
        this.compareToAndCheckMore(ContentDispositionParameterName.with("AAA"));
    }

    @Override
    protected ContentDispositionParameterName<?> createComparable() {
        return ContentDispositionParameterName.with("mmmm");
    }
}

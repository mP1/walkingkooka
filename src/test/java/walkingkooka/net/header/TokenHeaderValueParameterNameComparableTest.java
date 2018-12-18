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

final public class TokenHeaderValueParameterNameComparableTest extends ComparableTestCase<TokenHeaderValueParameterName<?>> {

    @Test
    public void testBefore() {
        this.compareToAndCheckLess(TokenHeaderValueParameterName.with("zzz"));
    }

    @Test
    public void testBeforeCaseUnimportant() {
        this.compareToAndCheckLess(TokenHeaderValueParameterName.with("ZZZ"));
    }

    @Test
    public void testAfter() {
        this.compareToAndCheckMore(TokenHeaderValueParameterName.with("aaa"));
    }

    @Test
    public void testAfterCaseUnimportant() {
        this.compareToAndCheckMore(TokenHeaderValueParameterName.with("AAA"));
    }

    @Override
    protected TokenHeaderValueParameterName<?> createComparable() {
        return TokenHeaderValueParameterName.with("mmmm");
    }
}

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

package walkingkooka.tree.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ExpressionNodeSelectorPredicateNormalizeSpaceFunctionTest extends ExpressionNodeSelectorPredicateFunctionTestCase<ExpressionNodeSelectorPredicateNormalizeSpaceFunction, String> {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParametersFails() {
        this.apply2();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testTwoParametersFails() {
        this.apply2("a1", "b2");
    }

    @Test
    public void testUnnecessary() {
        this.applyAndCheck3("a", "a");
    }

    @Test
    public void testUnnecessary2() {
        this.applyAndCheck3("abc", "abc");
    }

    @Test
    public void testUnnecessary3() {
        this.applyAndCheck3("a b c", "a b c");
    }

    @Test
    public void testLeadingWhitespace() {
        this.applyAndCheck3("  a 1", "a 1");
    }

    @Test
    public void testLeadingWhitespace2() {
        this.applyAndCheck3("\n\r  a 1", "a 1");
    }

    @Test
    public void testTrailingWhitespace() {
        this.applyAndCheck3("a 1  ", "a 1");
    }

    @Test
    public void testTrailingWhitespace2() {
        this.applyAndCheck3("a 1 \n\r ", "a 1");
    }

    @Test
    public void testWithoutLeadingOrTrailingWhitespace() {
        this.applyAndCheck3("a 1", "a 1");
    }

    @Test
    public void testWithLeadingOrTrailingWhitespace() {
        this.applyAndCheck3(" a 1  ", "a 1");
    }

    @Test
    public void testWithLeadingOrTrailingWhitespace2() {
        this.applyAndCheck3(" a    1  ", "a 1");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace() {
        this.applyAndCheck3("a\nb", "a b");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace2() {
        this.applyAndCheck3("a\rb", "a b");
    }

    @Test
    public void testIgnoresNonSpaceWhitespace3() {
        this.applyAndCheck3("a\r\n  b", "a b");
    }

    @Test
    public void testNumber() {
        this.applyAndCheck3(123, "123");
    }

    private void applyAndCheck3(final Object value, final String expected) {
        this.applyAndCheck2(parameters(value), expected);
    }

    @Test
    public void testToString() {
        assertEquals("normalize-space", this.createBiFunction().toString());
    }

    @Override
    protected ExpressionNodeSelectorPredicateNormalizeSpaceFunction createBiFunction() {
        return ExpressionNodeSelectorPredicateNormalizeSpaceFunction.INSTANCE;
    }

    @Override
    protected Class<ExpressionNodeSelectorPredicateNormalizeSpaceFunction> type() {
        return ExpressionNodeSelectorPredicateNormalizeSpaceFunction.class;
    }
}

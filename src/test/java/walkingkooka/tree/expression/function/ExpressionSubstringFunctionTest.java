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

package walkingkooka.tree.expression.function;

import org.junit.Test;
import walkingkooka.tree.select.NodeSelector;

import static org.junit.Assert.assertEquals;

public final class ExpressionSubstringFunctionTest extends ExpressionFunctionTestCase<ExpressionSubstringFunction, String> {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParametersFails() {
        this.apply2(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOneParametersFails() {
        this.apply2(this, "a1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoParametersFails() {
        this.apply2(this, "a1", "b2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFourParametersFails() {
        this.apply2(this, "a1", 2, 3, 4);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testSubstringOutOfRange() {
        this.apply2(this, "abcdef", -2, 2);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testSubstringOutOfRange2() {
        this.apply2(this, "abcdef", 2, -1);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testSubstringOutOfRange3() {
        this.apply2(this, "abcdef", 1, 99);
    }

    @Test
    public void testSubstring() {
        this.applyAndCheck2(parameters(this, "abcdef", 3, 0), "");
    }

    @Test
    public void testSubstring2() {
        this.applyAndCheck2(parameters(this, "abcdef", 4, 1), "d");
    }

    @Test
    public void testSubstring3() {
        this.applyAndCheck2(parameters(this, "abcdef", 3, 2), "cd");
    }

    @Test
    public void testSubstringLengthMissing() {
        this.applyAndCheck2(parameters(this, "abcdef", 2), "bcdef");
    }

    @Test
    public void testSubstringLengthMissing2() {
        this.applyAndCheck2(parameters(this, "abc", 3), "c");
    }

    @Test
    public void testToString() {
        assertEquals("substring", this.createBiFunction().toString());
    }

    @Override
    protected ExpressionSubstringFunction createBiFunction() {
        return ExpressionSubstringFunction.with(NodeSelector.INDEX_BIAS);
    }

    @Override
    protected Class<ExpressionSubstringFunction> type() {
        return ExpressionSubstringFunction.class;
    }
}

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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionContainsFunctionTest extends ClassTestCase<ExpressionContainsFunction>
        implements ExpressionFunctionTesting<ExpressionContainsFunction, Boolean> {

    @Test
    public void testOnlyThisParameterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.apply2(this);
        });
    }

    @Test
    public void testOneParameterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.apply2(this, "a1");
        });
    }

    @Test
    public void testContains() {
        this.applyAndCheck2(parameters(this, "xyz", "x"), true);
    }

    @Test
    public void testContains2() {
        this.applyAndCheck2(parameters(this, "xyz", "z"), true);
    }

    @Test
    public void testContains3() {
        this.applyAndCheck2(parameters(this, 123, 1), true);
    }

    @Test
    public void testMissing() {
        this.applyAndCheck2(parameters(this, "xyz", "a"), false);
    }

    @Test
    public void testMissing2() {
        this.applyAndCheck2(parameters(this, 123, 4), false);
    }

    @Test
    public void testMissingCaseSensitive() {
        this.applyAndCheck2(parameters(this, "xyz", "Z"), false);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "contains");
    }

    @Override
    public ExpressionContainsFunction createBiFunction() {
        return ExpressionContainsFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionContainsFunction> type() {
        return ExpressionContainsFunction.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}

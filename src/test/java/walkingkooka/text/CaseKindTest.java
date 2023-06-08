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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CaseKindTest implements ClassTesting<CaseKind> {

    @Test
    public void testNullTextFails() {
        for (final CaseKind from : CaseKind.values()) {
            for (final CaseKind to : CaseKind.values()) {
                assertThrows(
                        NullPointerException.class,
                        () -> from.change(null, to)
                );
            }
        }
    }

    @Test
    public void testNullCaseKindFails() {
        for (final CaseKind from : CaseKind.values()) {
            assertThrows(
                    NullPointerException.class,
                    () -> from.change("text", null)
            );
        }
    }

    @Test
    public void testSameCaseKind() {
        for (final CaseKind from : CaseKind.values()) {
            final String text = "abc123";

            assertSame(
                    text,
                    from.change(text, from)
            );
        }
    }

    @Test
    public void testEmpty() {
        for (final CaseKind from : CaseKind.values()) {
            for (final CaseKind to : CaseKind.values()) {
                this.changeAndCheckBothWays(
                        from,
                        "",
                        to,
                        ""
                );
            }
        }
    }

    @Test
    public void testCamelToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abc",
                CaseKind.PASCAL,
                "Abc"
        );
    }

    @Test
    public void testCamelToPascal2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testCamelToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abc",
                CaseKind.KEBAB,
                "abc"
        );
    }

    @Test
    public void testCamelToKebab2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    @Test
    public void testCamelToSnake() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.SNAKE,
                "abc_def"
        );
    }

    @Test
    public void testCamelToSnake2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDefGhi",
                CaseKind.SNAKE,
                "abc_def_ghi"
        );
    }

    @Test
    public void testKebabToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.CAMEL,
                "abcDef"
        );
    }

    @Test
    public void testKebabToNormal() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.NORMAL,
                "abc def"
        );
    }

    @Test
    public void testKebabToNormal2() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "ABC-DEF",
                CaseKind.NORMAL,
                "ABC DEF"
        );
    }

    @Test
    public void testKebabToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testKebabToSnake() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.SNAKE,
                "abc_def"
        );
    }

    @Test
    public void testKebabToSnake2() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def-g",
                CaseKind.SNAKE,
                "abc_def_g"
        );
    }

    @Test
    public void testKebabToSnake3() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "ABC-DEF",
                CaseKind.SNAKE,
                "ABC_DEF"
        );
    }

    @Test
    public void testNormalToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.CAMEL,
                "abcDef"
        );
    }

    @Test
    public void testNormalToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    @Test
    public void testNormalToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testPascalToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "A",
                CaseKind.CAMEL,
                "a"
        );
    }

    @Test
    public void testPascalToCamel2() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "Abc",
                CaseKind.CAMEL,
                "abc"
        );
    }

    @Test
    public void testPascalToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "AbcDef",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    private void changeAndCheckBothWays(final CaseKind from,
                                        final String fromText,
                                        final CaseKind to,
                                        final String toText) {
        this.changeAndCheck(
                from,
                fromText,
                to,
                toText
        );

        this.changeAndCheck(
                to,
                toText,
                from,
                fromText
        );
    }

    private void changeAndCheck(final CaseKind from,
                                final String text,
                                final CaseKind to,
                                final String expected) {
        this.checkEquals(
                expected,
                from.change(
                        text,
                        to
                ),
                () -> from + " " + CharSequences.quoteAndEscape(text) + " TO " + to + " " + CharSequences.quoteAndEscape(expected)
        );
    }

    // kebabEnumName....................................................................................................

    @Test
    public void testKebabEnumNameNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> CaseKind.kebabEnumName(null)
        );
    }

    @Test
    public void testKebanEnumName() {
        this.checkEquals(
                "half-up",
                CaseKind.kebabEnumName(RoundingMode.HALF_UP)
        );
    }


    @Test
    public void testKebanEnumName2() {
        this.checkEquals(
                "floor",
                CaseKind.kebabEnumName(RoundingMode.FLOOR)
        );
    }

    // CaseKind.........................................................................................................

    @Override
    public Class<CaseKind> type() {
        return CaseKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

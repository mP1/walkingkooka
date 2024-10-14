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
    public void testChangeWithNullTextFails() {
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
    public void testChangeWithNullCaseKindFails() {
        for (final CaseKind from : CaseKind.values()) {
            assertThrows(
                    NullPointerException.class,
                    () -> from.change("text", null)
            );
        }
    }

    @Test
    public void testChangeSameCaseKind() {
        for (final CaseKind from : CaseKind.values()) {
            final String text = "abc123";

            assertSame(
                    text,
                    from.change(text, from)
            );
        }
    }

    @Test
    public void testChangeEmpty() {
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
    public void testChangeCamelToNormal() {
        this.changeAndCheck(
                CaseKind.CAMEL,
                "abc",
                CaseKind.NORMAL,
                "abc"
        );
    }

    @Test
    public void testChangeCamelToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abc",
                CaseKind.PASCAL,
                "Abc"
        );
    }

    @Test
    public void testChangeCamelToPascal2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testChangeCamelToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abc",
                CaseKind.KEBAB,
                "abc"
        );
    }

    @Test
    public void testChangeCamelToKebab2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    @Test
    public void testChangeCamelToSnake() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDef",
                CaseKind.SNAKE,
                "ABC_DEF"
        );
    }

    @Test
    public void testChangeCamelToSnake2() {
        this.changeAndCheckBothWays(
                CaseKind.CAMEL,
                "abcDefGhi",
                CaseKind.SNAKE,
                "ABC_DEF_GHI"
        );
    }

    @Test
    public void testChangeCamelToTitle() {
        this.changeAndCheck(
                CaseKind.CAMEL,
                "abc",
                CaseKind.TITLE,
                "Abc"
        );
    }

    @Test
    public void testChangeKebabToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.CAMEL,
                "abcDef"
        );
    }

    @Test
    public void testChangeKebabToNormal() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.NORMAL,
                "abc def"
        );
    }

    @Test
    public void testChangeKebabToNormal2() {
        this.changeAndCheck(
                CaseKind.KEBAB,
                "ABC-DEF",
                CaseKind.NORMAL,
                "abc def"
        );
    }

    @Test
    public void testChangeKebabToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testChangeKebabToSnake() {
        this.changeAndCheck(
                CaseKind.KEBAB,
                "ABC-DEF",
                CaseKind.SNAKE,
                "ABC_DEF"
        );
    }

    @Test
    public void testChangeKebabToSnake2() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def-g",
                CaseKind.SNAKE,
                "ABC_DEF_G"
        );
    }

    @Test
    public void testChangeKebabToSnake3() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def",
                CaseKind.SNAKE,
                "ABC_DEF"
        );
    }

    @Test
    public void testChangeKebabToTitle() {
        this.changeAndCheckBothWays(
                CaseKind.KEBAB,
                "abc-def-ghi",
                CaseKind.TITLE,
                "Abc Def Ghi"
        );
    }

    @Test
    public void testChangeNormalToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.CAMEL,
                "abcDef"
        );
    }

    @Test
    public void testChangeNormalToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    @Test
    public void testChangeNormalToPascal() {
        this.changeAndCheckBothWays(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.PASCAL,
                "AbcDef"
        );
    }

    @Test
    public void testChangeNormalToTitle() {
        this.changeAndCheck(
                CaseKind.NORMAL,
                "abc def",
                CaseKind.TITLE,
                "Abc Def"
        );
    }

    @Test
    public void testChangePascalToCamel() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "A",
                CaseKind.CAMEL,
                "a"
        );
    }

    @Test
    public void testChangePascalToCamel2() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "Abc",
                CaseKind.CAMEL,
                "abc"
        );
    }

    @Test
    public void testChangePascalToTitle() {
        this.changeAndCheck(
                CaseKind.PASCAL,
                "AbcDef",
                CaseKind.TITLE,
                "Abc Def"
        );
    }

    @Test
    public void testChangePascalToKebab() {
        this.changeAndCheckBothWays(
                CaseKind.PASCAL,
                "AbcDef",
                CaseKind.KEBAB,
                "abc-def"
        );
    }

    @Test
    public void testChangeTitleToNormal() {
        this.changeAndCheck(
                CaseKind.TITLE,
                "Abc def",
                CaseKind.NORMAL,
                "abc def"
        );
    }

    @Test
    public void testChangeTitleToNormal2() {
        this.changeAndCheck(
                CaseKind.TITLE,
                "URL def",
                CaseKind.NORMAL,
                "url def"
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

    @Test
    public void testKebabToTitle() {
        this.checkEquals(
                "Abc Def Ghi",
                CaseKind.kebabToTitle("abc-def-ghi")
        );
    }

    // kebabEnumName....................................................................................................

    @Test
    public void testKebabEnumNameWithNullFails() {
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

    // class............................................................................................................

    @Override
    public Class<CaseKind> type() {
        return CaseKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

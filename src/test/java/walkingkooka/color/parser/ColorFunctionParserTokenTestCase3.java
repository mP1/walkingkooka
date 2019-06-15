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

package walkingkooka.color.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTesting;
import walkingkooka.type.JavaVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ColorFunctionParserTokenTestCase3<T extends ColorFunctionParserToken> extends ColorFunctionParserTokenTestCase<T>
        implements ParserTokenTesting<T>,
        HashCodeEqualsDefinedTesting<T>,
        IsMethodTesting<T> {

    ColorFunctionParserTokenTestCase3() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.check(ColorFunctionParserToken.class,
                "ColorFunction",
                ParserToken.class,
                this.type());
    }

    @Test
    public void testEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    public final T createObject() {
        return this.createToken();
    }

    // isMethodTesting2.................................................................................................

    @Override
    public T createIsMethodObject() {
        return this.createToken(this.text());
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "ColorFunction";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isNoise") || m.equals("isSymbol"); // skip isNoise
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

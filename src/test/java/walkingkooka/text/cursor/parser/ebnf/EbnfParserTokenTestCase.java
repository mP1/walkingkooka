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
 */
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.PublicStaticFactoryTesting;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EbnfParserTokenTestCase<T extends EbnfParserToken> implements ClassTesting2<T>,
        IsMethodTesting<T>,
        ParserTokenTesting<T> {

    EbnfParserTokenTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod()  {
        PublicStaticFactoryTesting.check(EbnfParserToken.class,
                "Ebnf",
                ParserToken.class,
                this.type());
    }

    @Test
    public final void testEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespacePropertiesNullCheck() throws Exception {
        final Optional<EbnfParserToken> without = this.createToken().withoutCommentsSymbolsOrWhitespace();
        if(without.isPresent()){
            BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(without.get(), Predicates.never());
        }
    }

    static EbnfSymbolParserToken symbol(final String s) {
        return EbnfParserToken.symbol(s, s);
    }

    // isMethodTesting2.....................................................................................

    @Override
    public final T createIsMethodObject() {
        return this.createToken();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Ebnf";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isNoise"); // skip isNoise
    }

    // ClassTestCase.................................................................................................

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

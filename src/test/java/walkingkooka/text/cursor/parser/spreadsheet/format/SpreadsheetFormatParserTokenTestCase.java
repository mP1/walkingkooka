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
package walkingkooka.text.cursor.parser.spreadsheet.format;

import org.junit.jupiter.api.Test;
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

public abstract class SpreadsheetFormatParserTokenTestCase<T extends SpreadsheetFormatParserToken> implements ClassTesting2<T>,
        IsMethodTesting<T>,
        ParserTokenTesting<T> {

    SpreadsheetFormatParserTokenTestCase() {
        super();
    }

    @Test
    public final void testPublicStaticFactoryMethod() {
        PublicStaticFactoryTesting.check(SpreadsheetFormatParserToken.class,
                "SpreadsheetFormat",
                ParserToken.class,
                this.type());
    }

    @Test
    public void testWithEmptyTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("");
        });
    }

    @Test
    public final void testWithWhitespace() {
        final String text = " ";
        final T token = this.createToken(text);
        this.checkText(token, text);
    }

    @Test
    public void testSetTextEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setText("");
        });
    }

    @Test
    public void testWithoutSymbolsPropertiesNullCheck() throws Exception {
        final Optional<SpreadsheetFormatParserToken> without = this.createToken().withoutSymbols();
        if (without.isPresent()) {
            BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(without.get());
        }
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final T createIsMethodObject() {
        return this.createToken();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "SpreadsheetFormat";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isCondition") || m.equals("isNoise") || m.equals("isSymbol");
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

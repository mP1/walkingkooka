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
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.ComparisonRelation;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetFormatConditionParserTokenTestCase<T extends SpreadsheetFormatConditionParserToken<T>> extends SpreadsheetFormatParentParserTokenTestCase<T> {

    @Test
    public final void testWithMissingNumberTokenFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("", this.whitespace());
        });
    }

    @Test
    public final void testWithMissingNumberTokenFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken("", this.whitespace(), this.whitespace());
        });
    }

    @Test
    public final void testSetValueMissingNumberTokenFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setValue(Lists.of(this.whitespace()));
        });
    }

    @Test
    public final void testSetValueWrongCountFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setValue(Lists.of(this.number1(), this.number2()));
        });
    }

    @Test
    public final void testSetValueDifferent() {
        final T token = this.createToken();
        final SpreadsheetFormatParserToken value = this.number(123);
        final T different = token.setValue(Lists.of(value));
        assertNotSame(token, different);
        this.checkValue(different, value);

        assertEquals(Optional.of(different), different.withoutSymbols());
    }

    @Test
    public final void testSetValueDifferent2() {
        final T token = this.createToken();

        final List<ParserToken> values = Lists.of(this.number(123), whitespace());
        final T different = token.setValue(values);
        assertNotSame(token, different);
        this.checkValue(different, values);

        final Optional<SpreadsheetFormatParserToken> differentWithout = different.withoutSymbols();
        assertNotEquals(Optional.of(different), differentWithout);

        this.checkValue(differentWithout.get(), values.subList(0, 1));
    }

    @Test
    public final void testRelation() {
        final T token = this.createToken();
        final String symbol = this.operatorSymbol().value();
        assertEquals(ComparisonRelation.findWithSymbol(symbol),
                token.relation(),
                () -> "Wrong relation for token " + token);
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    @Override
    protected final String text() {
        return this.operatorSymbol().text() + NUMBER1;
    }

    abstract SpreadsheetFormatSymbolParserToken operatorSymbol();

    final SpreadsheetFormatParserToken rightToken() {
        return this.number1();
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(this.operatorSymbol(), this.rightToken());
    }

    @Override
    protected T createDifferentToken() {
        return this.createToken(this.operatorSymbol().text() + NUMBER2,
                Lists.of(this.operatorSymbol(), this.number2()));
    }
}

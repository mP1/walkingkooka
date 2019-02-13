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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetFormatParentParserTokenTestCase<T extends SpreadsheetFormatParentParserToken<T>> extends SpreadsheetFormatParserTokenTestCase<T> {

    final static String NUMBER1 = "1";
    final static String NUMBER2 = "22";

    final static String TEXT1 = CharSequences.quote("text-1").toString();
    final static String TEXT2 = CharSequences.quote("text-2").toString();

    final static String WHITESPACE = "   ";

    SpreadsheetFormatParentParserTokenTestCase() {
        super();
    }

    @Test
    public final void testWithNullTokensFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken(this.text(), Cast.<List<ParserToken>>to(null));
        });
    }

    @Test
    public void testWithEmptyTokensFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), Lists.empty());
        });
    }

    @Test
    public final void testWithCopiesTokens() {
        final List<ParserToken> tokens = this.tokens();
        final String text = this.text();
        final T token = this.createToken(text, tokens);
        this.checkText(token, text);
        assertEquals(tokens, token.value(), "tokens");
        assertEquals(tokens, token.value(), "tokens not copied");
    }

    @Test
    public void testWithoutSymbolsCached() {
        final T token = this.createToken();
        assertSame(token.withoutSymbols(), token.withoutSymbols());
        assertSame(token.withoutSymbols().get().withoutSymbols(), token.withoutSymbols().get().withoutSymbols());
    }

    @Test
    public void testWithoutSymbolsDoubleSame() {
        final T token = this.createToken();
        assertSame(token.withoutSymbols().get(), token.withoutSymbols().get());
    }

    @Test
    public final void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken().setValue(null);
        });
    }

    @Test
    public final void testSetTextWhitespace() {
        final T token = this.createToken();
        final String text = " ";
        final T different = token.setText(text).cast();
        assertNotSame(token, different);
        this.checkText(different, text);
        this.checkValue(different, this.tokens());
    }

    @Test
    public final void testSetValueSame() {
        final T token = this.createToken();
        assertSame(token, token.setValue(token.value()));
    }

    @Test
    public final void testSetValueSame2() {
        final T token = this.createToken();
        assertSame(token, token.setValue(this.tokens()));
    }

    abstract T createToken(final String text, final List<ParserToken> tokens);

    @Override
    final public T createToken(final String text) {
        return this.createToken(text, this.tokens());
    }

    final T createToken(final String text, final ParserToken... tokens) {
        return this.createToken(text, Lists.of(tokens));
    }

    abstract List<ParserToken> tokens();

    final void checkValue(final SpreadsheetFormatParserToken token, final ParserToken... value) {
        this.checkValue(token, Lists.of(value));
    }

    final void checkValue(final SpreadsheetFormatParserToken token, final List<ParserToken> value) {
        this.checkValue(SpreadsheetFormatParentParserToken.class.cast(token), value);
    }

    final void checkValue(final SpreadsheetFormatParentParserToken<?> token, final ParserToken... value) {
        this.checkValue(token, Lists.of(value));
    }

    final void checkValue(final SpreadsheetFormatParentParserToken<?> token, final List<ParserToken> value) {
        assertEquals(value, token.value(),"value");
    }

    final SpreadsheetFormatParserToken closeBracket() {
        return SpreadsheetFormatParserToken.closeBracketSymbol("]", "]");
    }

    final SpreadsheetFormatParserToken openBracket() {
        return SpreadsheetFormatParserToken.openBracketSymbol("[", "[");
    }

    final SpreadsheetFormatParserToken digit() {
        return SpreadsheetFormatParserToken.digit("#", "#");
    }

    final SpreadsheetFormatParserToken fractionSymbol() {
        return SpreadsheetFormatParserToken.fractionSymbol("/", "/");
    }

    final SpreadsheetFormatConditionNumberParserToken number1() {
        return this.number(1);
    }

    final SpreadsheetFormatConditionNumberParserToken number2() {
        return this.number(2);
    }

    final SpreadsheetFormatConditionNumberParserToken number(final int value) {
        return SpreadsheetFormatParserToken.conditionNumber(BigDecimal.valueOf(value), String.valueOf(value));
    }

    final SpreadsheetFormatPercentSymbolParserToken percentSymbol() {
        return SpreadsheetFormatParserToken.percentSymbol("%", "%");
    }

    final SpreadsheetFormatWhitespaceParserToken whitespace() {
        return SpreadsheetFormatParserToken.whitespace(WHITESPACE, WHITESPACE);
    }

    final SpreadsheetFormatWhitespaceParserToken whitespace(final String text) {
        return SpreadsheetFormatParserToken.whitespace(text, text);
    }

    final SpreadsheetFormatTextLiteralParserToken text1() {
        return text(TEXT1);
    }

    final SpreadsheetFormatTextLiteralParserToken text2() {
        return text(TEXT2);
    }

    final SpreadsheetFormatTextLiteralParserToken text(final String text) {
        return SpreadsheetFormatParserToken.textLiteral(text, text);
    }
}

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

package walkingkooka.text.cursor.parser.spreadsheet;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionNode;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class SpreadsheetBinaryParserTokenTestCase2<T extends SpreadsheetBinaryParserToken<T>> extends SpreadsheetBinaryParserTokenTestCase<T> {

    @Test
    public final void testSetValueDifferent() {
        final T token = this.createToken();
        final List<ParserToken> differentValues = Lists.of(this.number(3), this.number(4));
        final T different = token.setValue(differentValues);
        this.checkValue(different, differentValues);

        assertEquals(Optional.of(different), different.withoutSymbols());
    }

    @Test
    public final void testSetValueDifferent2() {
        final T token = this.createToken();
        final List<ParserToken> differentValues = Lists.of(this.number(3), this.number(4), this.whitespace());
        final T different = token.setValue(differentValues);
        this.checkValue(different, differentValues);

        final Optional<SpreadsheetParserToken> differentWithout = different.withoutSymbols();
        assertNotEquals(Optional.of(different), differentWithout);
        this.checkValue(differentWithout.get(), differentValues.subList(0, 2));
    }

    @Test
    public final void testToExpressionNode() {
        this.toExpressionNodeAndCheck(this.expressionNode(
                ExpressionNode.bigInteger(leftBigInteger()),
                ExpressionNode.bigInteger(rightBigInteger())));
    }

    abstract ExpressionNode expressionNode(final ExpressionNode left, final ExpressionNode right);

    @Override
    final SpreadsheetParserToken leftToken() {
        return SpreadsheetParserToken.bigInteger(leftBigInteger(), "1");
    }

    private BigInteger leftBigInteger() {
        return BigInteger.valueOf(1);
    }

    @Override
    final SpreadsheetParserToken rightToken() {
        return SpreadsheetParserToken.bigInteger(rightBigInteger(), "22");
    }

    private BigInteger rightBigInteger() {
        return BigInteger.valueOf(22);
    }
}

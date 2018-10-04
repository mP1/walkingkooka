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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Optional;

/**
 * A token that contains a color declaration which may be a name or color number.
 */
public final class SpreadsheetFormatColorParserToken extends SpreadsheetFormatParentParserToken<SpreadsheetFormatColorParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetFormatColorParserToken.class);

    /**
     * Factory that creates a new {@link SpreadsheetFormatColorParserToken}.
     */
    static SpreadsheetFormatColorParserToken with(final List<ParserToken> value, final String text) {
        final List<ParserToken> copy = copyAndCheckTokensFailIfEmpty(value);

        return new SpreadsheetFormatColorParserToken(copy,
                text,
                WITHOUT_COMPUTE_REQUIRED);
    }

    /**
     * Private ctor use helper.
     */
    private SpreadsheetFormatColorParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);

        final List<SpreadsheetFormatParserToken> without = SpreadsheetFormatParentParserToken.class.cast(this.withoutSymbols().get()).value();
        final int count = without.size();
        if (1 != count) {
            throw new IllegalArgumentException("Expected 1 token but got " + count + "=" + without);
        }
        final Optional<SpreadsheetFormatParserToken> nameOrNumber = without.stream()
                .filter(t -> t.isColorName() || t.isColorNumber())
                .findFirst();
        if (!nameOrNumber.isPresent()) {
            throw new IllegalArgumentException("Color name or number missing from tokens " + value);
        }
        this.nameOrNumber = nameOrNumber.get();
    }

    public SpreadsheetFormatParserToken nameOrNumber() {
        return this.nameOrNumber;
    }

    private final SpreadsheetFormatParserToken nameOrNumber;

    @Override
    void checkText(final String text) {
        checkTextNullOrEmpty(text);
    }

    @Override
    public SpreadsheetFormatColorParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    public SpreadsheetFormatColorParserToken setValue(final List<ParserToken> values) {
        return this.setValue0(values).cast();
    }

    @Override
    SpreadsheetFormatParentParserToken replace(final List<ParserToken> tokens, final String text, final List<ParserToken> without) {
        return new SpreadsheetFormatColorParserToken(tokens, text, without);
    }

    // isXXX...........................................................................................................

    @Override
    public boolean isColor() {
        return true;
    }

    @Override
    public boolean isDate() {
        return false;
    }

    @Override
    public boolean isDateTime() {
        return false;
    }

    @Override
    public boolean isEquals() {
        return false;
    }

    @Override
    public boolean isExponent() {
        return false;
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    @Override
    public boolean isFraction() {
        return false;
    }

    @Override
    public boolean isGeneral() {
        return false;
    }

    @Override
    public boolean isGreaterThan() {
        return false;
    }

    @Override
    public boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public boolean isLessThan() {
        return false;
    }

    @Override
    public boolean isLessThanEquals() {
        return false;
    }

    @Override
    public boolean isNotEquals() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public boolean isTime() {
        return false;
    }

    @Override
    public void accept(SpreadsheetFormatParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetFormatColorParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}

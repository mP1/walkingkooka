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

import walkingkooka.Value;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Base class for any token with a single parameter.
 */
abstract class SpreadsheetUnaryParserToken extends SpreadsheetParentParserToken implements Value<List<ParserToken>> {

    static final SpreadsheetNumericParserToken NO_PARAMETER = null;

    SpreadsheetUnaryParserToken(final List<ParserToken> value, final String text, final SpreadsheetParserToken parameter, final boolean computeWithout) {
        super(value, text, computeWithout);
        this.parameter = parameter;
    }

    public final SpreadsheetParserToken parameter() {
        return this.parameter;
    }

    final SpreadsheetParserToken parameter;

    @Override
    public final boolean isAddition() {
        return false;
    }

    @Override
    public final boolean isCell() {
        return false;
    }

    @Override
    public boolean isDivision() {
        return false;
    }

    @Override
    public final boolean isFunction() {
        return false;
    }

    @Override
    public final boolean isGroup() {
        return false;
    }

    @Override
    public final boolean isMultiplication() {
        return false;
    }

    @Override
    public final boolean isPower() {
        return false;
    }

    @Override
    public final boolean isRange() {
        return false;
    }

    @Override
    public final boolean isSubtraction() {
        return false;
    }
}

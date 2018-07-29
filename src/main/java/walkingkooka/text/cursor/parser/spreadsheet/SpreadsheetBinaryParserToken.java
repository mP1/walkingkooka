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

import java.util.List;

/**
 * Base class for any token with two parameters.
 */
abstract class SpreadsheetBinaryParserToken extends SpreadsheetParentParserToken implements Value<List<SpreadsheetParserToken>> {

    final static SpreadsheetParserToken NO_PARAMETER = null;

    SpreadsheetBinaryParserToken(final List<SpreadsheetParserToken> value,
                                 final String text,
                                 final SpreadsheetParserToken left,
                                 final SpreadsheetParserToken right,
                                 final boolean computeWithout) {
        super(value, text, computeWithout);
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the left parameter.
     */
    public SpreadsheetParserToken left() {
        return this.left;
    }

    final SpreadsheetParserToken left;

    /**
     * Returns the right parameter.
     */
    public SpreadsheetParserToken right() {
        return this.right;
    }

    final SpreadsheetParserToken right;

    @Override
    public final boolean isCell() {
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
    public final boolean isNegative() {
        return false;
    }

    @Override
    public final boolean isPercentage() {
        return false;
    }
}

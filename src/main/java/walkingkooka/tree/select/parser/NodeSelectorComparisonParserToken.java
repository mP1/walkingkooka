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
package walkingkooka.tree.select.parser;

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Base class for any condition token, which includes the left side, condition symbol and right side argument.
 */
abstract public class NodeSelectorComparisonParserToken<T extends NodeSelectorComparisonParserToken> extends NodeSelectorBinaryParserToken<T> {

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorComparisonParserToken(final List<ParserToken> value,
                                      final String text,
                                      final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);
    }

    // isXXX............................................................................................................

    @Override
    public final boolean isAddition() {
        return false;
    }

    @Override
    public final boolean isAnd() {
        return false;
    }

    @Override
    public final boolean isDivision() {
        return false;
    }

    @Override
    public final boolean isModulo() {
        return false;
    }

    @Override
    public final boolean isMultiplication() {
        return false;
    }

    @Override
    public final boolean isOr() {
        return false;
    }

    @Override
    public final boolean isSubtraction() {
        return false;
    }
}

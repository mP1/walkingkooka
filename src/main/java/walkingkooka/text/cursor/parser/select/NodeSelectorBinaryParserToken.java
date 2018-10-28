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
package walkingkooka.text.cursor.parser.select;

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Base class token that includes two parameters or values.
 */
abstract class NodeSelectorBinaryParserToken<T extends NodeSelectorBinaryParserToken> extends NodeSelectorParentParserToken<T> {

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorBinaryParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);

        final List<NodeSelectorParserToken> without = NodeSelectorParentParserToken.class.cast(this.withoutSymbols().get()).value();
        final int count = without.size();
        if (2 != count) {
            throw new IllegalArgumentException("Expected 2 tokens but got " + count + "=" + without);
        }

        this.left = without.get(0);
        this.right = without.get(1);
    }

    /**
     * Returns the left parameter.
     */
    public final NodeSelectorParserToken left() {
        return this.left;
    }

    final NodeSelectorParserToken left;

    /**
     * Returns the right parameter.
     */
    public final NodeSelectorParserToken right() {
        return this.right;
    }

    final NodeSelectorParserToken right;

    // text................................................................................................

    @Override final void checkText(final String text) {
        checkTextNullOrEmpty(text);
    }

    // is................................................................................................

    @Override
    public final boolean isExpression() {
        return false;
    }

    @Override
    public final boolean isFunction() {
        return false;
    }

    @Override
    public final boolean isPredicate() {
        return false;
    }
}

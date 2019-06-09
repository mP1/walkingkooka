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
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Parser token that represents an not equals condition including parameters.
 */
public final class NodeSelectorNotEqualsParserToken extends NodeSelectorComparisonParserToken<NodeSelectorNotEqualsParserToken> {

    static NodeSelectorNotEqualsParserToken with(final List<ParserToken> value,
                                                 final String text) {
        return new NodeSelectorNotEqualsParserToken(copyAndCheckTokens(value),
                checkTextNullOrWhitespace(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private NodeSelectorNotEqualsParserToken(final List<ParserToken> value,
                                             final String text,
                                             final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);
    }

    @Override
    NodeSelectorParentParserToken replaceValue(final List<ParserToken> tokens,final List<ParserToken> without) {
        return new NodeSelectorNotEqualsParserToken(tokens, this.text(), without);
    }

    // is...............................................................................................................

    @Override
    public boolean isEquals() {
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
        return true;
    }

    // Visitor........................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorNotEqualsParserToken;
    }
}

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

package walkingkooka.tree.select;

import walkingkooka.text.cursor.parser.select.NodeSelectorParserTokenVisitorTestCase;

public final class ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitorTest extends NodeSelectorParserTokenVisitorTestCase<ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor> {
    @Override
    protected ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor createParserTokenVisitor() {
        return new ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor();
    }

    @Override
    protected String requiredNamePrefix() {
        return ExpressionNodeSelectorPredicate.class.getSimpleName();
    }

    @Override
    protected Class<ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor> parserTokenVisitorType() {
        return ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor.class;
    }
}

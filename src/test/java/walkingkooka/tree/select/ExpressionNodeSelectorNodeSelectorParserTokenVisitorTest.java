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

package walkingkooka.tree.select;


import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.ThrowableTesting;
import walkingkooka.tree.select.parser.NodeSelectorFunctionName;
import walkingkooka.tree.select.parser.NodeSelectorFunctionParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParserTokenVisitorTesting;
import walkingkooka.type.JavaVisibility;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNodeSelectorNodeSelectorParserTokenVisitorTest implements NodeSelectorParserTokenVisitorTesting<ExpressionNodeSelectorNodeSelectorParserTokenVisitor>,
        ThrowableTesting {

    @Test
    public void testUnknownFunctionFails() {
        final NodeSelectorFunctionParserToken token = NodeSelectorParserToken.function(Lists.of(NodeSelectorParserToken.functionName(NodeSelectorFunctionName.with("zyx"), "xyz"),
                NodeSelectorParserToken.number(BigDecimal.valueOf(123), "123")),
                "xyz");

        final NodeSelectorException thrown = assertThrows(NodeSelectorException.class, () -> {
            new ExpressionNodeSelectorNodeSelectorParserTokenVisitor(Predicates.never())
                    .accept(token);
        });
        checkMessage(thrown, "Unknown function \"zyx\" in \"xyz\"");
    }

    @Override
    public ExpressionNodeSelectorNodeSelectorParserTokenVisitor createVisitor() {
        return new ExpressionNodeSelectorNodeSelectorParserTokenVisitor(null);
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNodeSelector.class.getSimpleName();
    }

    @Override
    public Class<ExpressionNodeSelectorNodeSelectorParserTokenVisitor> type() {
        return ExpressionNodeSelectorNodeSelectorParserTokenVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}

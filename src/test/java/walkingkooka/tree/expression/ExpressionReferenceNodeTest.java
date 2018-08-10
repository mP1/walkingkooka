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

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetCell;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetColumn;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetReferenceKind;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetRow;
import walkingkooka.tree.visit.Visiting;

public final class ExpressionReferenceNodeTest extends ExpressionLeafValueNodeTestCase<ExpressionReferenceNode, ExpressionReference>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionReferenceNode node = this.createExpressionNode();

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final ExpressionReferenceNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("$B$3", this.createExpressionNode().toString());
    }

    @Override
    ExpressionReferenceNode createExpressionNode(final ExpressionReference value) {
        return ExpressionReferenceNode.with(value);
    }

    @Override
    ExpressionReference value() {
        return cell(1, 2);
    }

    @Override
    ExpressionReference differentValue() {
        return cell(30, 40);
    }

    private SpreadsheetCell cell(final int column, final int row) {
        return SpreadsheetCell.with(SpreadsheetColumn.with(column, SpreadsheetReferenceKind.ABSOLUTE), SpreadsheetRow.with(row, SpreadsheetReferenceKind.ABSOLUTE));
    }

    @Override
    Class<ExpressionReferenceNode> expressionNodeType() {
        return ExpressionReferenceNode.class;
    }
}

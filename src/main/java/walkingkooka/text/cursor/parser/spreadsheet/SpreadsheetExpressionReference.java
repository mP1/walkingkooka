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

import walkingkooka.compare.Comparators;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Comparator;

/**
 * Base class for all Spreadsheet {@link ExpressionReference}
 */
abstract public class SpreadsheetExpressionReference implements ExpressionReference, HashCodeEqualsDefined {

    /**
     * A comparator that orders {@link SpreadsheetLabelName} before {@link SpreadsheetCellReference}.
     */
    public final static Comparator<SpreadsheetExpressionReference> COMPARATOR = SpreadsheetExpressionReferenceComparator.INSTANCE;

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetExpressionReference() {
        super();
    }

    /**
     * Invoked by {@link SpreadsheetExpressionReferenceComparator} using double dispatch
     * to compare two {@link SpreadsheetExpressionReference}. Each sub class will use double dispatch which will invoke
     * either of the #compare0 methods. saving the need for instanceof checks.
     */
    abstract int compare(final SpreadsheetExpressionReference other);

    abstract int compare0(final SpreadsheetCellReference other);

    abstract int compare0(final SpreadsheetLabelName other);

    /**
     * Labels come before references, used as the result when a label compares with a reference.
     */
    final static int LABEL_COMPARED_WITH_CELL_RESULT = Comparators.LESS;
}

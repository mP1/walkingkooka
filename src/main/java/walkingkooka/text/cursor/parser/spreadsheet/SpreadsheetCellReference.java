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

import walkingkooka.Cast;
import walkingkooka.compare.Comparators;
import walkingkooka.compare.LowerOrUpper;

import java.util.Objects;

/**
 * A reference that includes a defined name or column and row.
 */
public final class SpreadsheetCellReference extends SpreadsheetExpressionReference
        implements Comparable<SpreadsheetCellReference>,
                   LowerOrUpper<SpreadsheetCellReference> {

    public static SpreadsheetCellReference with(final SpreadsheetColumnReference column, final SpreadsheetRowReference row){
        checkColumn(column);
        checkRow(row);

        return new SpreadsheetCellReference(column, row);
    }

    private SpreadsheetCellReference(final SpreadsheetColumnReference column, final SpreadsheetRowReference row){
        super();
        this.column = column;
        this.row = row;
    }

    /**
     * Adds a delta to the row and column and returns a {@link SpreadsheetCellReference} with the updated values.
     * Row and column values of 0 and 0 will return this.
     */
    public SpreadsheetCellReference add(final int column, final int row) {
        return this.setColumn(this.column().add(column))
                .setRow(this.row().add(row));
    }

    public SpreadsheetRowReference row() {
        return this.row;
    }

    public SpreadsheetCellReference setRow(final SpreadsheetRowReference row) {
        checkRow(row);
        return this.row.equals(row) ?
                this :
                this.replace(this.column, row);
    }

    private final SpreadsheetRowReference row;

    private static void checkRow(final SpreadsheetRowReference row) {
        Objects.requireNonNull(row, "row");
    }

    public SpreadsheetColumnReference column() {
        return this.column;
    }

    public SpreadsheetCellReference setColumn(final SpreadsheetColumnReference column) {
        checkColumn(column);
        return this.column.equals(column) ?
               this :
               this.replace(column, this.row);
    }

    private final SpreadsheetColumnReference column;
    
    private static void checkColumn(final SpreadsheetColumnReference column) {
        Objects.requireNonNull(column, "column");
    }

    private SpreadsheetCellReference replace(final SpreadsheetColumnReference column, final SpreadsheetRowReference row) {
        return new SpreadsheetCellReference(column, row);
    }

    /**
     * Returns the lower (Comparison wise) of two {@link SpreadsheetCellReference}
     */
    public SpreadsheetCellReference lower(final SpreadsheetCellReference other) {
        checkOther(other);

        return this.setColumn(this.column.lower(other.column))
               .setRow(this.row.lower(other.row));
    }

    /**
     * Returns the upper (Comparison wise) of two {@link SpreadsheetCellReference}
     */
    public SpreadsheetCellReference upper(final SpreadsheetCellReference other) {
        checkOther(other);

        return this.setColumn(this.column.upper(other.column))
                .setRow(this.row.upper(other.row));
    }

    private static void checkOther(final SpreadsheetCellReference other) {
        Objects.requireNonNull(other, "other");
    }

    // HashCodeEqualsDefined................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.column, this.row);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof SpreadsheetCellReference &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final SpreadsheetCellReference other) {
        return this.column.equals(other.column) &&
               this.row.equals(other.row);
    }

    public String toString() {
        return "" + this.column + this.row;
    }

    // Comparable ..................................................................................................

    @Override
    public int compareTo(final SpreadsheetCellReference other) {
        // reverse sign because #compare0 does compare in reverse because of double dispatch.
        return - this.compare0(other);
    }

    // SpreadsheetExpressionReferenceComparator........................................................................

    @Override
    final int compare(final SpreadsheetExpressionReference other) {
        return other.compare0(this);
    }

    @Override
    final int compare0(final SpreadsheetCellReference other) {
        final int result = other.column.value - this.column.value;
        return Comparators.EQUAL != result ?
               result :
               other.row.value - this.row.value;
    }

    @Override
    final int compare0(final SpreadsheetLabelName other) {
        return LABEL_COMPARED_WITH_CELL_RESULT;
    }
}

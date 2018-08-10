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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Objects;

/**
 * A reference that includes a defined name or column and row.
 */
public final class SpreadsheetCell implements HashCodeEqualsDefined, ExpressionReference {

    public static SpreadsheetCell with(final SpreadsheetColumn column, final SpreadsheetRow row){
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(row, "row");

        return new SpreadsheetCell(column, row);
    }

    private SpreadsheetCell(final SpreadsheetColumn column, final SpreadsheetRow row){
        super();
        this.column = column;
        this.row = row;
    }

    public SpreadsheetRow row() {
        return this.row;
    }

    private final SpreadsheetRow row;

    public SpreadsheetColumn column() {
        return this.column;
    }

    private final SpreadsheetColumn column;

    @Override
    public int hashCode() {
        return Objects.hash(this.column, this.row);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof SpreadsheetCell &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final SpreadsheetCell other) {
        return this.column.equals(other.column) &&
               this.row.equals(other.row);
    }

    public String toString() {
        return "" + this.column + this.row;
    }
}

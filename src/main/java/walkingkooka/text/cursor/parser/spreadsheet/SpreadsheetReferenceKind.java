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

/**
 * Used to note whether a column or row reference is absolute or relative.
 */
public enum SpreadsheetReferenceKind {

    ABSOLUTE {

        @Override
        public SpreadsheetColumn column(final int value) {
            return SpreadsheetColumn.with(value, this);
        }

        @Override
        public SpreadsheetRow row(final int value){
            return SpreadsheetRow.with(value, this);
        }

        @Override
        String prefix(){
            return "$";
        }
    },
    RELATIVE {

        @Override
        public SpreadsheetColumn column(final int value) {
            return SpreadsheetColumn.with(value, this);
        }

        @Override
        public SpreadsheetRow row(final int value){
            return SpreadsheetRow.with(value, this);
        }

        @Override
        String prefix(){
            return "";
        }
    };

    public abstract SpreadsheetColumn column(final int column);

    public abstract SpreadsheetRow row(final int row);

    // only called by {@link SpreadsheetRow#toString()} or {@link SpreadsheetColumn#toString()}
    abstract String prefix();
}

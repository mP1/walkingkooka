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

import org.junit.Test;
import walkingkooka.compare.LowerOrUpperTestCase;

public final class SpreadsheetColumnReferenceComparableTest extends LowerOrUpperTestCase<SpreadsheetColumnReference> {

    private final static int COLUMN = 123;

    @Test
    public void testEqualReferenceKindIgnored() {
        this.compareToAndCheckEqual(SpreadsheetReferenceKind.RELATIVE.column(COLUMN));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(SpreadsheetReferenceKind.ABSOLUTE.column(COLUMN + 999));
    }

    @Test
    public void testLess2() {
        this.compareToAndCheckLess(SpreadsheetReferenceKind.RELATIVE.column(COLUMN + 999));
    }

    @Override
    protected SpreadsheetColumnReference createLowerOrUpper() {
        return SpreadsheetReferenceKind.ABSOLUTE.column(COLUMN);
    }

    @Override
    protected boolean compareAndEqualsMatch() {
        return false;
    }
}

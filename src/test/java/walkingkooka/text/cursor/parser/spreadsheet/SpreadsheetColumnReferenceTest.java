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

public final class SpreadsheetColumnReferenceTest extends SpreadsheetColumnOrRowReferenceTestCase<SpreadsheetColumnReference> {

    @Test
    public void testToStringRelative() {
        this.checkToString(0, SpreadsheetReferenceKind.RELATIVE, "A");
    }

    @Test
    public void testToStringRelative2() {
        this.checkToString(25, SpreadsheetReferenceKind.RELATIVE, "Z");
    }

    @Test
    public void testToStringRelative3() {
        this.checkToString((1 * 26) + 0, SpreadsheetReferenceKind.RELATIVE, "AA");
    }

    @Test
    public void testToStringRelative4() {
        this.checkToString((1 * 26) + 3, SpreadsheetReferenceKind.RELATIVE, "AD");
    }

    @Test
    public void testToStringRelative5() {
        this.checkToString((1 * 26 * 26) + (25 * 26) + 12, SpreadsheetReferenceKind.RELATIVE, "AYM");
    }

    @Test
    public void testToStringAbsolute() {
        this.checkToString(0, SpreadsheetReferenceKind.ABSOLUTE, "$A");
    }

    @Test
    public void testToStringAbsolute2() {
        this.checkToString(2, SpreadsheetReferenceKind.ABSOLUTE, "$C");
    }

    @Override
    SpreadsheetColumnReference create(final int value, final SpreadsheetReferenceKind kind) {
        return SpreadsheetColumnReference.with(value, kind);
    }

    @Override
    int maxValue() {
        return SpreadsheetColumnReference.MAX;
    }

    @Override
    protected Class<SpreadsheetColumnReference> type() {
        return SpreadsheetColumnReference.class;
    }
}

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
import walkingkooka.naming.NameTesting;
import walkingkooka.naming.PropertiesPath;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

final public class SpreadsheetLabelNameTest extends SpreadsheetExpressionReferenceTestCase<SpreadsheetLabelName>
        implements NameTesting<SpreadsheetLabelName, SpreadsheetLabelName> {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContainsSeparatorFails() {
        SpreadsheetLabelName.with("xyz" + PropertiesPath.SEPARATOR.string());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        SpreadsheetLabelName.with("1abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        SpreadsheetLabelName.with("abc$def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellReferenceFails() {
        SpreadsheetLabelName.with("A1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellReferenceFails2() {
        SpreadsheetLabelName.with("AB12");
    }

    @Test//(expected = IllegalArgumentException.class)
    public void testCellReferenceFails3() {
        SpreadsheetLabelName.with(SpreadsheetColumnReference.MAX_ROW_NAME + "1");
    }

    @Test
    public void testWith2() {
        this.createNameAndCheck("ZZZ1");
    }

    @Test
    public void testWith3() {
        this.createNameAndCheck("A123Hello");
    }

    @Test
    public void testWith4() {
        this.createNameAndCheck("A1B2C2");
    }

    @Test
    public void testWithMissingRow() {
        this.createNameAndCheck("A");
    }

    @Test
    public void testWithMissingRow2() {
        this.createNameAndCheck("ABC");
    }

    @Test
    public void testWithEnormousColumn() {
        this.createNameAndCheck("ABCDEF1");
    }

    @Test
    public void testWithEnormousColumn2() {
        this.createNameAndCheck("ABCDEF");
    }

    @Test
    public void testWithEnormousRow() {
        this.createNameAndCheck("A" + (SpreadsheetRowReference.MAX + 1));
    }

    @Test
    public void testToString() {
        assertEquals("ABC_123", this.createName("ABC_123").toString());
    }

    @Override
    public SpreadsheetLabelName createName(final String name) {
        return SpreadsheetLabelName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return "state";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "postcode";
    }

    @Override
    protected Class<SpreadsheetLabelName> type() {
        return SpreadsheetLabelName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

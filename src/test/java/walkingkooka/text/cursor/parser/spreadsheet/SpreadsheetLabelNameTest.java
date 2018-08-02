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
import walkingkooka.naming.NameTestCase;
import walkingkooka.naming.PropertiesPath;

final public class SpreadsheetLabelNameTest extends NameTestCase<SpreadsheetLabelName> {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyStringFails() {
        SpreadsheetLabelName.with("");
    }

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
        SpreadsheetLabelName.with(SpreadsheetColumn.MAX_ROW_NAME + "1");
    }

    @Test
    public void testWith() {
        this.createAndCheck("Abc_123");
    }

    @Test
    public void testWith2() {
        this.createAndCheck("ZZZ1");
    }

    @Test
    public void testWith3() {
        this.createAndCheck("A" + (SpreadsheetRow.MAX + 1));
    }

    @Test
    public void testWith4() {
        this.createAndCheck("A123Hello");
    }

    @Test
    public void testWith5() {
        this.createAndCheck("A1B2C2");
    }

    private void createAndCheck(final String text) {
        final SpreadsheetLabelName name = this.createName(text);
        assertEquals("value", text, name.value());
    }

    @Test
    public void testToString() {
        assertEquals("ABC_123", this.createName("ABC_123").toString());
    }

    @Override
    protected SpreadsheetLabelName createName(final String name) {
        return SpreadsheetLabelName.with(name);
    }

    @Override
    protected Class<SpreadsheetLabelName> type() {
        return SpreadsheetLabelName.class;
    }
}

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

package walkingkooka.text.spreadsheetformat;

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SpreadsheetTextFormatContextSignTest extends PublicClassTestCase<SpreadsheetTextFormatContextSign> {

    @Test
    public void testFromSignNumPositiveOne() {
        fromSignumAndCheck(1, SpreadsheetTextFormatContextSign.POSITIVE);
    }

    @Test
    public void testFromSignNumPositive() {
        fromSignumAndCheck(100, SpreadsheetTextFormatContextSign.POSITIVE);
    }

    @Test
    public void testFromSignNumNegativeOne() {
        fromSignumAndCheck(-1, SpreadsheetTextFormatContextSign.NEGATIVE);
    }

    @Test
    public void testFromSignNumNegative() {
        fromSignumAndCheck(-100, SpreadsheetTextFormatContextSign.NEGATIVE);
    }

    @Test
    public void testFromSignNumZero() {
        fromSignumAndCheck(0, SpreadsheetTextFormatContextSign.ZERO);
    }

    private void fromSignumAndCheck(final int signum, final SpreadsheetTextFormatContextSign sign) {
        assertSame("for " + signum, sign, SpreadsheetTextFormatContextSign.fromSignum(signum));
    }

    @Test
    public void testNegativePattern() {
        this.checkPattern(SpreadsheetTextFormatContextSign.NEGATIVE, "-");
    }

    @Test
    public void testZeroPattern() {
        this.checkPattern(SpreadsheetTextFormatContextSign.ZERO, "0");
    }

    @Test
    public void testPositivePattern() {
        this.checkPattern(SpreadsheetTextFormatContextSign.POSITIVE, "+");
    }

    private void checkPattern(final SpreadsheetTextFormatContextSign sign, final String pattern) {
        assertEquals(sign.toString(), pattern, sign.symbol());
    }

    @Override
    protected Class<SpreadsheetTextFormatContextSign> type() {
        return SpreadsheetTextFormatContextSign.class;
    }
}

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
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class BigDecimalSpreadsheetTextFormatterZeroTest extends ClassTestCase<BigDecimalSpreadsheetTextFormatterZero> {

    @Test
    public void testHashPattern() {
        checkPattern(BigDecimalSpreadsheetTextFormatterZero.HASH, "#");
    }

    @Test
    public void testQuestionPattern() {
        checkPattern(BigDecimalSpreadsheetTextFormatterZero.QUESTION_MARK, "?");
    }

    @Test
    public void testZeroPattern() {
        checkPattern(BigDecimalSpreadsheetTextFormatterZero.ZERO, "0");
    }

    private void checkPattern(final BigDecimalSpreadsheetTextFormatterZero zero, final String pattern) {
        assertEquals(zero.toString(), pattern, zero.pattern());
    }

    @Override
    protected Class<BigDecimalSpreadsheetTextFormatterZero> type() {
        return BigDecimalSpreadsheetTextFormatterZero.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}

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
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

final public class SpreadsheetFunctionNameTest extends ClassTestCase<SpreadsheetFunctionName>
        implements NameTesting<SpreadsheetFunctionName, SpreadsheetFunctionName> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        SpreadsheetFunctionName.with("1abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        SpreadsheetFunctionName.with("abc$def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLengthFails() {
        final char[] c = new char[ SpreadsheetFunctionName.MAX_LENGTH + 1];
        Arrays.fill(c, 'a');

        SpreadsheetFunctionName.with(new String(c));
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Abc.123");
    }

    @Test
    public void testToString() {
        assertEquals("ABC.123", this.createName("ABC.123").toString());
    }

    @Override
    public SpreadsheetFunctionName createName(final String name) {
        return SpreadsheetFunctionName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "sin";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "abs";
    }

    @Override
    protected Class<SpreadsheetFunctionName> type() {
        return SpreadsheetFunctionName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

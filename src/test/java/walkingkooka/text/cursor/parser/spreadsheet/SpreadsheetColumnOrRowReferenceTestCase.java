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
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;

public abstract class SpreadsheetColumnOrRowReferenceTestCase<V extends SpreadsheetColumnOrRowReference> extends PublicClassTestCase<V> {

    @Test(expected = IllegalArgumentException.class)
    public final void testWithNegativeValueFails() {
        this.create(-1, SpreadsheetReferenceKind.RELATIVE);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithInvalidValueFails() {
        this.create(this.maxValue(), SpreadsheetReferenceKind.RELATIVE);
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullKindFails() {
        this.create(0, null);
    }

    abstract V create(final int value, final SpreadsheetReferenceKind kind);

    final void checkToString(final int value, final SpreadsheetReferenceKind kind, final String toString) {
        assertEquals(toString, this.create(value, kind).toString());
    }

    abstract int maxValue();
}

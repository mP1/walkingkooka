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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public abstract class SpreadsheetColumnOrRowReferenceTestCase<V extends SpreadsheetColumnOrRowReference> extends PublicClassTestCase<V> {

    final static int VALUE = 123;
    final static SpreadsheetReferenceKind REFERENCE_KIND = SpreadsheetReferenceKind.ABSOLUTE;

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

    @Test(expected = IllegalArgumentException.class)
    public final void testSetValueInvalidFails() {
        this.create().setValue(-1);
    }

    @Test
    public final void testSetValueSame() {
        final V reference = this.create();
        assertSame(reference, reference.setValue(VALUE));
    }

    @Test
    public final void testSetValueDifferent() {
        final V reference = this.create();
        final Integer differentValue = 999;
        final SpreadsheetColumnOrRowReference different = reference.setValue(differentValue);
        assertNotSame(reference, different);
        assertEquals("value", differentValue, different.value());
        assertSame("referenceKind", REFERENCE_KIND, different.referenceKind());
    }

    @Test
    public final void testSetValueDifferent2() {
        final SpreadsheetReferenceKind kind = SpreadsheetReferenceKind.RELATIVE;
        final V reference = this.create(VALUE, kind);
        final Integer differentValue = 999;
        final SpreadsheetColumnOrRowReference different = reference.setValue(differentValue);
        assertNotSame(reference, different);
        assertEquals("value", differentValue, different.value());
        assertSame("referenceKind", kind, different.referenceKind());
        assertEquals("same type", this.type(), different.getClass());
    }

    final V create() {
        return this.create(VALUE, REFERENCE_KIND);
    }

    abstract V create(final int value, final SpreadsheetReferenceKind kind);

    final void checkToString(final int value, final SpreadsheetReferenceKind kind, final String toString) {
        assertEquals(toString, this.create(value, kind).toString());
    }

    abstract int maxValue();
}

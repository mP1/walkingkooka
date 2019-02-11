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
 */

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineEndingTest extends ClassTestCase<LineEnding>
        implements CharSequenceTesting<LineEnding>,
        SerializationTesting<LineEnding> {

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testCr() {
        assertEquals("\r", LineEnding.CR.toString());
    }

    @Test
    public void testCrNl() {
        assertEquals("\r\n", LineEnding.CRNL.toString());
    }

    @Test
    public void testNl() {
        assertEquals("\n", LineEnding.NL.toString());
    }

    @Test
    public void testNone() {
        assertEquals("", LineEnding.NONE.toString());
    }

    @Test
    public void testSystem() {
        assertNotSame(LineEnding.SYSTEM, LineEnding.NONE);
    }

    @Test
    public void testFromNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LineEnding.from(null);
        });
    }

    @Test
    public void testFromUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LineEnding.from("UNKNOWN LINE ENDING");
        });
    }

    @Test
    public void testFromCr() {
        assertSame(LineEnding.CR, LineEnding.from("\r"));
    }

    @Test
    public void testFromCrNl() {
        assertSame(LineEnding.CRNL, LineEnding.from("\r\n"));
    }

    @Test
    public void testFromNl() {
        assertSame(LineEnding.NL, LineEnding.from("\n"));
    }

    @Test
    public void testFromEmpty() {
        assertSame(LineEnding.NONE, LineEnding.from(""));
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(LineEnding.NL);
    }

    @Override
    public LineEnding createCharSequence() {
        return LineEnding.CR;
    }

    @Override
    public Class<LineEnding> type() {
        return LineEnding.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public LineEnding createObject() {
        return this.createCharSequence();
    }

    @Override
    public LineEnding serializableInstance() {
        return LineEnding.CR;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}

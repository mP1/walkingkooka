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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class LineEndingTest extends CharSequenceTestCase<LineEnding> {

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
        Assert.assertNotSame(LineEnding.SYSTEM, LineEnding.NONE);
    }

    @Test
    public void testFromNullFails() {
        this.fromFails(null);
    }

    @Test
    public void testFromUnknownFails() {
        this.fromFails("UNKNOWN LINE ENDING");
    }

    private void fromFails(final String lineEnding) {
        try {
            LineEnding.from(lineEnding);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
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

    @Override
    protected LineEnding createCharSequence() {
        return LineEnding.CR;
    }

    @Override
    protected Class<LineEnding> type() {
        return LineEnding.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

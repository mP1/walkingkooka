/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineEndingTest implements ClassTesting2<LineEnding>,
    CharSequenceTesting<LineEnding> {

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testCr() {
        this.checkEquals("\r", LineEnding.CR.toString());
    }

    @Test
    public void testCrNl() {
        this.checkEquals("\r\n", LineEnding.CRNL.toString());
    }

    @Test
    public void testNl() {
        this.checkEquals("\n", LineEnding.NL.toString());
    }

    @Test
    public void testNone() {
        this.checkEquals("", LineEnding.NONE.toString());
    }

    @Test
    public void testSystem() {
        assertNotSame(LineEnding.SYSTEM, LineEnding.NONE);
    }

    @Test
    public void testFromNullFails() {
        assertThrows(NullPointerException.class, () -> LineEnding.from(null));
    }

    @Test
    public void testFromUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> LineEnding.from("UNKNOWN LINE ENDING"));
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
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public LineEnding createObject() {
        return this.createCharSequence();
    }
}

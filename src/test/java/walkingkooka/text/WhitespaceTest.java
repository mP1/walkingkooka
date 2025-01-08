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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class WhitespaceTest implements ClassTesting2<Whitespace>,
    CharSequenceTesting<Whitespace> {

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testNullFails() {
        assertThrows(NullPointerException.class, () -> Whitespace.with(null));
    }

    @Test
    public void testIncludesNonWhitespaceCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> Whitespace.with(" !"));
    }

    @Test
    public void testEmptyString() {
        assertSame(Whitespace.EMPTY, Whitespace.with(""));
    }

    @Test
    public void testWith() {
        final String content = "   ";
        final Whitespace whitespace = Whitespace.with(content);
        assertSame(content, whitespace.toString(), "toString");
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(" \t\r\n");
    }

    @Test
    public void testSubSequence() {
        this.checkEquals(Whitespace.with("\t"), Whitespace.with(" \t\r ").subSequence(1, 2));
    }

    @Override
    @Test
    public void testEmptySubSequence() {
        final Whitespace whitespace = this.createCharSequence();
        assertSame(Whitespace.EMPTY, whitespace.subSequence(2, 2));
    }

    // has.........................................

    @Test
    public void testHasNoWhitespace() {
        hasAndCheck("abc", false);
    }

    @Test
    public void testHasNull() {
        hasAndCheck(null, false);
    }

    @Test
    public void testHasEmpty() {
        hasAndCheck("", false);
    }

    @Test
    public void testHasWith() {
        hasAndCheck("with white space", true);
    }

    @Test
    public void testHasWithout() {
        hasAndCheck("without", false);
    }

    private void hasAndCheck(final String text, final boolean has) {
        this.checkEquals(has,
            Whitespace.has(text),
            CharSequences.quoteAndEscape(text) + " has whitespace");
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(Whitespace.with("\t"));
    }

    @Test
    public void testEqualsDifferentSameLength() {
        this.checkNotEquals(Whitespace.with("\t\t\t"));
    }

    @Override
    public Whitespace createCharSequence() {
        return Whitespace.with(" \t\r\n");
    }

    @Override
    public Class<Whitespace> type() {
        return Whitespace.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Whitespace createObject() {
        return this.createCharSequence();
    }
}

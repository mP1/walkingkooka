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
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class CharacterConstantTest extends CharSequenceTestCase<CharacterConstant> {

    private final static char CHAR = 'a';

    @Test
    public void testWith() {
        final char c = 0x200;
        this.check(CharacterConstant.with(c), c);
    }

    @Test
    public void testCachedLowerBounds() {
        this.checkCached(CharacterConstant.LOWER_BOUNDS);
    }

    @Test
    public void testCachedBetweenBounds() {
        this.checkCached((char) ((CharacterConstant.LOWER_BOUNDS + CharacterConstant.UPPER_BOUNDS)
                / 2));
    }

    @Test
    public void testCachedUpperBounds() {
        this.checkCached(CharacterConstant.UPPER_BOUNDS);
    }

    private void checkCached(final char c) {
        final CharacterConstant constant = CharacterConstant.with(c);
        assertSame(constant, CharacterConstant.with(c), "not cached");

        this.check(constant, c);
    }

    private void check(final CharacterConstant constant, final char c) {
        assertEquals( c, constant.character(), "character");
        assertEquals(String.valueOf(c), constant.string(), "string");
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(CharacterConstant.with((char) (CHAR
                + 1)));
    }

    @Test
    public void testSameCharacter() {
        assertTrue(this.createObject().equals(CHAR));
    }

    @Test
    public void testSameCharacter2() {
        assertTrue(this.createObject().equals((Object) CHAR));
    }

    @Test
    public void testSameString() {
        assertTrue(this.createObject().equals("" + CHAR));
    }

    @Test
    public void testSameString2() {
        assertTrue(this.createObject()
                .equals("" + CHAR));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharacterConstant.with('a'), "a");
    }

    @Override
    protected CharacterConstant createCharSequence() {
        return CharacterConstant.with(CHAR);
    }

    @Override
    public Class<CharacterConstant> type() {
        return CharacterConstant.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

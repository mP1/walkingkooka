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

package walkingkooka.naming;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class PathSeparatorTest extends ClassTestCase<PathSeparator> {

    // constants

    private final static char SEPARATOR = '/';
    private final static boolean REQUIRED = true;

    // tests

    @Test
    public void testRequiredControlSeparatorCharacterFails() {
        this.requiredFails('\n');
    }

    @Test
    public void testRequiredLetterFails() {
        this.requiredFails('A');
    }

    @Test
    public void testRequiredDigitFails() {
        this.requiredFails('1');
    }

    private void requiredFails(final char character) {
        try {
            PathSeparator.requiredAtStart(character);
            Assert.fail();
        } catch (final RuntimeException expected) {
            assertEquals(PathSeparator.invalidCharacter(character), expected.getMessage());
        }
    }

    @Test
    public void testRequiredAtStart() {
        final PathSeparator separator = PathSeparator.requiredAtStart(PathSeparatorTest.SEPARATOR);
        assertEquals("character", PathSeparatorTest.SEPARATOR, separator.character());
        assertEquals("string", String.valueOf(PathSeparatorTest.SEPARATOR), separator.string());
        assertEquals("requiredAtStart", PathSeparatorTest.REQUIRED, separator.isRequiredAtStart());
    }

    @Test
    public void testRequiredSlashSingleton() {
        final char c = '/';
        final PathSeparator separator = PathSeparator.requiredAtStart(c);
        assertSame(separator, PathSeparator.requiredAtStart(c));
        assertEquals("character", c, separator.character());
        assertEquals("string", String.valueOf(c), separator.string());
        assertEquals("requiredAtStart", PathSeparatorTest.REQUIRED, separator.isRequiredAtStart());
    }

    @Test
    public void testNotRequiredControlSeparatorCharacterFails() {
        this.notRequiredFails('\n');
    }

    @Test
    public void testNotRequiredLetterFails() {
        this.notRequiredFails('A');
    }

    @Test
    public void testNotRequiredDigitFails() {
        this.notRequiredFails('1');
    }

    private void notRequiredFails(final char character) {
        try {
            PathSeparator.notRequiredAtStart(character);
            Assert.fail();
        } catch (final RuntimeException expected) {
            assertEquals(PathSeparator.invalidCharacter(character), expected.getMessage());
        }
    }

    @Test
    public void testNotRequiredAtStart() {
        final PathSeparator separator = PathSeparator.notRequiredAtStart(PathSeparatorTest.SEPARATOR);
        assertEquals("character", PathSeparatorTest.SEPARATOR, separator.character());
        assertEquals("string", String.valueOf(PathSeparatorTest.SEPARATOR), separator.string());
        assertEquals("requiredAtStart", false, separator.isRequiredAtStart());
    }

    @Test
    public void testNotRequiredDotSingleton() {
        final char c = '.';
        final PathSeparator separator = PathSeparator.notRequiredAtStart(c);
        assertSame(separator, PathSeparator.notRequiredAtStart(c));
        assertEquals("character", c, separator.character());
        assertEquals("string", String.valueOf(c), separator.string());
        assertEquals("requiredAtStart", false, separator.isRequiredAtStart());
    }

    @Test
    public void testToString() {
        assertEquals(String.valueOf(PathSeparatorTest.SEPARATOR),
                PathSeparator.requiredAtStart(PathSeparatorTest.SEPARATOR).toString());
    }

    @Override
    protected Class<PathSeparator> type() {
        return PathSeparator.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

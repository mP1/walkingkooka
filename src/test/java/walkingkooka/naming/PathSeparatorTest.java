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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PathSeparatorTest implements ClassTesting2<PathSeparator>,
    HashCodeEqualsDefinedTesting2<PathSeparator>,
    ThrowableTesting,
    ToStringTesting<PathSeparator> {

    // constants

    private final static char SEPARATOR = '/';
    private final static boolean REQUIRED = true;

    // tests

    @Test
    public void testRequiredControlSeparatorCharacterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> PathSeparator.requiredAtStart('\n'));
        checkMessage(expected, PathSeparator.invalidCharacter('\n'));
    }

    @Test
    public void testRequiredLetterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> PathSeparator.requiredAtStart('A'));
        this.checkEquals(PathSeparator.invalidCharacter('A'), expected.getMessage());
    }

    @Test
    public void testRequiredDigitFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> PathSeparator.requiredAtStart('1'));
        this.checkEquals(PathSeparator.invalidCharacter('1'), expected.getMessage());
    }

    @Test
    public void testRequiredSlashAtStart() {
        this.check(PathSeparator.requiredAtStart(SEPARATOR), SEPARATOR, REQUIRED);
    }

    @Test
    public void testRequiredBackslashAtStart() {
        final char c = '\\';
        this.check(PathSeparator.requiredAtStart(c), c, REQUIRED);
    }

    @Test
    public void testRequiredSlashSingleton() {
        final char c = '/';
        final PathSeparator separator = PathSeparator.requiredAtStart(c);
        assertSame(separator, PathSeparator.requiredAtStart(c));
        this.check(separator, c, REQUIRED);
    }

    private void check(final PathSeparator separator,
                       final char c,
                       final boolean required) {
        this.checkEquals(c, separator.character(), "character");
        this.checkEquals(String.valueOf(c), separator.string(), "string");
        this.checkEquals(required, separator.isRequiredAtStart(), "requiredAtStart");
    }

    @Test
    public void testNotRequiredControlSeparatorCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> PathSeparator.notRequiredAtStart('\n'));
    }

    @Test
    public void testNotRequiredLetterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> PathSeparator.notRequiredAtStart('A'));
        this.checkEquals(PathSeparator.invalidCharacter('A'), expected.getMessage());
    }

    @Test
    public void testNotRequiredDigitFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> PathSeparator.notRequiredAtStart('1'));
        this.checkEquals(PathSeparator.invalidCharacter('1'), expected.getMessage());
    }

    @Test
    public void testNotRequiredAtStart() {
        this.check(PathSeparator.notRequiredAtStart(SEPARATOR), SEPARATOR, false);
    }

    @Test
    public void testNotRequiredDotSingleton() {
        this.check(PathSeparator.notRequiredAtStart('.'), '.', false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentSeparatorCharacter() {
        this.checkNotEquals(PathSeparator.requiredAtStart('.'));
    }

    @Test
    public void testEqualsDifferentRequiredAtStart() {
        this.checkNotEquals(PathSeparator.notRequiredAtStart(SEPARATOR));
    }

    // toString ..................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(PathSeparator.requiredAtStart(SEPARATOR), String.valueOf(SEPARATOR));
    }

    @Override
    public Class<PathSeparator> type() {
        return PathSeparator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public PathSeparator createObject() {
        return PathSeparator.requiredAtStart(SEPARATOR);
    }
}

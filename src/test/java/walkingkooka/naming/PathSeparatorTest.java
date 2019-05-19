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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PathSeparatorTest implements ClassTesting2<PathSeparator>,
        HashCodeEqualsDefinedTesting<PathSeparator>,
        SerializationTesting<PathSeparator>,
        ToStringTesting<PathSeparator> {

    // constants

    private final static char SEPARATOR = '/';
    private final static boolean REQUIRED = true;

    // tests

    @Test
    public void testRequiredControlSeparatorCharacterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.requiredAtStart('\n');
        });
        assertEquals(PathSeparator.invalidCharacter('\n'), expected.getMessage());
    }

    @Test
    public void testRequiredLetterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.requiredAtStart('A');
        });
        assertEquals(PathSeparator.invalidCharacter('A'), expected.getMessage());
    }

    @Test
    public void testRequiredDigitFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.requiredAtStart('1');
        });
        assertEquals(PathSeparator.invalidCharacter('1'), expected.getMessage());
    }

    @Test
    public void testRequiredAtStart() {
        this.check(PathSeparator.requiredAtStart(SEPARATOR), SEPARATOR, REQUIRED);
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
        assertEquals(c, separator.character(), "character");
        assertEquals(String.valueOf(c), separator.string(), "string");
        assertEquals(required, separator.isRequiredAtStart(), "requiredAtStart");
    }

    @Test
    public void testNotRequiredControlSeparatorCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.notRequiredAtStart('\n');
        });
    }

    @Test
    public void testNotRequiredLetterFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.notRequiredAtStart('A');
        });
        assertEquals(PathSeparator.invalidCharacter('A'), expected.getMessage());
    }

    @Test
    public void testNotRequiredDigitFails() {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            PathSeparator.notRequiredAtStart('1');
        });
        assertEquals(PathSeparator.invalidCharacter('1'), expected.getMessage());
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

    @Test
    public void testRequiredSlashSingletoness() throws Exception {
        this.serializeSingletonAndCheck(PathSeparator.requiredAtStart('/'));
    }

    @Test
    public void testNotRequiredDotSingletoness() throws Exception {
        this.serializeSingletonAndCheck(PathSeparator.notRequiredAtStart('.'));
    }

    @Override
    public Class<PathSeparator> type() {
        return PathSeparator.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public PathSeparator createObject() {
        return PathSeparator.requiredAtStart(SEPARATOR);
    }

    @Override
    public PathSeparator serializableInstance() {
        return PathSeparator.requiredAtStart('@');
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}

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
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.tree.Node;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

final public class PathSeparatorTest extends ClassTestCase<PathSeparator>
        implements HashCodeEqualsDefinedTesting<PathSeparator>, SerializationTesting<PathSeparator> {

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
        final PathSeparator separator = PathSeparator.requiredAtStart(SEPARATOR);
        assertEquals("character", SEPARATOR, separator.character());
        assertEquals("string", String.valueOf(SEPARATOR), separator.string());
        assertEquals("requiredAtStart", REQUIRED, separator.isRequiredAtStart());
    }

    @Test
    public void testRequiredSlashSingleton() {
        final char c = '/';
        final PathSeparator separator = PathSeparator.requiredAtStart(c);
        assertSame(separator, PathSeparator.requiredAtStart(c));
        assertEquals("character", c, separator.character());
        assertEquals("string", String.valueOf(c), separator.string());
        assertEquals("requiredAtStart", REQUIRED, separator.isRequiredAtStart());
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
        final PathSeparator separator = PathSeparator.notRequiredAtStart(SEPARATOR);
        assertEquals("character", SEPARATOR, separator.character());
        assertEquals("string", String.valueOf(SEPARATOR), separator.string());
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
    public void testAbsoluteNodeSelectorBuilder() {
        assertNotNull(PathSeparator.requiredAtStart(SEPARATOR)
                .absoluteNodeSelectorBuilder(Node.class));
    }

    @Test
    public void testRelativeNodeSelectorBuilder() {
        assertNotNull(PathSeparator.requiredAtStart(SEPARATOR)
                .relativeNodeSelectorBuilder(Node.class));
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
        assertEquals(String.valueOf(SEPARATOR),
                PathSeparator.requiredAtStart(SEPARATOR).toString());
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
    protected MemberVisibility typeVisibility() {
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

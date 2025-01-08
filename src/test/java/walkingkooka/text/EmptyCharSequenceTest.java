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

final public class EmptyCharSequenceTest implements ClassTesting2<EmptyCharSequence>,
    CharSequenceTesting<EmptyCharSequence> {

    @Test
    public void testCharAtFails() {
        this.charAtFails(EmptyCharSequence.INSTANCE, 0);
    }

    @Test
    public void testLength() {
        this.checkLength(0);
    }

    @Test
    public void testSubSequenceFails() {
        this.subSequenceFails(EmptyCharSequence.INSTANCE, 0, 1);
    }

    @Override
    @Test
    public void testSubSequenceWithSameFromAndToReturnsThis() {
        // nop
    }

    @Override
    @Test
    public void testEmptySubSequence() {
        // nop
    }

    @Override
    @Test
    public void testEmptySubSequence2() {
        // nop
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(EmptyCharSequence.INSTANCE, "");
    }

    @Override
    public EmptyCharSequence createCharSequence() {
        return EmptyCharSequence.INSTANCE;
    }

    @Override
    public Class<EmptyCharSequence> type() {
        return EmptyCharSequence.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public EmptyCharSequence createObject() {
        return this.createCharSequence();
    }
}

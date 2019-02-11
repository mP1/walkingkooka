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
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineTest extends ClassTestCase<Line>
        implements CharSequenceTesting<Line>,
            SerializationTesting<Line> {

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(NullPointerException.class, () -> {
            Line.with(null);
        });
    }

    @Test
    public void testWithCrTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Line.with("abc \r");
        });
    }

    @Test
    public void testWithNlTextFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Line.with("abc \n");
        });
    }

    @Test
    public void testEmpty() {
        this.createAndCheck("");
    }

    @Test
    public void testNotEmpty() {
        this.createAndCheck("abc123");
    }

    @Test
    public void testWhitespace() {
        this.createAndCheck("abc\tdef  ghi");
    }

    private void createAndCheck(final String text) {
        final Line line = Line.with(text);
        assertEquals(text, line.value(), "text value");
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(Line.with("different"));
    }

    @Test
    public void testSameTextDifferentCase() {
        this.checkNotEquals(Line.with("ABC123"));
    }

    @Override
    public Line createCharSequence() {
        return Line.with("abc");
    }

    @Override
    public Class<Line> type() {
        return Line.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Line createObject() {
        return this.createCharSequence();
    }

    @Override
    public Line serializableInstance() {
        return Line.with("abc123");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}

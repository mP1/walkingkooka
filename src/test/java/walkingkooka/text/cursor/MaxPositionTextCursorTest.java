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

package walkingkooka.text.cursor;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MaxPositionTextCursorTest implements TextCursorTesting,
    ToStringTesting<MaxPositionTextCursor> {

    @Test
    public void testWithNullTextCursorFails() {
        assertThrows(
            NullPointerException.class,
            () -> MaxPositionTextCursor.with(null)
        );
    }

    @Test
    public void testMax() {
        final MaxPositionTextCursor textCursor = MaxPositionTextCursor.with(
            TextCursors.charSequence("ABC456")
        );

        textCursor.next();

        final TextCursorSavePoint save = textCursor.save();

        textCursor.next();
        textCursor.next();

        this.atAndCheck(
            textCursor,
            '4'
        );

        save.restore();

        this.checkEquals(
            3,
            textCursor.max()
        );
    }

    @Test
    public void testToString() {
        final TextCursor wrapped = TextCursors.fake();

        this.toStringAndCheck(
            MaxPositionTextCursor.with(wrapped),
            wrapped.toString()
        );
    }

    @Override
    public Class<MaxPositionTextCursor> type() {
        return MaxPositionTextCursor.class;
    }
}

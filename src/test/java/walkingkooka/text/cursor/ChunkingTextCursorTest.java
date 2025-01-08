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
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public final class ChunkingTextCursorTest implements ClassTesting2<ChunkingTextCursor>,
    TextCursorTesting2<ChunkingTextCursor>,
    ToStringTesting<ChunkingTextCursor> {

    @Test
    public void testSingleChunk() {
        final TextCursor cursor = this.createTextCursor0("ABC");
        this.isEmptyAndCheck(
            cursor,
            false
        );

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    public void testMultipleChunks() {
        final TextCursor cursor = this.createTextCursor0("A", "B");
        this.isEmptyAndCheck(
            cursor,
            false
        );

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    public void testMultipleChunks2() {
        final TextCursor cursor = this.createTextCursor0("AB", "CD");
        this.isEmptyAndCheck(
            cursor,
            false
        );

        this.atAndCheck(cursor, 'A');
        cursor.next();
        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    @Test
    public void testGraphSaveAndRestores() {
        final TextCursor cursor = this.createTextCursor0("A", "BC", "D");
        this.isEmptyAndCheck(
            cursor,
            false
        );
        this.atAndCheck(cursor, 'A');
        cursor.next();

        final TextCursorSavePoint save = cursor.save();

        this.atAndCheck(cursor, 'B');
        cursor.next();
        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );

        save.restore(); // BCD

        this.atAndCheck(cursor, 'B');
        cursor.next();

        save.save();

        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );

        save.restore(); // CD

        this.atAndCheck(cursor, 'C');
        cursor.next();
        this.atAndCheck(cursor, 'D');
        cursor.next();

        this.isEmptyAndCheck(
            cursor,
            true
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final String text = "abc123";
        final ChunkingTextCursor chunking = this.createTextCursor(text);
        chunking.at();

        final TextCursor cursor = TextCursors.charSequence(text);
        cursor.at();

        this.toStringAndCheck(chunking, cursor.toString());
    }

    // TextCursorTesting2...............................................................................................

    @Override
    public ChunkingTextCursor createTextCursor(final String text) {
        return this.createTextCursor0(text);
    }

    private ChunkingTextCursor createTextCursor0(final String... text) {
        return ChunkingTextCursor.with(Iterators.array(text));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ChunkingTextCursor> type() {
        return ChunkingTextCursor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}

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

package walkingkooka.text.cursor.parser;

import walkingkooka.text.cursor.TextCursor;

/**
 * A {@link CharSequence} that reads characters from a {@link TextCursor}.
 */
final class DateTimeFormatterParserTextCursorCharSequence implements CharSequence {

    static DateTimeFormatterParserTextCursorCharSequence with(final TextCursor cursor, final int length) {
        return new DateTimeFormatterParserTextCursorCharSequence(cursor, length);
    }

    private DateTimeFormatterParserTextCursorCharSequence(final TextCursor cursor, final int length) {
        this.cursor = cursor;
        this.length = length;
    }

    @Override
    public int length() {
        return this.length;
    }

    private final int length;

    @Override
    public char charAt(final int index) {
        char at;

        if (index < this.b.length()) {
            at = this.b.charAt(index);
        } else {
            this.fillBuffer(index);
            at = this.b.charAt(index);
        }
        return at;
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        this.fillBuffer(end);
        return this.b.subSequence(start, end);
    }

    private void fillBuffer(final int index) {
        while (this.b.length() <= index) {
            if (this.cursor.isEmpty()) {
                break;
            }
            this.b.append(this.cursor.at());
            this.cursor.next();
        }
    }

    private final TextCursor cursor;

    private final StringBuilder b = new StringBuilder();

    @Override
    public String toString() {
        return b.toString();
    }
}

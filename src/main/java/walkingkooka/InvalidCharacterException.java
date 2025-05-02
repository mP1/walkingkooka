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

package walkingkooka;

import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * An {@link IllegalArgumentException} that reports an invalid character within some text.
 */
public class InvalidCharacterException extends InvalidTextException {

    public InvalidCharacterException(final String text,
                                     final int position) {
        this(
            text,
            position,
            NO_COLUMN,
            NO_LINE
        );
    }

    public InvalidCharacterException(final String text,
                                     final int position,
                                     final OptionalInt column,
                                     final OptionalInt line) {
        this(
            text,
            position,
            column,
            line,
            NO_APPEND_TO_MESSAGE,
            null
        );
    }

    public InvalidCharacterException(final String text,
                                     final int position,
                                     final Throwable cause) {
        this(
            text,
            position,
            NO_COLUMN,
            NO_LINE,
            NO_APPEND_TO_MESSAGE,
            cause
        );
    }

    private InvalidCharacterException(final String text,
                                      final int position,
                                      final OptionalInt column,
                                      final OptionalInt line,
                                      final String appendToMessage,
                                      final Throwable cause) {
        super(cause);

        CharSequences.failIfNullOrEmpty(text, "text");
        if (position < 0 || position >= text.length()) {
            throw new IllegalArgumentException("Invalid position " + position + " not between 0 and " +
                text.length() + " in " +
                CharSequences.quoteAndEscape(text));
        }

        this.text = text;
        this.position = position;

        this.column = column;
        this.line = line;

        this.appendToMessage = appendToMessage;
    }

    /**
     * Getter that returns the invalid character.
     */
    public final char character() {
        return this.text.charAt(this.position);
    }

    /**
     * Getter that returns the position of the invalid character within {@link #text()}.
     */
    public final int position() {
        return this.position;
    }

    private final int position;

    // column & line....................................................................................................

    public final static OptionalInt NO_COLUMN = OptionalInt.empty();

    public final OptionalInt column() {
        return this.column;
    }

    private final OptionalInt column;

    public final static OptionalInt NO_LINE = OptionalInt.empty();

    public final OptionalInt line() {
        return this.line;
    }

    private final OptionalInt line;

    public InvalidCharacterException setColumnAndLine(final int column,
                                                      final int line) {
        if (column < 1) {
            throw new IllegalArgumentException("Invalid column " + column + " < 1");
        }
        if (line < 1) {
            throw new IllegalArgumentException("Invalid line " + line + " < 1");
        }

        return this.setColumnAndLine0(
            OptionalInt.of(column),
            OptionalInt.of(line)
        );
    }

    private InvalidCharacterException setColumnAndLine0(final OptionalInt column,
                                                        final OptionalInt line) {

        return this.column.equals(column) &&
            this.line.equals(line) ?
            this :
            this.replace(
                this.text,
                this.position,
                column,
                line,
                this.appendToMessage
            );
    }

    // HasText..........................................................................................................

    @Override
    public final String text() {
        return this.text;
    }

    public final InvalidCharacterException setTextAndPosition(final String text, final int position) {
        return this.text.equals(text) && this.position == position ?
            this :
            this.replace(
                text,
                position,
                NO_COLUMN, // new position means old column/line must be wrong so clear
                NO_LINE,
                this.appendToMessage
            );
    }

    private final String text;

    // Throwable........................................................................................................

    /**
     * <pre>
     * Invalid character '/' at 1 in \"text\"
     * </pre>
     */
    @Override
    public final String getMessage() {
        return this.buildMessage(
            true // includeAt
        );
    }

    /**
     * Returns invalid character at position without any {@link #text}.
     * <pre>
     * Invalid character '/' at 1
     * </pre>
     */
    public final String getShortMessage() {
        return this.buildMessage(
            false // includeAt
        );
    }

    private String buildMessage(final boolean includeInText) {
        final StringBuilder b = new StringBuilder();

        // Invalid character '/' at X in \"text\" $appendToMessage
        b.append("Invalid character ");
        b.append(
            CharSequences.quoteIfChars(
                this.character()
            )
        );
        b.append(" at ");

        final OptionalInt column = this.column();
        if (column.isPresent()) {
            b.append('(');
            b.append(column.getAsInt());
            b.append(',');
            b.append(this.line.getAsInt());
            b.append(')');
        } else {
            b.append(this.position);
        }

        if (includeInText) {
            b.append(" in ");
            b.append(
                CharSequences.quote(this.text)
            );
        }

        final String appendToMessage = this.appendToMessage;
        if (false == appendToMessage.isEmpty()) {
            b.append(' ')
                .append(appendToMessage);
        }

        return b.toString();
    }

    // appendToMessage..................................................................................................

    // @VisibleForTesting
    final static String NO_APPEND_TO_MESSAGE = "";

    /**
     * Appends some text to the generic {@link #getMessage()}. This is useful when the invalid position may belong
     * to a specific line within a large amount of text.
     */
    public final InvalidCharacterException appendToMessage(final String appendToMessage) {
        return this.appendToMessage.equals(appendToMessage) ?
            this :
            this.replace(
                this.text,
                this.position,
                this.column,
                this.line,
                Objects.requireNonNull(appendToMessage, "appendToMessage")
            );
    }

    /**
     * Getter that returns the {@link #appendToMessage}.
     */
    public String appendToMessage() {
        return this.appendToMessage;
    }

    /**
     * This is some extra text appended to the generic Invalid character message.
     */
    // @VisibleForTesting
    final String appendToMessage;

    private InvalidCharacterException replace(final String text,
                                              final int position,
                                              final OptionalInt column,
                                              final OptionalInt line,
                                              final String appendToMessage) {
        return
            new InvalidCharacterException(
                text,
                position,
                column,
                line,
                appendToMessage,
                this.getCause()
            );
    }

    private static final long serialVersionUID = 1L;

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(
            this.text,
            this.position,
            this.column,
            this.line,
            this.appendToMessage
        );
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || other instanceof InvalidCharacterException && this.equals0((InvalidCharacterException) other);
    }

    private boolean equals0(final InvalidCharacterException other) {
        return this.text.equals(other.text) &&
            this.position == other.position &&
            this.column.equals(other.column) &&
            this.line.equals(other.line) &&
            this.appendToMessage.equals(other.appendToMessage);
    }
}

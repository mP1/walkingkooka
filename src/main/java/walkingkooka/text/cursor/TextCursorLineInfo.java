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

import walkingkooka.EmptyTextException;
import walkingkooka.InvalidCharacterException;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasTextOffset;

import java.util.Optional;

/**
 * A snapshot in time about the position of the {@link TextCursor}.
 */
public interface TextCursorLineInfo extends TextCursorLike,
    HasTextOffset {

    /**
     * The entire line of text that the {@link TextCursor} is currently on. Note the text may include characters that were filtered and not returned by
     * the parent {@link TextCursor} itself.
     */
    CharSequence text();

    /**
     * Returns the line number of the cursor. Note that line numbering begins at 1.
     */
    int lineNumber();

    /**
     * Returns the column number of the {@link TextCursor}. Note that column numbering begins at 1.
     */
    int column();

    /**
     * Builds a string with the column and line number separated by a comma and surrounded by parenthesis.
     */
    String summary();

    /**
     * Returns the text offset of the {@link TextCursor}, with the first character returning a textOffset of 0.
     */
    @Override
    int textOffset();

    /**
     * Builds a {@link InvalidCharacterException} from this {@link TextCursorLineInfo}.
     * If the cursor has advanced past the end of the text, this returns an {@link Optional#empty()}.
     */
    default Optional<InvalidCharacterException> invalidCharacterException() {
        final String text = this.text().toString();
        final int textOffset = this.textOffset();

        return Optional.ofNullable(
            textOffset >= 0 && textOffset < text.length() ?
                new InvalidCharacterException(
                    text,
                    textOffset
                ) :
                null
        );
    }

    /**
     * Returns a {@link EmptyTextException} if the text is empty of a {@link InvalidCharacterException}.
     * This is useful to provide an {@link IllegalArgumentException} to handle all text position cases.
     */
    default IllegalArgumentException emptyTextOrInvalidCharacterExceptionOrLast(final String emptyTextLabel) {
        CharSequences.failIfNullOrEmpty(emptyTextLabel, "emptyTextLabel");

        final String text = this.text()
            .toString();
        final int textOffset = this.textOffset();

        return text.isEmpty() ?
            new EmptyTextException(emptyTextLabel) :
            this.invalidCharacterException()
                .orElseGet(
                    () -> new InvalidCharacterException(
                        text,
                        textOffset - 1
                    )
                );
    }
}

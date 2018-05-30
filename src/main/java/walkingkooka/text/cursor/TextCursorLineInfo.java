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

package walkingkooka.text.cursor;

/**
 * A snapshot in time about the position of the {@link TextCursor}.
 */
public interface TextCursorLineInfo extends TextCursorLike {

    /**
     * The entire line of text that the {@link TextCursor} is currently on. Note the text may include characters that were filtered and not returned by
     * the parent {@link TextCursor} itself.
     */
    abstract CharSequence text();

    /**
     * Returns the line number of the cursor. Note that line numbering begins at 1.
     */
    abstract int lineNumber();

    /**
     * Returns the column number of the {@link TextCursor}. Note that column numbering begins at 1.
     */
    abstract int column();

    /**
     * Builds a string with with the column and line number separated by a comma and surrounded by parenthesis.
     */
    abstract String summary();
}

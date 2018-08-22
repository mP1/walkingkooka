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
 *
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.TextCursorLineInfo;

import java.util.Objects;

/**
 * This exception is used to report unrecoverable errors within text, such as an invalid escape character in a quoted string.
 */
public class ParserReporterException extends ParserException {

    private final static long serialVersionUID = 1L;

    public ParserReporterException(final String message, final TextCursorLineInfo lineInfo) {
        super(Whitespace.failIfNullOrWhitespace(message, "message"));
        Objects.requireNonNull(lineInfo, "lineInfo");
        this.lineInfo = lineInfo;
    }

    public final TextCursorLineInfo lineInfo() {
        return this.lineInfo;
    }

    private final TextCursorLineInfo lineInfo;
}

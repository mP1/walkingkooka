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

import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;

/**
 * A reporter may be used to report a parse failure. Typically its the last in a chain, so if other {@link Parser parsers},
 * fail the reporter will then be called.
 */
public interface ParserReporter<T extends ParserToken, C extends ParserContext> {

    /**
     * Handles a parse failure.
     */
    Optional<T> report(final TextCursor cursor, final C context, final Parser<T, C> parser) throws ParserReporterException;
}

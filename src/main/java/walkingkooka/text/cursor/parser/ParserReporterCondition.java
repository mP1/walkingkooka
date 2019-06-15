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

import java.util.Optional;

/**
 * Several conditions that predicate the calling of a {@link ParserReporter}.
 */
public enum ParserReporterCondition {

    /**
     * Invoke the {@link ParserReporter} unconditionally.
     */
    ALWAYS {
        <C extends ParserContext> Optional<ParserToken> parse(final TextCursor cursor,
                                                              final ReportingParser<C> parser,
                                                              final C context) {
            return parser.report(cursor, context);
        }
    },

    /**
     * The {@link ParserReporter} should only be triggered when the {@link TextCursor} is not empty.
     */
    NOT_EMPTY {
        <C extends ParserContext> Optional<ParserToken> parse(final TextCursor cursor,
                                                              final ReportingParser<C> parser,
                                                              final C context) {
            return parser.reportIfNotEmpty(cursor, context);
        }
    };

    abstract <C extends ParserContext> Optional<ParserToken> parse(final TextCursor cursor,
                                                                   final ReportingParser<C> parser,
                                                                   final C context);
}

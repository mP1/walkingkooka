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

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} that acts as a bridge invoking a {@link ParserReporter}. The reporter will
 * typically throw an exception with a message noting a parse failure of the parser in this instance.
 */
final class ReportingParser<C extends ParserContext> implements Parser<C> {

    /**
     * Static factory
     */
    static <C extends ParserContext> ReportingParser<C> with(final ParserReporterCondition condition,
                                                                                    final ParserReporter<C> reporter,
                                                                                    final Parser<C> parser) {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(reporter, "reporter");
        Objects.requireNonNull(parser, "parser");

        return new ReportingParser<>(condition, reporter, parser);
    }

    /**
     * Private ctor
     */
    private ReportingParser(final ParserReporterCondition condition, final ParserReporter<C> reporter, final Parser<C> parser) {
        super();

        this.condition = condition;
        this.reporter = reporter;
        this.parser = parser;
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final C context) {
        return this.condition.parse(cursor, this, context);
    }

    final Optional<ParserToken> report(final TextCursor cursor, final C context) {
        return this.reporter.report(cursor, context, this.parser);
    }

    final Optional<ParserToken> reportIfNotEmpty(final TextCursor cursor, final C context) {
        final Optional<ParserToken> result = this.parser.parse(cursor, context);
        return cursor.isEmpty() ?
               result :
               this.report(cursor, context);
    }

    private final ParserReporterCondition condition;

    private final ParserReporter<C> reporter;

    private final Parser<C> parser;

    @Override
    public String toString() {
        return this.reporter.toString();
    }
}

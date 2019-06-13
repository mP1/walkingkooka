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

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ResourceTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.type.JavaVisibility;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SearchQueryTest implements ClassTesting2<SearchQuery>,
        ResourceTesting {

    @Test
    public void testReplaceSelected() throws IOException {
        this.replaceSelectedAndCheck(
                "testReplaceSelected",
                this.queryAndReplace("ABC",
                        CaseSensitivity.SENSITIVE,
                        "XYZ"));
    }

    @Test
    public void testReplaceSelectedInsensitive() throws IOException {
        this.replaceSelectedAndCheck("testReplaceSelectedInsensitive", CaseSensitivity.INSENSITIVE);
    }

    /**
     * Some EOL and extra ABC have been randomly inserted into the typical LOREM text.
     * The expected file has these replaced.
     */
    @Test
    public void testReplaceMultipleSameSearchForText() throws IOException {
        this.replaceSelectedAndCheck("testReplaceMultipleSameSearchForText", CaseSensitivity.INSENSITIVE);
    }

    /**
     * Random words within the LOREM text are searched for selected and uppercased by the replaced function.
     */
    @Test
    public void testReplaceDifferentSelectedText() throws IOException {
        this.replaceSelectedAndCheck("testReplaceDifferentSelectedText",
                (sequenceNode -> {
                    SearchNode result = sequenceNode;
                    for (String searchFor : Lists.of("Lorem", "elit", "eiusmod", "nisi", "laborum")) {
                        final SearchQuery query = SearchQueryValue.text(searchFor)
                                .equalsQuery(CaseSensitivity.INSENSITIVE);
                        result = query.select(result);
                    }

                    return result.replaceSelected((s) -> SearchNode.text(s.text().toUpperCase(), s.text().toUpperCase()));
                }));
    }

    private void replaceSelectedAndCheck(final String test, final CaseSensitivity caseSensitivity) throws IOException {
        this.replaceSelectedAndCheck(test, this.queryAndReplace("abc", caseSensitivity, "XYZ"));
    }

    private Function<SearchSequenceNode, SearchNode> queryAndReplace(final String searchFor,
                                                                     final CaseSensitivity caseSensitivity,
                                                                     final String replaceWith) {
        return (sequenceNode -> {
            final SearchQuery query = SearchQueryValue.text(searchFor)
                    .equalsQuery(caseSensitivity);

            // query and replace
            return query.select(sequenceNode)
                    .replaceSelected((s) -> SearchNode.text(replaceWith, replaceWith));
        });
    }

    private void replaceSelectedAndCheck(final String test,
                                         final Function<SearchSequenceNode, SearchNode> queryAndReplace) throws IOException {
        final String input = this.resourceAsText(test + ".input.txt");

        // boring tokenize on space...

        final Parser<ParserContext> words = Parsers.stringCharPredicate(CharPredicates.letterOrDigit(), 1, 100).cast();
        final Parser<ParserContext> whitespace = Parsers.stringCharPredicate(CharPredicates.whitespace(), 1, 100).cast();
        final Parser<ParserContext> other = Parsers.stringCharPredicate(CharPredicates.whitespace().or(CharPredicates.letterOrDigit()).negate(), 1, 100).cast();

        final Parser<ParserContext> parser = Parsers.repeated(
                Parsers.alternatives(Lists.of(words, whitespace, other)))
                .orReport(ParserReporters.basic());

        final Optional<ParserToken> tokens = parser.parse(TextCursors.charSequence(input), new FakeParserContext());

        // convert into SearchTextNodes
        final SearchSequenceNode nodes = SearchNode.sequence(RepeatedParserToken.class.cast(tokens.get()).flat().value().stream()
                .map(t -> SearchNode.text(t.text(), t.text()))
                .collect(Collectors.toList()));

        final SearchNode replaced = queryAndReplace.apply(nodes);

        // convert SearchNode back into text.
        final String expected = this.resourceAsText(test + ".expected.txt");

        assertEquals(expected,
                replaced.text(),
                "search and replace failed\n" + input);
    }

    private String resourceAsText(final String filename) throws IOException {
        final Class<?> classs = this.getClass();
        return this.resourceAsText(classs, classs.getSimpleName() + "/" + filename);
    }

    @Override
    public Class<SearchQuery> type() {
        return SearchQuery.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

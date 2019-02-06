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

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserTokenInvalidReferencesException;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserContexts;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class CharPredicateGrammarEbnfParserTokenVisitorTest extends CharPredicateTestCase<CharPredicate> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testFromGrammarNullGrammarFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicateGrammarEbnfParserTokenVisitor.fromGrammar(null, Maps.empty());
        });
    }

    @Test
    public void testFromGrammarNullDefaultsFails() {
        final String text = "test='text';";
        final EbnfParserToken rule = EbnfRuleParserToken.rule(Lists.of(
                EbnfParserToken.identifier(TEST, "test"),
                EbnfParserToken.terminal("terminal", "'terminal'")),
                text);
        final EbnfGrammarParserToken grammar = EbnfParserToken.grammar(Lists.of(rule), text);

        assertThrows(NullPointerException.class, () -> {
            CharPredicateGrammarEbnfParserTokenVisitor.fromGrammar(grammar, null);
        });
    }

    @Test
    public void testTerminal() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testIdentifier() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testIdentifierForwardReference() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testIdentifierUnknownFail() {
        assertThrows(EbnfGrammarParserTokenInvalidReferencesException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testRangeBeginInvalidTerminalFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testRangeEndInvalidTerminalFail() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testRange() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testAlternatives() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testPredefined() {
        final CharPredicate predicate = this.createCharPredicate0();
        this.testTrue(predicate,'@');
        this.testTrue(predicate,'B');
        this.testTrue(predicate,'C');
        this.testFalse(predicate,'D');
    }

    @Test
    public void testException() {
        this.readGrammarAndCheck();
    }

    @Test
    public void testConcatenation() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testOptional() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testRepeated() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createCharPredicate0();
        });
    }

    @Test
    public void testGroup() {
        this.readGrammarAndCheck();
    }

    private void readGrammarAndCheck() {
        final CharPredicate predicate = this.createCharPredicate0();
        this.testTrue(predicate,'A');
        this.testTrue(predicate,'B');
        this.testTrue(predicate,'C');
        this.testFalse(predicate,'D');
    }

    @Override
    public void testCheckToStringOverridden() {
        // nop
    }

    @Override
    protected CharPredicate createCharPredicate() {
        return this.createCharPredicate1("default.grammar");
    }

    private CharPredicate createCharPredicate0() {
        return this.createCharPredicate1(this.currentTestName() + ".grammar");
    }

    /**
     * Parses the grammar file, uses the transformer to convert each rule into matchers and then returns the parser for the rule called "TEST".
     */
    private CharPredicate createCharPredicate1(final String grammarResourceFile) {
        final EbnfGrammarParserToken grammar = this.grammar(grammarResourceFile);

        final Map<EbnfIdentifierName, CharPredicate> defaults = Maps.hash();
        defaults.put(EbnfIdentifierName.with("ATSIGN"), CharPredicates.is('@'));
        final Map<EbnfIdentifierName, CharPredicate> all = CharPredicateGrammarEbnfParserTokenVisitor.fromGrammar(grammar, defaults);

        final CharPredicate test = all.get(TEST);
        assertNotNull(test, TEST + " parser not found in grammar\n" + grammar);
        return test;
    }

    private final EbnfIdentifierName TEST = EbnfIdentifierName.with("TEST");

    private EbnfGrammarParserToken grammar(final String resourceName) {
        try {
            final Class<?> classs = this.getClass();
            final String text = this.resourceAsText(classs, classs.getSimpleName() + "/" + resourceName);
            final TextCursor cursor = TextCursors.charSequence(text);
            final Optional<EbnfGrammarParserToken> grammar = EbnfParserToken.grammarParser()
                    .parse(cursor, EbnfParserContexts.basic());
            if (!grammar.isPresent()) {
                fail("Failed to parse a grammar from " + CharSequences.quote(resourceName) + "\n" + text);
            }
            if(!cursor.isEmpty()) {
                final TextCursorSavePoint save = cursor.save();
                cursor.end();
                final CharSequence remaining = save.textBetween();
                fail("Failed to parse all of grammar from " + CharSequences.quote(resourceName) + " text remaining: " + remaining + "\n\n" + CharSequences.escape(remaining) + "\n\nGrammar File:\n" + text);
            }
            return grammar.get();
        } catch (final IOException cause) {
            throw new Error("failed to read grammar from " + CharSequences.quote(resourceName));
        }
    }

    @Override
    public void testClassVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<CharPredicate> type() {
        return CharPredicate.class;
    }
}

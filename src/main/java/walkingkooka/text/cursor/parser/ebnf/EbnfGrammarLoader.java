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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

/**
 * Loads a grammar file using {@link Class#getResourceAsStream(String)} holding the result which may be an
 * {@link Optional} or any thrown {@link Throwable}.
 */
public final class EbnfGrammarLoader {

    /**
     * Factory that creates a new {@link EbnfGrammarLoader}
     */
    public static EbnfGrammarLoader with(final String filename, final Class<?> klass) {
        CharSequences.failIfNullOrEmpty(filename, "filename");
        Objects.requireNonNull(klass, "class");

        return new EbnfGrammarLoader(filename, klass);
    }

    /**
     * Private ctor use factory.
     */
    private EbnfGrammarLoader(String filename, Class<?> klass) {
        this.filename = filename;
        this.klass = klass;
    }

    /**
     * Lazily loads the loaded and stores the result including exceptions in {@link #loaded}
     */
    public Optional<EbnfGrammarParserToken> grammar() {
        if (null == this.loaded) {
            this.loaded = loadGrammar();
        }
        if (this.loaded instanceof RuntimeException) {
            throw (RuntimeException) this.loaded;
        }
        return Cast.to(this.loaded);
    }

    /**
     * Holds either a {@link Optional<EbnfGrammarParserToken>} or {@link Throwable}.
     */
    private Object loaded;

    /**
     * Loads the grammar and returns the result.
     */
    private Object loadGrammar() {
        Object result = null;

        try {
            final InputStream inputStream = this.klass.getResourceAsStream(this.filename);
            if (null == inputStream) {
                throw new EbnfParserException("Unable to find " + this);
            }
            try (final InputStream inputStream2 = inputStream) {
                final TextCursor grammarFile = TextCursors.charSequence(
                        CharSequences.readerConsuming(
                                new InputStreamReader(inputStream2, Charset.defaultCharset()),
                                4096));
                final Optional<ParserToken> grammar = EbnfParserToken.grammarParser()
                        .orFailIfCursorNotEmpty(ParserReporters.basic())
                        .parse(grammarFile, EbnfParserContexts.basic());
                if (grammar.isPresent()) {
                    result = grammar;
                }
            }
        } catch (final Exception fail) {
            result = fail;
        }
        return result;
    }

    private final String filename;
    private final Class<?> klass;

    @Override
    public String toString() {
        return klass.getName() + "/" + CharSequences.quote(this.filename);
    }
}

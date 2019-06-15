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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;
import java.util.Optional;

/**
 * A parser that knows how to parse text holding one or more directives.
 * <pre>
 * Cache-Control: public, max-age=31536000
 * Cache-Control: no-cache, no-store, must-revalidate
 * </pre>
 */
final class CacheControlDirectiveHeaderValueParser extends HeaderValueParser {

    static List<CacheControlDirective<?>> parseCacheControlDirectiveList(final String text) {
        final CacheControlDirectiveHeaderValueParser parser = new CacheControlDirectiveHeaderValueParser(text);
        parser.parse();
        return parser.directives;
    }

    private CacheControlDirectiveHeaderValueParser(String text) {
        super(text);
    }

    @Override
    void whitespace() {
        if (!this.requireDirectiveName) {
            this.failInvalidCharacter(); // whitespace after directives not allowed.
        }
        this.skipWhitespace();
    }

    @Override
    void tokenSeparator() {
        if (this.requireDirectiveName) {
            this.failInvalidCharacter();
        }
        this.addParameter();
    }

    @Override
    void keyValueSeparator() {
        if (this.requireDirectiveName) {
            this.failInvalidCharacter();
        }
        this.expectsParameterValue = true;
    }

    @Override
    void multiValueSeparator() {
        if (this.requireDirectiveName) {
            this.failInvalidCharacter();
        }
        if (this.expectsParameterValue) {
            this.failMissingParameterValue();
        }

        final CacheControlDirectiveName directiveName = this.directiveName;
        if (null != directiveName) {
            this.addParameter();
        }
    }

    @Override
    void wildcard() {
        this.failInvalidCharacter();
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    @Override
    void quotedText() {
        // didnt expect this quoted text.
        if (this.requireDirectiveName || !this.expectsParameterValue) {
            this.failInvalidCharacter();
        }
        this.parameterValue = this.directiveName.toValue(this.quotedText(ASCII, ESCAPING_SUPPORTED));
        this.expectsParameterValue = false;
    }

    @Override
    void comment() {
        this.skipComment(); // consume and ignore comment text itself.
    }

    @Override
    void token() {
        if (this.requireDirectiveName) {
            this.requireDirectiveName = false;
            this.directiveName = CacheControlDirectiveName.with(this.token(RFC2045TOKEN));
            this.parameterValue = Optional.empty();
        } else {
            if (!this.expectsParameterValue) {
                this.failInvalidCharacter();
            }
            final String text = this.token(DIGIT);
            if (text.isEmpty()) {
                this.failInvalidCharacter();
            }
            this.parameterValue = this.directiveName.toValue(text);
            this.expectsParameterValue = false;
        }
    }

    /**
     * Unquoted tokens must contain numeric values.
     */
    private final static CharPredicate DIGIT = CharPredicates.digit();

    @Override
    void endOfText() {
        if (this.requireDirectiveName) {
            this.position--;
            this.failInvalidCharacter();
        }
        if (this.expectsParameterValue) {
            this.failMissingParameterValue();
        }

        CacheControlDirectiveName directiveName = this.directiveName;
        if (null != directiveName) {
            this.addParameter();
        }
    }

    private void addParameter() {
        final CacheControlDirectiveName<?> directiveName = this.directiveName;
        final Optional<?> parameterValue = this.parameterValue;

        if (directiveName.requiresParameter() && !parameterValue.isPresent()) {
            if (this.hasMoreCharacters()) {
                this.failInvalidCharacter();
            } else {
                this.failMissingParameterValue();
            }
        }
        this.directives.add(directiveName.setParameter(Cast.to(parameterValue)));

        this.requireDirectiveName = true;
        this.directiveName = null;
        this.parameterValue = null;
        this.expectsParameterValue = false;
    }

    /**
     *
     */
    private boolean requireDirectiveName = true;

    /**
     * The current directive name.
     */
    private CacheControlDirectiveName<?> directiveName;

    /**
     * The accompanying parameter value
     */
    private Optional<?> parameterValue;

    /**
     * When true tokens are actually parameter values, after a equals sign.
     */
    private boolean expectsParameterValue;

    /**
     * Collects directives as they are encountered.
     */
    private final List<CacheControlDirective<?>> directives = Lists.array();

    @Override
    void missingValue() {
        this.failMissingValue(DIRECTIVE);
    }

    final static String DIRECTIVE = "directive";
}

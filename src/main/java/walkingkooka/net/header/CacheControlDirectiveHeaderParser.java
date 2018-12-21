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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;
import java.util.Optional;

/**
 * A parser that knows how to parse text holding one or more directives.
 */
final class CacheControlDirectiveHeaderParser extends HeaderParser {

    static List<CacheControlDirective<?>> parseCacheControlDirectiveList(final String text) {
        return new CacheControlDirectiveHeaderParser(text)
                .parse();
    }

    private CacheControlDirectiveHeaderParser(String text) {
        super(text);
    }

    /**
     * Parses a header value which must have one or more cache control directives.
     */
    private List<CacheControlDirective<?>> parse() {
        int mode = MODE_DIRECTIVE_NAME;
        int start = 0;
        CacheControlDirectiveName<?> directiveName = null;
        Optional<?> parameter = Optional.empty();

        while (this.hasMoreCharacters()) {
            final char c = this.character();

            switch (mode) {
                case MODE_SEPARATOR_WHITESPACE:
                    this.consumeWhitespace();

                    start = this.position;
                    parameter = Optional.empty();
                    mode = MODE_DIRECTIVE_NAME;

                    this.position--;
                    break;
                case MODE_DIRECTIVE_NAME:
                    if (RFC2045TOKEN.test(c)) {
                        break;
                    }
                    directiveName = this.directiveName(start);
                    parameter = Optional.empty();
                    if (SEPARATOR == c) {
                        this.addParameter(Cast.to(directiveName), parameter);
                        mode = MODE_SEPARATOR_WHITESPACE;
                        break;
                    }
                    if (directiveName.mayIncludeParameter() && PARAMETER_NAME_VALUE_SEPARATOR == c) {
                        mode = MODE_PARAMETER_VALUE_INITIAL;
                        start = this.position + 1;
                        break;
                    }
                    failInvalidCharacter();
                case MODE_PARAMETER_VALUE_INITIAL:
                    if (DOUBLE_QUOTE == c) {
                        mode = MODE_PARAMETER_QUOTED_VALUE;
                        start = this.position + 1;
                        break;
                    }
                    mode = MODE_PARAMETER_NUMERIC_VALUE;
                    start = this.position;
                    // fall thru must be a parameter value
                case MODE_PARAMETER_NUMERIC_VALUE:
                    if (DIGIT.test(c)) {
                        break;
                    }
                    if (SEPARATOR == c) {
                        this.addParameter(directiveName, start, DISALLOW_EMPTY_PARAMETER);
                        mode = MODE_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter();
                case MODE_PARAMETER_QUOTED_VALUE:
                    if (DOUBLE_QUOTE == c) {
                        this.addParameter(directiveName, start, ALLOW_EMPTY_PARAMETER);
                        mode = MODE_SEPARATOR;
                        break;
                    }
                    if (RFC2045TOKEN.test(c)) {
                        break;
                    }
                    failInvalidCharacter();
                case MODE_SEPARATOR:
                    if (SEPARATOR == c) {
                        mode = MODE_SEPARATOR_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter();
                default:
                    NeverError.unhandledCase(MODE_SEPARATOR_WHITESPACE,
                            MODE_DIRECTIVE_NAME,
                            MODE_PARAMETER_VALUE_INITIAL,
                            MODE_PARAMETER_NUMERIC_VALUE,
                            MODE_PARAMETER_QUOTED_VALUE,
                            MODE_SEPARATOR,
                            MODE_SEPARATOR_WHITESPACE);
            }
            this.position++;
        }

        switch (mode) {
            case MODE_SEPARATOR_WHITESPACE:
                this.position--;
                failInvalidCharacter();
                break;
            case MODE_DIRECTIVE_NAME:
                this.addParameter(Cast.to(this.directiveName(start)), parameter);
                break;
            case MODE_PARAMETER_VALUE_INITIAL:
                this.failMissingParameterValue();
            case MODE_PARAMETER_NUMERIC_VALUE:
                this.addParameter(directiveName, start, DISALLOW_EMPTY_PARAMETER);
                break;
            case MODE_PARAMETER_QUOTED_VALUE:
                this.fail(missingClosingQuote(this.text));
            case MODE_SEPARATOR:
                break;
            default:
                NeverError.unhandledCase(MODE_SEPARATOR_WHITESPACE,
                        MODE_DIRECTIVE_NAME,
                        MODE_PARAMETER_VALUE_INITIAL,
                        MODE_PARAMETER_NUMERIC_VALUE,
                        MODE_PARAMETER_QUOTED_VALUE,
                        MODE_SEPARATOR,
                        MODE_SEPARATOR_WHITESPACE);
        }

        return Lists.readOnly(this.directives);
    }

    private final static int MODE_SEPARATOR_WHITESPACE = 1;
    private final static int MODE_DIRECTIVE_NAME = MODE_SEPARATOR_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE_INITIAL = MODE_DIRECTIVE_NAME + 1;
    private final static int MODE_PARAMETER_NUMERIC_VALUE = MODE_PARAMETER_VALUE_INITIAL + 1;
    private final static int MODE_PARAMETER_QUOTED_VALUE = MODE_PARAMETER_NUMERIC_VALUE + 1;
    private final static int MODE_SEPARATOR = MODE_PARAMETER_QUOTED_VALUE + 1;

    private final static CharPredicate DIGIT = CharPredicates.digit();

    private final static char SEPARATOR = HeaderValue.SEPARATOR.character();
    private final static char PARAMETER_NAME_VALUE_SEPARATOR = HeaderValue.PARAMETER_NAME_VALUE_SEPARATOR.character();

    /**
     * Factory that creates a new {@link CacheControlDirectiveName}
     */
    private CacheControlDirectiveName<?> directiveName(final int start) {
        final String text = this.text.substring(start, this.position);
        if (text.isEmpty()) {
            if(this.position == this.text.length()) {
                this.position--;
            }
            this.failInvalidCharacter();
        }
        return CacheControlDirectiveName.with(text);
    }

    private final static boolean ALLOW_EMPTY_PARAMETER = true;
    private final static boolean DISALLOW_EMPTY_PARAMETER = false;

    /**
     * Factory that creates a {@link CacheControlDirective} after extracting and converting the text of the present value
     */
    private <T> void addParameter(final CacheControlDirectiveName<T> name,
                                  final int start,
                                  final boolean allowEmpty) {
        final String parameter = this.text.substring(start, this.position);
        if (parameter.isEmpty() && !allowEmpty) {
            this.failMissingParameterValue();
        }
        this.addParameter(name, name.toValue(parameter));
    }

    /**
     * Factory that creates a {@link CacheControlDirective} with the given parameter value
     */
    private <T> void addParameter(final CacheControlDirectiveName<T> name,
                                  final Optional<T> parameter) {
        if (name.requiresParameter() && Optional.empty().equals(parameter)) {
            if (this.hasMoreCharacters()) {
                this.failInvalidCharacter();
            } else {
                this.failMissingParameterValue();
            }
        }
        if (!name.mayIncludeParameter() && !Optional.empty().equals(parameter)) {
            if (this.hasMoreCharacters()) {
                this.failInvalidCharacter();
            } else {
                this.failMissingParameterValue();
            }
        }
        this.directives.add(name.setParameter(parameter));
    }

    private final List<CacheControlDirective<?>> directives = Lists.array();
}

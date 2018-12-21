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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

/**
 * Base class for all header value token parsers.
 */
abstract class TokenHeaderValueHeaderParser extends HeaderParserWithParameters<TokenHeaderValue,
        TokenHeaderValueParameterName<?>> {

    TokenHeaderValueHeaderParser(final String text) {
        super(text);
    }

    @Override
    final TokenHeaderValue wildcardValue() {
        return this.failInvalidCharacter();
    }

    @Override
    final TokenHeaderValue value() {
        return this.token(RFC2045TOKEN, TokenHeaderValue::with);
    }

    @Override
    final TokenHeaderValueParameterName<?> parameterName() {
        return this.token(PARAMETER_NAME, TokenHeaderValueParameterName::with);
    }

    final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override
    final String quotedParameterValue(final TokenHeaderValueParameterName<?> parameterName) {
        return this.failInvalidCharacter();
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = CharPredicates.ascii();

    @Override
    final String unquotedParameterValue(final TokenHeaderValueParameterName<?> parameterName) {
        return this.token(RFC2045TOKEN);
    }

    final static CharPredicate UNQUOTED_PARAMETER_VALUE = RFC2045TOKEN;

    @Override
    final void missingValue() {
        this.failMissingValue(VALUE);
    }

    final static String VALUE = "value";
}

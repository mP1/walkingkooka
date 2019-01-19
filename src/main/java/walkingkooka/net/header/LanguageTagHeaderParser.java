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

/**
 * Base class for a parser of language tags.
 */
abstract class LanguageTagHeaderParser extends HeaderParserWithParameters<LanguageTag, LanguageTagParameterName<?>> {

    LanguageTagHeaderParser(final String text) {
        super(text);
    }

    @Override final LanguageTag wildcardValue() {
        this.position++;
        return LanguageTag.WILDCARD;
    }

    @Override final LanguageTag value() {
        return LanguageTag.with(this.token(LANGUAGE_TAG, LanguageTagName::with));
    }

    final static CharPredicate LANGUAGE_TAG = RFC2045TOKEN;

    @Override final LanguageTagParameterName<?> parameterName() {
        return this.parameterName(PARAMETER_NAME, LanguageTagParameterName::with);
    }

    final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override final String quotedParameterValue(final LanguageTagParameterName<?> parameterName) {
        return this.quotedText(QUOTED_PARAMETER_VALUE, ESCAPING_SUPPORTED);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = ASCII;

    @Override final String unquotedParameterValue(final LanguageTagParameterName<?> parameterName) {
        return this.token(UNQUOTED_PARAMETER_VALUE);
    }

    final static CharPredicate UNQUOTED_PARAMETER_VALUE = RFC2045TOKEN;

    @Override final void missingValue() {
        this.failEmptyToken(LANGUAGE);
    }

    final static String LANGUAGE = "language";
}

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
 * Base class which parses text containing one or many media types.
 */
abstract class MediaTypeHeaderParser extends HeaderParserWithParameters<MediaType,
        MediaTypeParameterName<?>> {

    // @VisibleForTesting
    MediaTypeHeaderParser(final String text) {
        super(text);
    }

    @Override
    final MediaType wildcardValue() {
        final String type = "" + WILDCARD;
        this.position++;

        this.expectSlash();

        return MediaType.with(type,
                this.subType());
    }

    @Override
    final MediaType value() {
        return MediaType.with(this.type(),
                this.subType());
    }

    private String type() {
        final String type = this.token(RFC2045TOKEN);
        if (!this.hasMoreCharacters()) {
            failEmptyToken(TYPE);
        }

        this.expectSlash();

        if (type.isEmpty()) {
            this.failEmptyToken(TYPE);
        }

        return type;
    }

    private void expectSlash() {
        if (!this.hasMoreCharacters()) {
            this.failEmptyToken(SUBTYPE);
        }
        if (this.character() != SLASH) {
            this.failInvalidCharacter();
        }
        this.position++;
    }

    private String subType() {
        String subType;

        if (this.hasMoreCharacters() && this.character() == WILDCARD) {
            subType = "" + WILDCARD;
            this.position++;
        } else {
            subType = this.token(RFC2045TOKEN);
            if (subType.isEmpty()) {
                if (!this.hasMoreCharacters()) {
                    this.failEmptyToken(SUBTYPE);
                }

                this.failInvalidCharacter();
            }
        }
        return subType;
    }

    @Override
    final void missingValue() {
        this.failMissingValue(MEDIATYPE);
    }

    final static String MEDIATYPE = "media type";

    private final static char SLASH = '/';
    private final static String TYPE = "type";
    private final static String SUBTYPE = "sub type";

    @Override
    final MediaTypeParameterName<?> parameterName() {
        return this.token(PARAMETER_NAME, MediaTypeParameterName::with);
    }

    final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override
    final String quotedParameterValue(final MediaTypeParameterName<?> parameterName) {
        return this.quotedText(QUOTED_PARAMETER_VALUE, true);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = CharPredicates.ascii();

    @Override
    final String unquotedParameterValue(final MediaTypeParameterName<?> parameterName) {
        return this.token(UNQUOTED_PARAMETER_VALUE);
    }

    final static CharPredicate UNQUOTED_PARAMETER_VALUE = RFC2045TOKEN;
}

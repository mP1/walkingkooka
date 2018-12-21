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

/**
 * Base class for all header value token parsers.
 */
abstract class TokenHeaderValueHeaderParser extends HeaderParser2<TokenHeaderValueParameterName<?>> {

    TokenHeaderValueHeaderParser(final String text) {
        super(text);
    }

    @Override
    final void value() {
        this.token = this.value(RFC2045TOKEN, VALUE, TokenHeaderValue::with);
    }

    @Override
    void failMissingValue() {
        this.failEmptyToken(VALUE);
    }

    private final static String VALUE = "value";

    @Override
    final void parameterName() {
        this.parameterName(RFC2045TOKEN, TokenHeaderValueParameterName::with);
    }

    @Override
    void quotedParameterValue() {
        this.failInvalidCharacter();
    }

    @Override
    void unquotedParameterValue() {
        this.parameterValue(RFC2045TOKEN);
    }

    @Override
    final void missingParameterValue() {
        this.failMissingParameterValue();
    }

    @Override
    final void tokenEnd() {
        this.token = this.token.setParameters(this.parameters);
        this.tokenHeaderValueEnd();
    }

    abstract void tokenHeaderValueEnd();

    TokenHeaderValue token;
}

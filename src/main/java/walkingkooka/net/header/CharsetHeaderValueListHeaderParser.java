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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.HasQFactorWeight;

import java.util.List;

/**
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Charset"></a>
 * <pre>
 * Accept-Charset: <charset>
 *
 * // Multiple types, weighted with the quality value syntax:
 * Accept-Charset: utf-8, iso-8859-1;q=0.5
 * </pre>
 */
final class CharsetHeaderValueListHeaderParser extends HeaderParser<CharsetHeaderValueParameterName<?>> {

    static List<CharsetHeaderValue> parseCharsetHeaderValueList(final String text) {
        checkText(text, "header text");

        final CharsetHeaderValueListHeaderParser parser = new CharsetHeaderValueListHeaderParser(text);
        parser.parse();
        parser.list.sort(HasQFactorWeight.qFactorDescendingComparator());
        return Lists.readOnly(parser.list);
    }

    // @VisibleForTesting
    CharsetHeaderValueListHeaderParser(final String text) {
        super(text);
    }

    @Override
    void value() {
        this.token = CharsetHeaderValue.with(this.charsetName());
    }

    @Override
    void failMissingValue() {
        this.failEmptyToken("charset");
    }

    private CharsetName charsetName() {
        if (!this.hasMoreCharacters()) {
            failEmptyToken(CHARSET);
        }

        final char c = this.character();
        return HeaderValue.WILDCARD.character() == c ?
                charsetNameWildcard() :
                charsetNameNotWildcard();
    }

    private CharsetName charsetNameWildcard() {
        this.position++;
        return CharsetName.WILDCARD_CHARSET;
    }

    private CharsetName charsetNameNotWildcard() {
        final String initial = this.tokenText(CharsetName.INITIAL_CHAR_PREDICATE);
        if (initial.isEmpty()) {
            failInvalidCharacter();
        }

        return CharsetName.with(initial + this.tokenText(CharsetName.PART_CHAR_PREDICATE));
    }

    private final static String CHARSET = "charset";

    @Override
    void parameterName() {
        this.parseParameterName(RFC2045TOKEN, CharsetHeaderValueParameterName::with);
    }

    @Override
    void parameterValue() {
        this.parseParameterValue(RFC2045TOKEN);
    }

    @Override
    void separator() {
        // multiple charsets are ok!
    }

    @Override
    void missingParameterValue() {
        this.failEmptyParameterValue();
    }

    @Override
    void tokenEnd() {
        this.list.add(this.token.setParameters(this.parameters));
    }

    CharsetHeaderValue token;
    private final List<CharsetHeaderValue> list = Lists.array();
}

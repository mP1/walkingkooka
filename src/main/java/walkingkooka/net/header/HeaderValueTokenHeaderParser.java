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
 * A parser that creates a list of {@link HeaderValueToken}.
 */
final class HeaderValueTokenHeaderParser extends HeaderParser<HeaderValueTokenParameterName<?>> {

    static List<HeaderValueToken> parseHeaderValueTokenList(final String text) {
        checkText(text, "header");

        final HeaderValueTokenHeaderParser parser = new HeaderValueTokenHeaderParser(text);
        parser.parse();
        parser.list.sort(HasQFactorWeight.qFactorDescendingComparator());
        return Lists.readOnly(parser.list);
    }

    // @VisibleForTesting
    HeaderValueTokenHeaderParser(final String text) {
        super(text);
    }

    @Override final void value() {
        this.token = this.parseValue(RFC2045TOKEN, "value", this::createHeaderValueToken);
    }

    private HeaderValueToken createHeaderValueToken(final String value) {
        return HeaderValueToken.with(value);
    }

    @Override final void parameterName() {
        this.parseParameterName(RFC2045TOKEN, HeaderValueTokenParameterName::with);
    }

    @Override
    void parameterValue() {
        this.parseParameterValue(RFC2045TOKEN);
    }

    @Override final void missingParameterValue() {
        this.failEmptyParameterValue();
    }

    @Override final void tokenEnd() {
        this.list.add(this.token.setParameters(this.parameters));
    }

    @Override
    void separator() {
        // multiple tokens allowed.
    }

    HeaderValueToken token;

    private final List<HeaderValueToken> list = Lists.array();
}

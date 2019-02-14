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

import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

public abstract class HeaderParserTestCase<P extends HeaderParser, V> implements ClassTesting2<P>,
        ParseStringTesting<V>,
        ToStringTesting<P>,
        TypeNameTesting<P> {

    HeaderParserTestCase() {
        super();
    }

    // parse ...........................................................................................

    final void parseMissingClosingQuoteFails(final String text) {
        this.parseFails(text, HeaderParser.missingClosingQuote(text));
    }

    final void parseMissingValueFails(final String text) {
        this.parseMissingValueFails(text, text.length());
    }

    final void parseMissingValueFails(final String text, final int pos) {
        this.parseFails(text,
                HeaderParser.emptyToken(this.valueLabel(), pos, text));
    }

    abstract String valueLabel();

    final void parseMissingParameterNameFails(final String text) {
        this.parseMissingParameterNameFails(text, text.length());
    }

    final void parseMissingParameterNameFails(final String text, final int pos) {
        this.parseFails(text,
                HeaderParser.missingParameterName(pos, text));
    }

    final void parseMissingParameterValueFails(final String text) {
        this.parseMissingParameterValueFails(text, text.length());
    }

    final void parseMissingParameterValueFails(final String text, final int pos) {
        this.parseFails(text,
                HeaderParser.missingParameterValue(pos, text));
    }

    final void parseFails(final String text, final String message) {
        this.parseFails(text, new HeaderValueException(message));
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return new HeaderValueException(expected.getMessage(), expected);
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return HeaderValueException.class;
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return HeaderParser.class.getSimpleName();
    }
}

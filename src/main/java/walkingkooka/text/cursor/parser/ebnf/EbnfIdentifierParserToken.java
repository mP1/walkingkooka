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
package walkingkooka.text.cursor.parser.ebnf;

/**
 * Holds the text for an identifier. Identifiers may appear on the left of a definition or as a reference to another rule definition.
 */
public final class EbnfIdentifierParserToken extends EbnfLeafParserToken<EbnfIdentifierName> {

    static EbnfIdentifierParserToken with(final EbnfIdentifierName value, final String text) {
        checkValue(value);
        checkText(text);

        return new EbnfIdentifierParserToken(value, text);
    }

    private EbnfIdentifierParserToken(final EbnfIdentifierName value, final String text) {
        super(value, text);
    }

    // EbnfParserTokenVisitor............................................................................................

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfIdentifierParserToken;
    }

}

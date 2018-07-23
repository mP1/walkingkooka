/*
 *
 *  * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;

import java.util.Objects;

/**
 * The parser token representing the sign of a number. True means negative, false means positive
 */
public final class SignParserToken extends ParserTemplateToken<Boolean> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SignParserToken.class);

    static SignParserToken with(final boolean value, final String text) {
        Objects.requireNonNull(text, "text");

        return new SignParserToken(value, text);
    }

    private SignParserToken(final boolean value, final String text) {
        super(value, text);
    }

    @Override
    public SignParserToken setText(final String text){
        return Cast.to(this.setText0(text));
    }

    @Override
    SignParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public void accept(final ParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SignParserToken;
    }

    @Override
    boolean equals1(final ParserTemplateToken<?> other) {
        return true; // no extra properties to compare
    }

    @Override
    public String toString() {
        return this.value() ? "Negative" : "Positive";
    }
}

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
package walkingkooka.text.cursor.parser.json;

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * A wrapper around a numeric type that is also a percentage.
 */
public final class JsonNodeObjectParserToken extends JsonNodeParentParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeObjectParserToken.class);

    static JsonNodeObjectParserToken with(final List<ParserToken> value, final String text){
        return new JsonNodeObjectParserToken(copyAndCheckTokens(value),
                checkText(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private JsonNodeObjectParserToken(final List<ParserToken> value, final String text, final boolean computeWithout){
        super(value, text, computeWithout);
    }

    @Override
    public JsonNodeObjectParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeObjectParserToken replaceText(final String text) {
        return new JsonNodeObjectParserToken(this.value, text, WITHOUT_USE_THIS);
    }

    @Override
    JsonNodeParentParserToken replaceTokens(final List<ParserToken> tokens) {
        return new JsonNodeObjectParserToken(tokens, this.text(), WITHOUT_USE_THIS);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }
    
    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeObjectParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}

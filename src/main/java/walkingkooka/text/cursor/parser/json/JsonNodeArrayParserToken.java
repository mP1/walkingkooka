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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * A wrapper around a numeric type that is also a percentage.
 */
public final class JsonNodeArrayParserToken extends JsonNodeParentParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(JsonNodeArrayParserToken.class);

    static JsonNodeArrayParserToken with(final List<ParserToken> value, final String text){
        return new JsonNodeArrayParserToken(copyAndCheckTokens(value),
                checkText(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private JsonNodeArrayParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout){
        super(value, text, valueWithout);
    }

    @Override
    public JsonNodeArrayParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeArrayParserToken replaceText(final String text) {
        return new JsonNodeArrayParserToken(this.value, text, WITHOUT_COMPUTE_REQUIRED);
    }

    @Override
    JsonNodeParentParserToken replaceTokens(final List<ParserToken> tokens) {
        return new JsonNodeArrayParserToken(tokens, this.text(), tokens);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    JsonNode toJsonNodeOrNull() {
        final List<JsonNode> children = Lists.array();
        this.addJsonNode(children);

        return JsonNode.array().setChildren(children);
    }

    @Override
    final void addJsonNode(final List<JsonNode> children) {
        for(ParserToken element : this.value()) {
            if(element instanceof JsonNodeParserToken) {
                JsonNodeParserToken.class.cast(element).addJsonNode(children);
            }
        }
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeArrayParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}

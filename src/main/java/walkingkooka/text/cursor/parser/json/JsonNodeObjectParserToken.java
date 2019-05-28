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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * A wrapper around a json object.
 */
public final class JsonNodeObjectParserToken extends JsonNodeParentParserToken<JsonNodeObjectParserToken> {

    static JsonNodeObjectParserToken with(final List<ParserToken> value, final String text) {
        return new JsonNodeObjectParserToken(copyAndCheckTokens(value),
                checkText(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private JsonNodeObjectParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(value, text, valueWithout);
        this.checkKeys(text);
    }

    private void checkKeys(final String text) {
        final List<ParserToken> without = this.valueIfWithoutSymbolsOrNull();
        if (null != without) {
            int i = 0;
            JsonNodeParserToken j = null;

            for (ParserToken t : without) {
                j = JsonNodeParserToken.class.cast(t);
                if ((i & 1) == 0) {
                    if (!j.isString()) {
                        throw new IllegalArgumentException("Expected string key token, but got " + j + "=" + text);
                    }
                }
                i++;
            }
            if (1 == (i & 1)) {
                throw new IllegalArgumentException("Key " + j + " missing value=" + text);
            }
        }
    }

    @Override
    public JsonNodeObjectParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    JsonNodeObjectParserToken replaceText(final String text) {
        return new JsonNodeObjectParserToken(this.value, text, this.valueIfWithoutSymbolsOrNull());
    }

    @Override
    public JsonNodeObjectParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    JsonNodeParentParserToken replaceValue(final List<ParserToken> tokens, final List<ParserToken> without) {
        return new JsonNodeObjectParserToken(tokens, this.text(), without);
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
    JsonNode toJsonNodeOrNull() {
        JsonNodeStringParserToken key = null;

        final JsonObjectNode object = JsonNode.object();
        final List<JsonNode> objectChildren = Lists.array();

        for (ParserToken element : JsonNodeObjectParserToken.class.cast(this.withoutSymbols().get()).value()) {
            if (element instanceof JsonNodeParserToken) {
                final JsonNodeParserToken j = JsonNodeParserToken.class.cast(element);
                if (j.isNoise()) {
                    continue;
                }
                if (null == key) {
                    key = j.cast();
                } else {
                    final JsonNode node = j.toJsonNode().get();
                    objectChildren.add(node.setName(JsonNodeName.with(key.value())));
                    key = null;
                }
            }
        }

        return object.setChildren(objectChildren);
    }

    @Override
    final void addJsonNode(final List<JsonNode> children) {
        children.add(this.toJsonNodeOrNull());
    }

    // visitor ...............................................................................................

    @Override
    public void accept(final JsonNodeParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object ...............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof JsonNodeObjectParserToken;
    }
}

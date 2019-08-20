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
package walkingkooka.tree.json.parser;

import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class JsonNodeParentParserToken<T extends JsonNodeParentParserToken<T>> extends JsonNodeParserToken implements ParentParserToken<T> {

    JsonNodeParentParserToken(final List<ParserToken> value, final String text) {
        super(text);
        this.value = value;
    }

    // isXXX............................................................................................................

    @Override
    public final boolean isArrayBeginSymbol() {
        return false;
    }

    @Override
    public final boolean isArrayEndSymbol() {
        return false;
    }

    @Override
    public final boolean isBoolean() {
        return false;
    }

    @Override
    public final boolean isNull() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isObjectBeginSymbol() {
        return false;
    }

    @Override
    public final boolean isObjectEndSymbol() {
        return false;
    }

    @Override
    public final boolean isObjectAssignmentSymbol() {
        return false;
    }

    @Override
    public final boolean isSeparatorSymbol() {
        return false;
    }

    @Override
    public final boolean isString() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    // JsonNodeParserTokenVisitor.......................................................................................

    final void acceptValues(final JsonNodeParserTokenVisitor visitor) {
        for (ParserToken token : this.value()) {
            visitor.accept(token);
        }
    }
}

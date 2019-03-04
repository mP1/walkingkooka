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

package walkingkooka.tree.expression;

import walkingkooka.naming.NameTesting2;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

public final class ExpressionNodeNameTest implements ClassTesting2<ExpressionNodeName>,
        HasJsonNodeTesting<ExpressionNodeName>,
        NameTesting2<ExpressionNodeName, ExpressionNodeName> {

    @Override
    public ExpressionNodeName createName(final String name) {
        return ExpressionNodeName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "abc2";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "a1";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "-";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return NameTesting2.subtract(ASCII, this.possibleValidChars(position));
    }

    @Override
    public Class<ExpressionNodeName> type() {
        return ExpressionNodeName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // HasJsonNodeTesting............................................................................................

    @Override
    public ExpressionNodeName fromJsonNode(final JsonNode from) {
        return ExpressionNodeName.fromJsonNode(from);
    }

    @Override
    public ExpressionNodeName createHasJsonNode() {
        return this.createName("sum");
    }
}

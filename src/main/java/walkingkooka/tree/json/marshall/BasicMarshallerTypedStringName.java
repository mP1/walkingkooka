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

package walkingkooka.tree.json.marshall;

import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.json.JsonNode;

final class BasicMarshallerTypedStringName extends BasicMarshallerTyped<StringName> {

    static BasicMarshallerTypedStringName instance() {
        return new BasicMarshallerTypedStringName();
    }

    private BasicMarshallerTypedStringName() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<StringName> type() {
        return StringName.class;
    }

    @Override
    String typeName() {
        return "string-name";
    }

    @Override
    StringName fromJsonNodeNull(final FromJsonNodeContext context) {
        return null;
    }

    @Override
    StringName fromJsonNodeNonNull(final JsonNode node,
                                   final FromJsonNodeContext context) {
        return Names.string(node.stringValueOrFail());
    }

    @Override
    JsonNode toJsonNodeNonNull(final StringName value,
                               final ToJsonNodeContext context) {
        return JsonNode.string(value.toString());
    }
}

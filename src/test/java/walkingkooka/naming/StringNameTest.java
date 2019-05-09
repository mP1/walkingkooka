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
 */

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class StringNameTest implements ClassTesting2<StringName>,
        NameTesting<StringName, StringName>,
        HasJsonNodeTesting<StringName>,
        SerializationTesting<StringName> {

    private final static String TEXT = "bcd123";
    
    @Test
    public void testContainsSeparatorFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringName.with("name-" + StringPath.SEPARATOR.string());
        });
    }

    @Test
    public void testSerializeRootIsSingleton() throws Exception {
        this.serializeSingletonAndCheck(StringName.ROOT);
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(StringName.with(TEXT), JsonNode.string(TEXT));
    }

    @Test
    public void testFromJsonNode() {
        this.fromJsonNodeAndCheck(JsonNode.string(TEXT), StringName.with(TEXT));
    }

    @Override
    public StringName createName(final String name) {
        return StringName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return TEXT;
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "aa";
    }

    // ClassTesting...................................................................................................

    @Override
    public Class<StringName> type() {
        return StringName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // SerializableTesting.............................................................................................

    @Override
    public StringName serializableInstance() {
        return StringName.with("string");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    // HasJsonesting...................................................................................................

    @Override
    public StringName fromJsonNode(final JsonNode from) {
        return StringName.fromJsonNode(from);
    }

    @Override
    public StringName createHasJsonNode() {
        return this.createName(this.nameText());
    }

}

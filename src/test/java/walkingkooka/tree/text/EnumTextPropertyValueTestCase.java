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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class EnumTextPropertyValueTestCase<E extends Enum<?>> implements ClassTesting2<E> {

    EnumTextPropertyValueTestCase() {
        super();
    }

    @Test
    public final void testTextPropertyNode() {
        final TextNode properties = TextNode.style(TextStyleNode.NO_CHILDREN)
                .setAttributes(Maps.of(this.textStylePropertyName(), this.value()));
        final JsonNode json = properties.toJsonNodeWithType();
        assertEquals(properties,
                json.fromJsonNodeWithType(),
                () -> "" + properties);
    }

    @Test
    public final void testTextStylePropertyNameCheck() {
        this.textStylePropertyName().check(this.value());
    }

    abstract TextStylePropertyName<E> textStylePropertyName();

    abstract E value();

    // ClassTyping......................................................................................................

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

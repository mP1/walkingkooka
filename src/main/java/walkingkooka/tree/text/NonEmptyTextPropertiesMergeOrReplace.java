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

import walkingkooka.collect.map.Maps;

import java.util.Map;

enum NonEmptyTextPropertiesMergeOrReplace {
    MERGE {
        @Override
        TextNode accept0(final TextPropertiesNode node, final TextProperties textProperties) {
            final Map<TextPropertyName<?>, Object> merged = Maps.sorted();
            merged.putAll(node.attributes());
            merged.putAll(textProperties.value());

            return node.setAttributes(textProperties.value());
        }
    },
    REPLACE {
        @Override
        TextNode accept0(final TextPropertiesNode node, final TextProperties textProperties) {
            return node.setAttributes(textProperties.value());
        }
    };

    final TextNode accept(final TextPropertiesNode node, final TextProperties textProperties) {
        return textProperties.isEmpty() ?
                node :
                this.accept0(node, textProperties);
    }

    abstract TextNode accept0(final TextPropertiesNode node, final TextProperties textProperties);
}

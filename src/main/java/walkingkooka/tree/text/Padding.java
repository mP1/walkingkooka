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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

abstract class Padding extends LengthTextStylePropertyValue implements HasJsonNode {

    static void paddingCheck(final Length<?> length) {
        check(length);
        length.pixelOrFail();
    }

    Padding(final Length<?> length) {
        super(length);
    }

    // HasJsonNode.....................................................................................................

    @Override
    public final JsonNode toJsonNode() {
        return this.length.toJsonNode();
    }
}
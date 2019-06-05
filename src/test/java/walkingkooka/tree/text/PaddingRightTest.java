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

import walkingkooka.tree.json.JsonNode;

public final class PaddingRightTest extends PaddingTestCase<PaddingRight> {

    @Override
    PaddingRight createPropertyValue(final Length length) {
        return PaddingRight.with(length);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public PaddingRight parse(final String text) {
        return PaddingRight.parse(text);
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public PaddingRight fromJsonNode(final JsonNode from) {
        return PaddingRight.fromJsonNode(from);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PaddingRight> type() {
        return PaddingRight.class;
    }
}

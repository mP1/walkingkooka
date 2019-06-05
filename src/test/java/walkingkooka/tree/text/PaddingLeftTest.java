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

public final class PaddingLeftTest extends PaddingTestCase<PaddingLeft> {

    @Override
    PaddingLeft createPropertyValue(final Length length) {
        return PaddingLeft.with(length);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public PaddingLeft parse(final String text) {
        return PaddingLeft.parse(text);
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public PaddingLeft fromJsonNode(final JsonNode from) {
        return PaddingLeft.fromJsonNode(from);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PaddingLeft> type() {
        return PaddingLeft.class;
    }
}

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
import walkingkooka.tree.json.JsonNode;

public final class TabSizeTest extends LengthTextStylePropertyValueTestCase<TabSize> {

    @Test
    public void testWithNumber() {
        this.withAndCheck(Length.number(1));
    }

    @Test
    public void testWithNormalFails() {
        this.withFails(Length.normal());
    }

    @Test
    public void testWithPixelFails() {
        this.withFails(Length.pixel(12.0));
    }

    @Override
    TabSize createPropertyValue(final Length length) {
        return TabSize.with(length);
    }

    @Override
    Length<?> length() {
        return Length.number(12);
    }

    @Override
    Length<?> differentLength() {
        return Length.number(99);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public TabSize parse(final String text) {
        return TabSize.parse(text);
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public TabSize fromJsonNode(final JsonNode from) {
        return TabSize.fromJsonNode(from);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TabSize> type() {
        return TabSize.class;
    }
}

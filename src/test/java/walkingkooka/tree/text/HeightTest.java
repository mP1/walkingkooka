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

public final class HeightTest extends LengthTextStylePropertyValueTestCase<Height> {

    @Test
    public void testWithNumberFails() {
        this.withFails(Length.number(1));
    }

    @Test
    public void testWithNormal() {
        this.withFails(Length.normal());
    }

    @Test
    public void testWithPixel() {
        this.withAndCheck(Length.pixel(12.0));
    }

    @Override
    Height createPropertyValue(final Length length) {
        return Height.with(length);
    }

    @Override
    Length<?> length() {
        return Length.pixel(12.5);
    }

    @Override
    Length<?> differentLength() {
        return Length.pixel(99.0);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public Height parse(final String text) {
        return Height.parse(text);
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public Height fromJsonNode(final JsonNode from) {
        return Height.fromJsonNode(from);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<Height> type() {
        return Height.class;
    }
}
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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.FakeNode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NormalLengthPixelLengthTextStylePropertyValueHandlerTest extends TextStylePropertyValueHandlerTestCase<NormalLengthPixelLengthTextStylePropertyValueHandler, Length<?>> {

    @Test
    public void testCheckNormal() {
        this.check(Length.normal());
    }

    @Test
    public void testCheckNumberFails() {
        this.checkFails(Length.number(1L), "Property \"line-height\" value 1(NumberLength) is not a NormalLength|PixelLength");
    }

    @Test
    public void testCheckPixel() {
        this.check(Length.pixel(1.5));
    }

    @Test
    public final void testCheckWrongValueTypeFails() {
        this.checkFails(this, "Property " + this.propertyName().inQuotes() + " value " + this + "(" + this.getClass().getSimpleName() + ") is not a " + this.propertyValueType());
    }

    @Test
    public final void testCheckWrongValueTypeFails2() {
        final FakeNode fakeNode = new FakeNode();
        this.checkFails(fakeNode, "Property " + this.propertyName().inQuotes() + " value " + fakeNode + "(" + FakeNode.class.getName() + ") is not a " + this.propertyValueType());
    }

    @Test
    public void testFromJsonNodeNoneFails() {
        assertThrows(TextStylePropertyValueException.class,() -> {
            this.handler().fromJsonNode(Length.none().toJsonNode(), this.propertyName());
        });
    }

    @Test
    public void testFromJsonNodeNormal() {
        final NormalLength normal = Length.normal();
        this.fromJsonNodeAndCheck(normal.toJsonNode(), normal);
    }

    @Test
    public void testFromJsonNodeNumberFails() {
        assertThrows(TextStylePropertyValueException.class,() -> {
            this.handler().fromJsonNode(Length.number(1).toJsonNode(), this.propertyName());
        });
    }

    @Test
    public void testFromJsonNodePixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.fromJsonNodeAndCheck(pixel.toJsonNode(), pixel);
    }

    @Test
    public void testToJsonNodeNormal() {
        final NormalLength normal = Length.normal();
        this.toJsonNodeAndCheck(normal, normal.toJsonNode());
    }

    @Test
    public void testToJsonNodePixel() {
        final PixelLength pixel = Length.pixel(1.0);
        this.toJsonNodeAndCheck(pixel, pixel.toJsonNode());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.handler(), "NormalLength|PixelLength");
    }

    @Override
    NormalLengthPixelLengthTextStylePropertyValueHandler handler() {
        return NormalLengthPixelLengthTextStylePropertyValueHandler.INSTANCE;
    }

    @Override
    TextStylePropertyName<Length<?>> propertyName() {
        return TextStylePropertyName.LINE_HEIGHT;
    }

    @Override
    Length<?> propertyValue() {
        return Length.pixel(1.0);
    }

    @Override
    String propertyValueType() {
        return "NormalLength|PixelLength";
    }

    @Override
    public String typeNamePrefix() {
        return NormalLength.class.getSimpleName() + PixelLength.class.getSimpleName();
    }

    @Override
    public Class<NormalLengthPixelLengthTextStylePropertyValueHandler> type() {
        return NormalLengthPixelLengthTextStylePropertyValueHandler.class;
    }
}

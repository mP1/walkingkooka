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
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class BorderTest extends BorderMarginPaddingTestCase<Border> {

    // color............................................................................................................

    @Test
    public final void testColor() {
        final ColorHslOrHsv color = ColorHslOrHsv.parse("red");
        final Direction direction = Direction.BOTTOM;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_BOTTOM_COLOR, color));
        assertEquals(Optional.of(color), border.color(), "color");
    }

    @Test
    public final void testSetColorSame() {
        final ColorHslOrHsv color = ColorHslOrHsv.parse("blue");
        final Direction direction = Direction.TOP;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_TOP_COLOR, color));
        assertSame(border, border.setColor(Optional.of(color)));
    }

    @Test
    public final void testSetColorDifferent() {
        final ColorHslOrHsv color = ColorHslOrHsv.parse("lime");
        final Direction direction = Direction.LEFT;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_LEFT_COLOR, color));

        final ColorHslOrHsv differentColor = ColorHslOrHsv.parse("yellow");
        final Border different = border.setColor(Optional.of(differentColor));

        assertNotSame(border, different);
        assertEquals(this.textStyle(TextStylePropertyName.BORDER_LEFT_COLOR, differentColor), different.textStyle());
    }

    @Test
    public final void testSetColorDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseColor("aqua"));

        final Direction direction = Direction.RIGHT;
        final Border border = direction.border(TextStyle.with(properties));

        final ColorHslOrHsv differentColor = ColorHslOrHsv.parse("yellow");
        final Border different = border.setColor(Optional.of(differentColor));

        assertNotSame(border, different);

        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, differentColor);
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    @Test
    public final void testSetColorRemoved() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseColor("aqua"));

        final Direction direction = Direction.RIGHT;
        final Border border = direction.border(TextStyle.with(properties));

        final Border different = border.setColor(Optional.empty());

        assertNotSame(border, different);

        properties.remove(TextStylePropertyName.BORDER_RIGHT_COLOR);
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    // style............................................................................................................

    @Test
    public final void testStyle() {
        final BorderStyle style = BorderStyle.DOTTED;
        final Direction direction = Direction.BOTTOM;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_BOTTOM_STYLE, style));
        assertEquals(Optional.of(style), border.style(), "style");
    }

    @Test
    public final void testSetStyleSame() {
        final BorderStyle style = BorderStyle.DASHED;
        final Direction direction = Direction.TOP;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_TOP_STYLE, style));
        assertSame(border, border.setStyle(Optional.of(style)));
    }

    @Test
    public final void testSetStyleDifferent() {
        final BorderStyle style = BorderStyle.HIDDEN;
        final Direction direction = Direction.LEFT;
        final Border border = direction.border(this.textStyle(TextStylePropertyName.BORDER_LEFT_STYLE, style));

        final BorderStyle differentStyle = BorderStyle.GROOVE;
        final Border different = border.setStyle(Optional.of(differentStyle));

        assertNotSame(border, different);
        assertEquals(this.textStyle(TextStylePropertyName.BORDER_LEFT_STYLE, differentStyle), different.textStyle());
    }

    @Test
    public final void testSetStyleDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.OUTSET);

        final Direction direction = Direction.RIGHT;
        final Border border = direction.border(TextStyle.with(properties));

        final BorderStyle differentStyle = BorderStyle.INSET;
        final Border different = border.setStyle(Optional.of(differentStyle));

        assertNotSame(border, different);

        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, differentStyle);
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    @Test
    public final void testSetStyleRemoved() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#333"));
        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOUBLE);

        final Direction direction = Direction.RIGHT;
        final Border border = direction.border(TextStyle.with(properties));

        final Border different = border.setStyle(Optional.empty());

        assertNotSame(border, different);

        properties.remove(TextStylePropertyName.BORDER_RIGHT_STYLE);
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    // helpers..........................................................................................................

    @Override
    Border createBorderMarginPadding(final Direction direction, final TextStyle textStyle) {
        return direction.border(textStyle);
    }
    
    @Override
    TextStylePropertyName<Length<?>> widthPropertyName(final Direction direction) {
        return direction.borderWidthPropertyName();
    }

    @Override
    public Class<Border> type() {
        return Border.class;
    }
}

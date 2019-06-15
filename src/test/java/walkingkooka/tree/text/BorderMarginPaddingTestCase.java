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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BorderMarginPaddingTestCase<T extends BorderMarginPadding> implements ClassTesting2<T>,
        HashCodeEqualsDefinedTesting<T> {

    BorderMarginPaddingTestCase() {
        super();
    }

    @Test
    public final void testWithNullTextStyleFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBorderMarginPadding(Direction.RIGHT, null);
        });
    }

    @Test
    public final void testWithEmptyTextStyle() {
        final TextStyle textStyle = TextStyle.EMPTY;

        for (Direction direction : Direction.values()) {
            final T borderMarginPadding = this.createBorderMarginPadding(direction, textStyle);
            this.check(borderMarginPadding, direction, textStyle);
            assertSame(borderMarginPadding, this.createBorderMarginPadding(direction, textStyle));
        }
    }

    @Test
    public final void testWithNonEmptyTextStyle() {
        final TextStyle textStyle = this.textStyle();

        for (Direction direction : Direction.values()) {
            this.check(this.createBorderMarginPadding(direction, textStyle), direction, textStyle);
        }
    }

    // direction........................................................................................................

    @Test
    public final void testDirection() {
        final TextStyle textStyle = this.textStyle();

        for (Direction direction : Direction.values()) {
            assertEquals(direction, this.createBorderMarginPadding(direction, textStyle).direction(), "direction");
        }
    }

    @Test
    public final void testSetDirectionSame() {
        final TextStyle textStyle = this.textStyle();

        for (Direction direction : Direction.values()) {
            final T borderMarginPadding = this.createBorderMarginPadding(direction, textStyle);
            assertSame(borderMarginPadding, borderMarginPadding.setDirection(direction));
        }
    }

    @Test
    public final void testSetDirectionDifferent() {
        final TextStyle textStyle = this.textStyle();

        final T borderMarginPadding = this.createBorderMarginPadding(Direction.LEFT, textStyle);
        final BorderMarginPadding different = borderMarginPadding.setDirection(Direction.RIGHT);
        assertNotSame(borderMarginPadding, different);
        this.check(different, Direction.RIGHT, textStyle);
    }

    // width............................................................................................................

    @Test
    public final void testWidth() {
        final Length<?> width = Length.pixel(2.5);
        final Direction direction = Direction.BOTTOM;
        final T borderMarginPadding = this.createBorderMarginPadding(direction, this.textStyle(this.widthPropertyName(direction), width));
        assertEquals(Optional.of(width), borderMarginPadding.width(), "width");
    }

    @Test
    public final void testSetWidthSame() {
        final Length<?> width = Length.pixel(2.5);
        final Direction direction = Direction.TOP;
        final T borderMarginPadding = this.createBorderMarginPadding(direction, this.textStyle(this.widthPropertyName(direction), width));
        assertSame(borderMarginPadding, borderMarginPadding.setWidth(Optional.of(width)));
    }

    @Test
    public final void testSetWidthDifferent() {
        final Length<?> width = Length.pixel(2.5);
        final Direction direction = Direction.LEFT;
        final TextStylePropertyName<Length<?>> propertyName = this.widthPropertyName(direction);
        final T borderMarginPadding = this.createBorderMarginPadding(direction, this.textStyle(propertyName, width));

        final Length<?> differentWidth = Length.pixel(99.0);
        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.of(differentWidth));

        assertNotSame(borderMarginPadding, different);
        assertEquals(this.textStyle(propertyName, differentWidth), different.textStyle());
    }

    @Test
    public final void testSetWidthDifferent2() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#333"));

        final Direction direction = Direction.RIGHT;
        final T borderMarginPadding = this.createBorderMarginPadding(direction, TextStyle.with(properties));

        final Length<?> differentWidth = Length.pixel(99.0);
        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.of(differentWidth));

        assertNotSame(borderMarginPadding, different);

        properties.put(this.widthPropertyName(direction), differentWidth);
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    @Test
    public final void testSetWidthRemoved() {
        final Direction direction = Direction.RIGHT;

        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("#444"));
        properties.put(this.widthPropertyName(direction), Length.pixel(1.5));

        final T borderMarginPadding = this.createBorderMarginPadding(direction, TextStyle.with(properties));

        final BorderMarginPadding different = borderMarginPadding.setWidth(Optional.empty());

        assertNotSame(borderMarginPadding, different);

        properties.remove(this.widthPropertyName(direction));
        assertEquals(TextStyle.with(properties), different.textStyle());
    }

    // equals...........................................................................................................

    @Test
    public final void testDifferentDirection() {
        final TextStyle textStyle = this.textStyle();
        this.checkNotEquals(this.createBorderMarginPadding(Direction.LEFT, textStyle),
                this.createBorderMarginPadding(Direction.RIGHT, textStyle));
    }

    @Test
    public final void testDifferentTextStyle() {
        final Direction direction = Direction.RIGHT;
        this.checkNotEquals(this.createBorderMarginPadding(direction, TextStyle.EMPTY),
                this.createBorderMarginPadding(direction, this.textStyle()));
    }

    // helpers..........................................................................................................

    abstract T createBorderMarginPadding(final Direction direction,
                                         final TextStyle textStyle);

    abstract TextStylePropertyName<Length<?>> widthPropertyName(final Direction direction);

    private TextStyle textStyle() {
        return this.textStyle(TextStylePropertyName.TEXT_COLOR, Color.fromArgb(0x123456));
    }

    final <TT> TextStyle textStyle(final TextStylePropertyName<TT> propertyName, final TT value) {
        return TextStyle.with(Maps.of(propertyName, value));
    }

    final void check(final BorderMarginPadding borderMarginPadding,
                     final Direction direction,
                     final TextStyle textStyle) {
        assertEquals(direction, borderMarginPadding.direction(), "direction");
        assertEquals(textStyle, borderMarginPadding.textStyle(), "textStyle");
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefined............................................................................................

    @Override
    public final T createObject() {
        return this.createBorderMarginPadding(Direction.LEFT, this.textStyle());
    }
}

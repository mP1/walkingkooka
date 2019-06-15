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

import walkingkooka.color.ColorHslOrHsv;

/**
 * One of the four direction around a box with methods to retrieve {@link Border}, {@link Margin} and {@link Padding}
 * style view.
 */
public enum Direction {
    BOTTOM {
        @Override
        public Direction flip() {
            return TOP;
        }

        @Override
        TextStylePropertyName<ColorHslOrHsv> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_COLOR;
        }

        @Override
        TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_STYLE;
        }

        @Override
        TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_BOTTOM_WIDTH;
        }

        @Override
        TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_BOTTOM;
        }

        @Override
        TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_BOTTOM;
        }
    },
    LEFT {
        @Override
        public Direction flip() {
            return RIGHT;
        }

        @Override
        TextStylePropertyName<ColorHslOrHsv> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_LEFT_COLOR;
        }

        @Override
        TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_LEFT_STYLE;
        }

        @Override
        TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_LEFT_WIDTH;
        }

        @Override
        TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_LEFT;
        }

        @Override
        TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_LEFT;
        }
    },
    RIGHT {
        @Override
        public Direction flip() {
            return LEFT;
        }

        @Override
        TextStylePropertyName<ColorHslOrHsv> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_COLOR;
        }

        @Override
        TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_STYLE;
        }

        @Override
        TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_RIGHT_WIDTH;
        }

        @Override
        TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_RIGHT;
        }

        @Override
        TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_RIGHT;
        }
    },
    TOP {
        @Override
        public Direction flip() {
            return BOTTOM;
        }

        @Override
        TextStylePropertyName<ColorHslOrHsv> borderColorPropertyName() {
            return TextStylePropertyName.BORDER_TOP_COLOR;
        }

        @Override
        TextStylePropertyName<BorderStyle> borderStylePropertyName() {
            return TextStylePropertyName.BORDER_TOP_STYLE;
        }

        @Override
        TextStylePropertyName<Length<?>> borderWidthPropertyName() {
            return TextStylePropertyName.BORDER_TOP_WIDTH;
        }

        @Override
        TextStylePropertyName<Length<?>> marginPropertyName() {
            return TextStylePropertyName.MARGIN_TOP;
        }

        @Override
        TextStylePropertyName<Length<?>> paddingPropertyName() {
            return TextStylePropertyName.PADDING_TOP;
        }
    };

    public abstract Direction flip();

    // border...........................................................................................................

    /**
     * Creates a {@link Border} pre-populated using the {@link TextStyle}.
     */
    public final Border border(final TextStyle style) {
        return style.border(this);
    }

    /**
     * Singleton for each direction.
     */
    final Border emptyBorder = Border.with(this, TextStyle.EMPTY);

    // margin...........................................................................................................

    /**
     * Creates a {@link Margin} pre-populated using the {@link TextStyle}.
     */
    public final Margin margin(final TextStyle style) {
        return style.margin(this);
    }

    /**
     * Singleton for each direction.
     */
    final Margin emptyMargin = Margin.with(this, TextStyle.EMPTY);

    // padding..........................................................................................................

    /**
     * Creates a {@link Padding} pre-populated using the {@link TextStyle}.
     */
    public final Padding padding(final TextStyle style) {
        return style.padding(this);
    }

    /**
     * Singleton for each direction.
     */
    final Padding emptyPadding = Padding.with(this, TextStyle.EMPTY);

    // property names...................................................................................................

    abstract TextStylePropertyName<ColorHslOrHsv> borderColorPropertyName();

    abstract TextStylePropertyName<BorderStyle> borderStylePropertyName();

    abstract TextStylePropertyName<Length<?>> borderWidthPropertyName();

    abstract TextStylePropertyName<Length<?>> marginPropertyName();

    abstract TextStylePropertyName<Length<?>> paddingPropertyName();
}

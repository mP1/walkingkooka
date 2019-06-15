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

import java.util.Optional;

/**
 * Provides a bean like view of a padding.
 */
public final class Padding extends BorderMarginPadding {

    static Padding with(final Direction direction, final TextStyle textStyle) {
        return new Padding(direction, textStyle);
    }

    private Padding(final Direction direction, final TextStyle textStyle) {
        super(direction, textStyle);
    }

    @Override
    public Padding setDirection(final Direction direction) {
        return this.setDirection0(direction).cast();
    }

    @Override
    public Padding setWidth(final Optional<Length<?>> width) {
        return this.setWidth0(width).cast();
    }

    @Override
    TextStylePropertyName<Length<?>> widthPropertyName() {
        return this.direction.paddingPropertyName();
    }

    @Override
    BorderMarginPadding replace(final Direction direction, final TextStyle textStyle) {
        return new Padding(direction, textStyle);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Padding;
    }
}

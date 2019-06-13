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
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class DirectionTest implements ClassTesting2<Direction> {

    @Test
    public void testFlipBottom() {
        this.flipAndCheck(Direction.BOTTOM, Direction.TOP);
    }

    @Test
    public void testFlipLeft() {
        this.flipAndCheck(Direction.LEFT, Direction.RIGHT);
    }

    @Test
    public void testFlipRight() {
        this.flipAndCheck(Direction.RIGHT, Direction.LEFT);
    }

    @Test
    public void testFlipTop() {
        this.flipAndCheck(Direction.TOP, Direction.BOTTOM);
    }

    private void flipAndCheck(final Direction direction, final Direction expected) {
        assertSame(expected, direction.flip(), () -> direction + " flip");
    }

    @Override
    public Class<Direction> type() {
        return Direction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

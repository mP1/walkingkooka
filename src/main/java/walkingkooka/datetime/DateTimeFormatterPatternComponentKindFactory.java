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

package walkingkooka.datetime;

enum DateTimeFormatterPatternComponentKindFactory {

    /**
     * Tests the length and returns one of the following:
     * <ul>
     * <li>{@link DateTimeFormatterPatternComponentKind#SHORT_TEXT}</li>
     * <li>{@link DateTimeFormatterPatternComponentKind#LONG_TEXT}</li>
     * <li>{@link DateTimeFormatterPatternComponentKind#NARROW_TEXT}</li>
     * </ul>
     */
    TEXT {
        @Override
        DateTimeFormatterPatternComponentKind kind(final int width) {
            DateTimeFormatterPatternComponentKind kind;

            switch (width) {
                case 1:
                case 2:
                case 3:
                    kind = DateTimeFormatterPatternComponentKind.SHORT_TEXT;
                    break;
                case 4:
                    kind = DateTimeFormatterPatternComponentKind.LONG_TEXT;
                    break;
                default:
                    kind = DateTimeFormatterPatternComponentKind.NARROW_TEXT;
                    break;
            }

            return kind;
        }
    },
    /**
     * Always returns {@link DateTimeFormatterPatternComponentKind} after testing the width.
     */
    NUMBER_OR_TEXT {
        @Override
        DateTimeFormatterPatternComponentKind kind(final int width) {
            DateTimeFormatterPatternComponentKind kind;

            switch (width) {
                case 1:
                case 2:
                    kind = DateTimeFormatterPatternComponentKind.NUMBER;
                    break;
                case 3:
                    kind = DateTimeFormatterPatternComponentKind.SHORT_TEXT;
                    break;
                case 4:
                    kind = DateTimeFormatterPatternComponentKind.LONG_TEXT;
                    break;
                default:
                    kind = DateTimeFormatterPatternComponentKind.NARROW_TEXT;
                    break;
            }

            return kind;
        }
    };

    abstract DateTimeFormatterPatternComponentKind kind(final int width);
}
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

package walkingkooka.text.spreadsheetformat;

import walkingkooka.DecimalNumberContext;
import walkingkooka.color.Color;

/**
 * Context that accompanies a value format, holding local sensitive attributes such as the decimal point character.
 */
public interface SpreadsheetTextFormatContext extends DecimalNumberContext {

    /**
     * Returns the selected AM or PM given the hour of the day (24 hour time).
     */
    String ampm(int hourOfDay);

    /**
     * Returns the {@link Color} with the given number.
     */
    Color colorNumber(int number);

    /**
     * Returns the {@link Color} with the given name.
     */
    Color colorName(String name);

    /**
     * Returns the requested month in full. The month is zero index.
     */
    String monthName(int month);

    /**
     * Returns the requested month in abbreviated form. The month is zero index.
     */
    String monthNameAbbreviation(int month);

    /**
     * Returns the requested week day in full. Sunday is 0.
     */
    String weekDayName(int day);

    /**
     * Returns the requested week day in abbreviated form. Sunday is 0.
     */
    String weekDayNameAbbreviation(int day);

    /**
     * The width of the "cell" in characters.
     * This value affects STAR operator.
     */
    int width();

    /**
     * Handles converting the given value to the target.
     */
    <T> T convert(final Object value, final Class<T> target);
}

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

import walkingkooka.type.PublicStaticHelper;

public final class SpreadsheetTextFormatters implements PublicStaticHelper {

    /**
     * {@see FakeSpreadsheetTextFormatter}
     */
    public static <V> SpreadsheetTextFormatter<V> fake() {
        return new FakeSpreadsheetTextFormatter<V>();
    }

    /**
     * {@see LocalDateTimeSpreadsheetTextFormatter}
     */
    static SpreadsheetTextFormatter dateTime(final String pattern,
                                             final SpreadsheetTextFormatter general) {
        return LocalDateTimeSpreadsheetTextFormatter.with(pattern, general);
    }

    /**
     * {@see TextSpreadsheetTextFormatter}
     */
    public static SpreadsheetTextFormatter<String> text(final String pattern) {
        return TextSpreadsheetTextFormatter.parse(pattern);
    }

    /**
     * Stops creation
     */
    private SpreadsheetTextFormatters() {
        throw new UnsupportedOperationException();
    }
}

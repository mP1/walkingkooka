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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;
import java.util.List;

public final class CsvTest implements PublicStaticHelperTesting<Csv> {

    @Test
    public void testToCsvWithComma() {
        this.toCsvAndCheck(
            Lists.of("11", "22", "33"),
            ',',
            "11,22,33"
        );
    }

    @Test
    public void testToCsvWithSemiColon() {
        this.toCsvAndCheck(
            Lists.of("11", "22", "33"),
            ';',
            "11;22;33"
        );
    }

    private void toCsvAndCheck(final List<String> list,
                               final char separator,
                               final String expected) {
        this.checkEquals(
            expected,
            Csv.toCsv(
                list,
                separator
            ),
            () -> list + " toCsv " + CharSequences.quoteIfChars(separator)
        );
    }

    // class............................................................................................................

    @Override
    public Class<Csv> type() {
        return Csv.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}

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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DelimiterSeparatedValuesTest implements ClassTesting<DelimiterSeparatedValues> {

    // forCharacter.....................................................................................................

    @Test
    public void testForCharacterWithUnsupportedCharacterFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> DelimiterSeparatedValues.forCharacter('@')
        );
    }

    @Test
    public void testForCharacterWithComma() {
        assertSame(
            DelimiterSeparatedValues.CSV,
            DelimiterSeparatedValues.forCharacter(',')
        );
    }

    // csv..............................................................................................................

    @Test
    public void testCsvEmptyList() {
        assertSame(
            CsvStringList.EMPTY,
            DelimiterSeparatedValues.CSV.emptyList()
        );
    }

    @Test
    public void testCsvParseList() {
        final String string = "a,b,c";

        this.checkEquals(
            CsvStringList.parse(string),
            DelimiterSeparatedValues.CSV.parseList(string)
        );
    }

    @Test
    public void testCsvCharacter() {
        this.checkEquals(
            ',',
            DelimiterSeparatedValues.CSV.character()
        );
    }

    // class............................................................................................................

    @Override
    public Class<DelimiterSeparatedValues> type() {
        return DelimiterSeparatedValues.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

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

package walkingkooka.collect.list;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.PublicClassTesting;

import java.util.List;

public final class DelimiterSeparatedValuesKindTest implements PublicClassTesting<DelimiterSeparatedValuesKind<?>> {

    // character........................................................................................................

    @Test
    public void testCharacterWithComma() {
        this.characterAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            ','
        );
    }

    @Test
    public void testCharacterWithTab() {
        this.characterAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            '\t'
        );
    }

    private void characterAndCheck(final DelimiterSeparatedValuesKind<?> values,
                                   final char expected) {
        this.checkEquals(
            expected,
            values.character()
        );
    }

    // setElements......................................................................................................

    private final static List<String> ELEMENTS = Lists.of(
        "AA",
        "BB",
        "CC"
    );

    @Test
    public void testSetElementsWithComma() {
        this.setElementsAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            ELEMENTS,
            CsvStringList.EMPTY.setElements(ELEMENTS)
        );
    }

    @Test
    public void testSetElementsWithTab() {
        this.setElementsAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            ELEMENTS,
            TsvStringList.EMPTY.setElements(ELEMENTS)
        );
    }

    private <T extends CsvTsvStringList<T>> void setElementsAndCheck(final DelimiterSeparatedValuesKind<T> values,
                                                                     final List<String> elements,
                                                                     final T expected) {
        this.checkEquals(
            expected,
            values.setElements(elements)
        );
    }

    // empty............................................................................................................

    @Test
    public void testEmptyWithComma() {
        this.emptyAndCheck(
            DelimiterSeparatedValuesKind.COMMA,
            CsvStringList.EMPTY
        );
    }

    @Test
    public void testEmptyWithTab() {
        this.emptyAndCheck(
            DelimiterSeparatedValuesKind.TAB,
            TsvStringList.EMPTY
        );
    }

    private <T extends CsvTsvStringList<T>> void emptyAndCheck(final DelimiterSeparatedValuesKind<T> values,
                                                               final T expected) {
        this.checkEquals(
            expected,
            values.empty()
        );
    }

    // class............................................................................................................

    @Override
    public Class<DelimiterSeparatedValuesKind<?>> type() {
        return Cast.to(DelimiterSeparatedValuesKind.class);
    }
}

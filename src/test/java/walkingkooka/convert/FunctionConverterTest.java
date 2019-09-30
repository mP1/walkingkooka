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

package walkingkooka.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FunctionConverterTest extends ConverterTestCase2<FunctionConverter<String, Boolean>> {

    private final static Class<String> SOURCE_TYPE = String.class;
    private final static Class<Boolean> TARGET_TYPE = Boolean.class;
    private final static Function<String, Boolean> CONVERTER = Boolean::valueOf;

    @Test
    public void testWithNullSourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            FunctionConverter.with(null, TARGET_TYPE, CONVERTER);
        });
    }

    @Test
    public void testWithNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            FunctionConverter.with(SOURCE_TYPE, null, CONVERTER);
        });
    }

    @Test
    public void testWithNullConverterFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            FunctionConverter.with(null, TARGET_TYPE, null);
        });
    }

    @Test
    public void testWithSameSourceAndTargetTypesFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            FunctionConverter.with(SOURCE_TYPE, SOURCE_TYPE, String::valueOf);
        });
    }

    // converter........................................................................................................

    @Test
    public void testTrue() {
        this.convertAndCheck2(Boolean.TRUE.toString(), true);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck2(Boolean.FALSE.toString(), false);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "String->Boolean");
    }

    // Converter........................................................................................................

    @Override
    public FunctionConverter<String, Boolean> createConverter() {
        return FunctionConverter.with(String.class, Boolean.class, Boolean::valueOf);
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    private void convertAndCheck2(final String value,
                                  final boolean expected) {
        this.convertAndCheck(value, Boolean.class, expected);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<FunctionConverter<String, Boolean>> type() {
        return Cast.to(FunctionConverter.class);
    }
}

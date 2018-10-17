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
package walkingkooka.convert;

import org.junit.Test;
import walkingkooka.Cast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class CustomToStringConverterTest extends ConverterTestCase<CustomToStringConverter> {

    private final static Converter WRAPPED = Converters.string();
    private final static String CUSTOM_TO_STRING = "!!custom-to-string!!";

    @Test(expected = NullPointerException.class)
    public void testWrapNullConverterFails() {
        CustomToStringConverter.wrap(null, CUSTOM_TO_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testWrapNullToStringFails() {
        CustomToStringConverter.wrap(WRAPPED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapEmptyToStringFails() {
        CustomToStringConverter.wrap(WRAPPED, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWhitespaceToStringFails() {
        CustomToStringConverter.wrap(WRAPPED, " \t");
    }

    @Test
    public void testDoesntWrapEquivalentToString() {
        assertSame(WRAPPED, CustomToStringConverter.wrap(WRAPPED, WRAPPED.toString()));
    }

    @Test
    public void testUnwrapOtherCustomToStringConverter() {
        final Converter first = CustomToStringConverter.wrap(WRAPPED, "different");
        final CustomToStringConverter wrapped = Cast.to(CustomToStringConverter.wrap(first, CUSTOM_TO_STRING));
        assertNotSame(first, wrapped);
        assertSame("wrapped converter", WRAPPED, wrapped.converter);
        assertSame("wrapped toString", CUSTOM_TO_STRING, wrapped.toString);
    }

    @Test
    public void testConvert() {
        this.convertAndCheck(123, String.class, String.valueOf(123));
    }

    @Test
    public void testToString() {
        assertEquals(CUSTOM_TO_STRING, this.createConverter().toString());
    }

    @Override
    protected CustomToStringConverter createConverter() {
        return Cast.to(CustomToStringConverter.wrap(WRAPPED, CUSTOM_TO_STRING));
    }

    @Override
    protected ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    protected Class<CustomToStringConverter> type() {
        return Cast.to(CustomToStringConverter.class);
    }
}

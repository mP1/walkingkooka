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

import static org.junit.Assert.assertEquals;

public final class BooleanStringConverterTest extends FixedTypeConverterTestCase<BooleanStringConverter, String> {

    private final static String TRUE = "true!!";
    private final static String FALSE = "false!!";

    @Test(expected = NullPointerException.class)
    public void testNullTrueStringFails() {
        BooleanStringConverter.with(null, FALSE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFalseStringFails() {
        BooleanStringConverter.with(TRUE, null);
    }

    @Test
    public void testTrue() {
        this.convertAndCheck(true, TRUE);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck(false, FALSE);
    }

    @Test
    public void testToString() {
        assertEquals("Boolean->String", this.createConverter().toString());
    }

    @Override
    protected BooleanStringConverter createConverter() {
        return BooleanStringConverter.with(TRUE, FALSE);
    }

    @Override
    protected Class<String> onlySupportedType() {
        return String.class;
    }

    @Override
    protected Class<BooleanStringConverter> type() {
        return BooleanStringConverter.class;
    }
}

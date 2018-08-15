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

public final class BooleanConverterTest extends FixedTypeConverterTestCase<BooleanConverter, String> {

    private final static Class<String> TYPE = String.class;
    private final static String TRUE = "true!!";
    private final static String FALSE = "false!!";

    @Test(expected = NullPointerException.class)
    public void testNullTypeFails() {
        BooleanConverter.with(null,TRUE, FALSE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTrueValueFails() {
        BooleanConverter.with(TYPE,null, FALSE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFalseValueFails() {
        BooleanConverter.with(TYPE, TRUE, null);
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
        assertEquals("Boolean->" + TYPE.getName(), this.createConverter().toString());
    }

    @Override
    protected BooleanConverter createConverter() {
        return BooleanConverter.with(TYPE, TRUE, FALSE);
    }

    @Override
    protected Class<String> onlySupportedType() {
        return String.class;
    }

    @Override
    protected Class<BooleanConverter> type() {
        return BooleanConverter.class;
    }
}

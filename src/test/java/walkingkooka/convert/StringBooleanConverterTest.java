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

public final class StringBooleanConverterTest extends FixedTypeConverterTestCase<StringBooleanConverter, Boolean> {

    @Test
    public void testTrue() {
        this.convertAndCheck(Boolean.TRUE.toString(), true);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck(Boolean.FALSE.toString(), false);
    }

    @Test
    public void testToString() {
        assertEquals("String->Boolean", this.createConverter().toString());
    }

    @Override
    protected StringBooleanConverter createConverter() {
        return StringBooleanConverter.INSTANCE;
    }

    @Override
    protected Class<Boolean> onlySupportedType() {
        return Boolean.class;
    }

    @Override
    protected Class<StringBooleanConverter> type() {
        return StringBooleanConverter.class;
    }
}

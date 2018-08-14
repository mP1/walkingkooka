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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ConverterCollectionTest extends ConverterTestCase<ConverterCollection> {

    @Test
    @Ignore
    public void testCheckNaming() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = NullPointerException.class)
    public void testWithZeroConvertersFails() {
        ConverterCollection.with(null);
    }

    @Test
    public void testWithOneConverter() {
        final Converter only = Converters.string();
        assertSame(only, ConverterCollection.with(Lists.of(only)));
    }

    @Test
    public void testFirst() {
        this.convertAndCheck(Boolean.TRUE.toString(), Boolean.class, Boolean.TRUE);
    }

    @Test
    public void testLast() {
        this.convertAndCheck(1.0, Long.class, 1L);
    }

    @Test
    public void testUnhandledTargetType() {
        this.convertFails("Cant convert to Void", Void.class);
    }

    @Test
    public void testToString() {
        assertEquals("String->Boolean | BigDecimal|BigInteger|Double|Long->Long", this.createConverter().toString());
    }

    @Override
    protected ConverterCollection createConverter() {
        return Cast.to(ConverterCollection.with(Lists.of(Converters.stringBoolean(), Converters.numberLong())));
    }

    @Override
    protected Class<ConverterCollection> type() {
        return ConverterCollection.class;
    }
}

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

public final class SourceTypeRoutingConverterTest extends FixedTypeConverterTestCase<SourceTypeRoutingConverter<Boolean>, Boolean>{

    @Test
    public void testStringValue() {
        this.convertAndCheck(Boolean.TRUE.toString(), Boolean.TRUE);
    }

    @Test
    public void testStringValue2() {
        this.convertAndCheck(Boolean.FALSE.toString(), Boolean.FALSE);
    }

    @Test
    public void testLongNumberTrue() {
        this.convertAndCheck(1L, Boolean.TRUE);
    }

    @Test
    public void testDoubleNumberTrue() {
        this.convertAndCheck(1.0, Boolean.TRUE);
    }

    @Test
    public void testDoubleNumberFalse() {
        this.convertAndCheck(0.0, Boolean.FALSE);
    }

    @Override
    protected SourceTypeRoutingConverter<Boolean> createConverter() {
        return Cast.to(SourceTypeRoutingConverterBuilder.create(this.onlySupportedType())
                .add(Number.class, Converters.truthyNumberBoolean())
                .add(String.class, Converters.stringBoolean())
                .build());
    }

    @Override
    protected Class<SourceTypeRoutingConverter<Boolean>> type() {
        return Cast.to(SourceTypeRoutingConverter.class);
    }

    protected Class<Boolean> onlySupportedType() {
        return Boolean.class;
    }
}

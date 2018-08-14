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
import walkingkooka.build.BuilderTestCase;

import static org.junit.Assert.assertSame;

public final class SourceTypeRoutingConverterBuilderTest extends BuilderTestCase<SourceTypeRoutingConverterBuilder<Boolean>, Converter> {

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullTypeFails() {
        SourceTypeRoutingConverterBuilder.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullTypeFails() {
        this.createBuilder().add(null, Converters.numberLong());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullConverterFails() {
        this.createBuilder().add(String.class, null);
    }

    @Test
    public void testConverterDoesntSupportTargetType() {
        this.createBuilder().add(String.class, Converters.numberDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateTypeFails() {
        this.createBuilder().add(String.class, Converters.stringBoolean())
            .add(String.class, Converters.stringBoolean());
    }

    @Test
    public void testAdd() {
        this.createBuilder().add(String.class, Converters.stringBoolean())
                .add(Number.class, Converters.truthyNumberBoolean());
    }

    @Test
    public void testBuildOne() {
        final Converter converter = Converters.numberDouble();
        assertSame(converter, this.createBuilder().add(String.class, converter).build());
    }

    @Override
    protected SourceTypeRoutingConverterBuilder<Boolean> createBuilder() {
        return SourceTypeRoutingConverterBuilder.create(Boolean.class);
    }

    @Override
    protected Class<Converter> builderProductType() {
        return Converter.class;
    }

    @Override
    protected Class type() {
        return SourceTypeRoutingConverterBuilder.class;
    }
}

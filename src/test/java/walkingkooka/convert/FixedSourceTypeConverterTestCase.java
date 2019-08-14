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

public abstract class FixedSourceTypeConverterTestCase<C extends FixedSourceTypeConverter<S>, S> extends ConverterTestCase<C>
        implements ConverterTesting2<C> {

    FixedSourceTypeConverterTestCase() {
        super();
    }

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return this.sourceType().getSimpleName() + Converter.class.getSimpleName();
    }

    abstract Class<S> sourceType();

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}

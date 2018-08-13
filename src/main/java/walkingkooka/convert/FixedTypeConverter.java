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

import walkingkooka.Cast;

/**
 * A base converter that only accepts one target.
 */
abstract class FixedTypeConverter<T> extends ConverterTemplate {

    /**
     * Package private to limit sub classing.
     */
    FixedTypeConverter(){
        super();
    }

    @Override
    public boolean canConvert(final Class<?> type) {
        return this.onlySupportedType() == type;
    }

    final <TT> TT convert0(final Object value, final Class<TT> type) {
        return this.onlySupportedType().isInstance(value) ?
               Cast.to(value) :
               Cast.to(this.convert1(value));
    }

    abstract Class<T> onlySupportedType();

    abstract T convert1(Object value);

    final T failConversion(final Object value) {
        return this.failConversion(value, this.onlySupportedType());
    }
}

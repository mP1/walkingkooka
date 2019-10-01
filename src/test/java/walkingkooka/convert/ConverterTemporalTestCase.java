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

import walkingkooka.Cast;

import java.time.temporal.Temporal;

public abstract class ConverterTemporalTestCase<C extends ConverterTemporal<S, D>, S extends Temporal, D> extends ConverterTestCase2<C> {

    ConverterTemporalTestCase() {
        super();
    }

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    final void convertAndCheck2(final Object value, final Object expected) {
        this.convertAndCheck(value, Cast.to(expected.getClass()), expected);
    }

    abstract Class<D> targetType();

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return this.targetType().getSimpleName();
    }
}

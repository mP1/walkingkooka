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

import walkingkooka.build.Builder;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Builder} that eventually creates a {@link Converter} that maps individual converters based on the type of the source value.
 */
public final class SourceTypeRoutingConverterBuilder<T> implements Builder<Converter> {

    /**
     * Creates a builder with no mappings.
     */
    static <T> SourceTypeRoutingConverterBuilder<T> create(final Class<T> targetType) {
        Objects.requireNonNull(targetType, "targetType");

        return new SourceTypeRoutingConverterBuilder(targetType);
    }

    /**
     * Private ctor use factory
     */
    private SourceTypeRoutingConverterBuilder(final Class<T> targetType) {
        super();
        this.targetType = targetType;
    }

    /**
     * Adds another source type to converter. If the type already exists an exception will be thrown.
     */
    public SourceTypeRoutingConverterBuilder<T> add(final Class<?> type, final Converter converter) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(converter, "converter");

        final Optional<SourceTypeRoutingConverterTypeAndConverter> first = this.routes.stream()
                .filter(t -> type.isAssignableFrom(t.type))
                .findFirst();
        if (first.isPresent()) {
            throw new IllegalArgumentException("Source type " + CharSequences.quote(type.getName()) + " already mapped to " + first.get().converter);
        }

        this.routes.add(new SourceTypeRoutingConverterTypeAndConverter(type, converter));
        return this;
    }

    @Override
    public Converter build() throws BuilderException {
        final List<SourceTypeRoutingConverterTypeAndConverter> routes = Lists.array();
        routes.addAll(this.routes);

        Converter converter;
        final int count = this.routes.size();
        switch(count) {
            case 0:
                throw new BuilderException("No converters added");
            case 1:
                converter = routes.iterator().next().converter;
                break;
            default:
                converter = new SourceTypeRoutingConverter(this.targetType, routes);
                break;
        }

        return converter;
    }

    private final Class<T> targetType;
    private final List<SourceTypeRoutingConverterTypeAndConverter> routes = Lists.array();

    @Override
    public String toString() {
        return SourceTypeRoutingConverter.toString0(this.targetType, this.routes);
    }
}

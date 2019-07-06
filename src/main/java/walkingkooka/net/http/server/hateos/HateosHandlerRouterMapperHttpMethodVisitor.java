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

package walkingkooka.net.http.server.hateos;

import walkingkooka.ToStringBuilder;
import walkingkooka.compare.Range;
import walkingkooka.net.http.HttpMethodVisitor;

import java.util.Optional;

/**
 * Uses a {@link walkingkooka.net.http.HttpMethod} to find the matching {@link HateosHandler}.
 */
final class HateosHandlerRouterMapperHttpMethodVisitor<I extends Comparable<I>,
        R extends HateosResource<Optional<I>>,
        S extends HateosResource<Range<I>>> extends HttpMethodVisitor {

    static <I extends Comparable<I>,
            R extends HateosResource<Optional<I>>,
            S extends HateosResource<Range<I>>>
    HateosHandlerRouterMapperHttpMethodVisitor<I, R, S> with(final HateosHandlerRouterMapper<I, R, S> mapper) {
        return new HateosHandlerRouterMapperHttpMethodVisitor<>(mapper);
    }

    HateosHandlerRouterMapperHttpMethodVisitor(final HateosHandlerRouterMapper<I, R, S> mapper) {
        super();
        this.mapper = mapper;
    }

    @Override
    protected void visitGet() {
        this.mapping = this.mapper.get;
    }

    @Override
    protected void visitPost() {
        this.mapping = this.mapper.post;
    }

    @Override
    protected void visitPut() {
        this.mapping = this.mapper.put;
    }

    @Override
    protected void visitDelete() {
        this.mapping = this.mapper.delete;
    }

    private final HateosHandlerRouterMapper<I, R, S> mapper;

    HateosHandler<I, R, S> mapping;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.mapper)
                .value(this.mapping)
                .build();
    }
}

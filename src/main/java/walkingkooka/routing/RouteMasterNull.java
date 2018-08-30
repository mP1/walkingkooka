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

package walkingkooka.routing;

import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.naming.Name;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A placeholder for a route with no conditions.
 */
final class RouteMasterNull<T> extends RouteMaster<T> {

    static <T> RouteMasterNull<T> get() {
        return Cast.to(INSTANCE);
    }

    private final static RouteMasterNull<?> INSTANCE = new RouteMasterNull();

    private RouteMasterNull() {
        super();
    }

    @Override
    RouteMaster<T> add(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        return nameToCondition.isEmpty() ?
                RouteMasterTerminal.with(target) :
                this.expand(target, nameToCondition);
    }

    @Override
    RouteMaster<T> build() {
        throw new BuilderException("Builder requires at least 1 Route");
    }

    @Override
    Optional<T> route0(final Map<Name, Object> parameters) {
        return Optional.empty();
    }

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        // nop
    }
}

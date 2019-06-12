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

package walkingkooka.collect;

import walkingkooka.collect.map.Maps;
import walkingkooka.type.Types;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A registry of known types which may be queried at a later time. Rather than perform lookups using {@link String }class names},
 * the registry holds {@link Class} inside an {@link java.util.IdentityHashMap}, which is considerably quicker due to
 * identity tests rather than {@link String#equals(Object)}.
 * TODO add register whitelist.
 */
public final class ImmutableTypeRegistry {

    /**
     * Factory that creates a new registry.
     */
    public static  ImmutableTypeRegistry with(final Class<?> base) {
        Objects.requireNonNull(base, "base");

        return new ImmutableTypeRegistry(base);
    }

    private ImmutableTypeRegistry(final Class base) {
        super();
        this.base = base;
    }

    public ImmutableTypeRegistry add(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        if(!this.base.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Type " + type.getName() + " is not a sub class of " + this.base.getName());
        }

        if(Types.isAbstract(type)) {
            throw new IllegalArgumentException("Type " + type.getName() + " is abstract");
        }

        this.types.add(type);
        return this;
    }

    /**
     * Base class that all registered and queried must implement or be a sub class.
     */
    private final Class base;

    /**
     * Returns true if the type has been registered.
     */
    public boolean contains(final Class<?> type) {
        return this.types.contains(type);
    }

    private final Set<Class<?>> types = Collections.newSetFromMap(Maps.identity());

    @Override
    public String toString() {
        return this.types.stream()
                .map(Class::getName)
                .sorted()
                .collect(Collectors.joining(", ", "[","]"));
    }
}

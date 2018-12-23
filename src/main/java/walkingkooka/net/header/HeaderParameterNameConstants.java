/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache Licenseersion 2.0 (the "License");
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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * A two phase process to register constants and then lookup them up by name. If the query is not available the factory is called.
 * It is assumed all registrations are done within a static initializer of the contained class.
 */
final class HeaderParameterNameConstants<N extends HeaderParameterName<?>> {

    /**
     * Creates a new empty constants. One or more constants need to be registered before lookups can work.
     */
    static <R extends HeaderParameterName<?>> HeaderParameterNameConstants<R> empty(final BiFunction<String, HeaderValueConverter<?>, R> factory,
                                                                                    final HeaderValueConverter<?> defaultConverter) {
        return new HeaderParameterNameConstants<>(factory, defaultConverter);
    }

    /**
     * Private ctor use factory.
     */
    private HeaderParameterNameConstants(final BiFunction<String, HeaderValueConverter<?>, N> factory,
                                         final HeaderValueConverter<?> defaultConverter) {
        super();
        this.factory = factory;
        this.defaultConverter = defaultConverter;
    }

    /**
     * Registers a new parameter name constant.
     */
    <T, NN extends N> NN register(final String name, final HeaderValueConverter<T> converter) {
        if (this.nameToConstant.containsKey(name)) {
            throw new IllegalArgumentException("Constant already registered " + CharSequences.quote(name));
        }
        final N instance = this.factory.apply(name, converter);
        this.nameToConstant.put(name, instance);
        return Cast.to(instance);
    }

    /**
     * Performs a lookup of the name which may be a constant and if that lookup fails checks the name and creates a new instance.
     */
    N lookup(final String name) {
        Objects.requireNonNull(name, "name");

        final N instance = this.nameToConstant.get(name);
        return null != instance ?
                instance :
                this.checkAndCreate(name);
    }

    /**
     * Checks the characters are all valid and then calls the factory
     */
    private N checkAndCreate(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", RFC2045);
        return this.factory.apply(name, this.defaultConverter);
    }

    private final static CharPredicate RFC2045 = CharPredicates.rfc2045Token();

    /**
     * Basically calls the ctor for the parameter name. Should not perform any character checking of the provided name.
     */
    private final BiFunction<String, HeaderValueConverter<?>, N> factory;

    /**
     * A map that provides lookups ignoring case sensitivity of previously registered constants.
     */
    private final Map<String, N> nameToConstant = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * The default converter for non constant parameter names.
     */
    private final HeaderValueConverter<?> defaultConverter;

    @Override
    public final String toString() {
        return this.nameToConstant.toString();
    }
}

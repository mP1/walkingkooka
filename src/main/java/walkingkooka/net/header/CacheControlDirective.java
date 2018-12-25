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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <pre>
 * Cache request directivesSection
 * Standard Cache-Control directives that can be used by the client in an HTTP request.
 *
 * Cache-Control: max-age=<seconds>
 * Cache-Control: max-stale[=<seconds>]
 * Cache-Control: min-fresh=<seconds>
 * Cache-Control: no-cache
 * Cache-Control: no-store
 * Cache-Control: no-transform
 * Cache-Control: only-if-cached
 * Cache response directivesSection
 * Standard Cache-Control directives that can be used by the server in an HTTP response.
 *
 * Cache-Control: must-revalidate
 * Cache-Control: no-cache
 * Cache-Control: no-store
 * Cache-Control: no-transform
 * Cache-Control: public
 * Cache-Control: private
 * Cache-Control: proxy-revalidate
 * Cache-Control: max-age=<seconds>
 * Cache-Control: s-maxage=<seconds>
 * </pre>
 */
public final class CacheControlDirective<T> implements HeaderValue {

    /**
     * Parses a header value into its {@link List} of {@link CacheControlDirective} equivalent.
     */
    public static List<CacheControlDirective<?>> parse(final String text) {
        return CacheControlDirectiveHeaderParser.parseCacheControlDirectiveList(text);
    }

    // constants................................................................................................

    /**
     * Registers a directive name including some meta data about its parameters.
     */
    private static <T> CacheControlDirective<T> register(final CacheControlDirectiveName<T> name) {
        final Optional<T> parameter = Optional.empty();
        name.checkValue(parameter);

        final CacheControlDirective<T> directive = new CacheControlDirective<T>(name, parameter);
        CONSTANTS.put(name, directive);
        return directive;
    }

    /**
     * Holds all constants directives without a value.
     */
    private final static Map<CacheControlDirectiveName<?>, CacheControlDirective<?>> CONSTANTS =
            Maps.sorted();

    /**
     * max-stale
     */
    public final static CacheControlDirective<Long> MAX_STALE = register(CacheControlDirectiveName.MAX_STALE);

    /**
     * must-revalidate
     */
    public final static CacheControlDirective<Void> MUST_REVALIDATE = register(CacheControlDirectiveName.MUST_REVALIDATE);

    /**
     * no-cache
     */
    public final static CacheControlDirective<Void> NO_CACHE = register(CacheControlDirectiveName.NO_CACHE);

    /**
     * no-store
     */
    public final static CacheControlDirective<Void> NO_STORE = register(CacheControlDirectiveName.NO_STORE);

    /**
     * no-transform
     */
    public final static CacheControlDirective<Void> NO_TRANSFORM = register(CacheControlDirectiveName.NO_TRANSFORM);

    /**
     * only-if-cached
     */
    public final static CacheControlDirective<Void> ONLY_IF_CACHED = register(CacheControlDirectiveName.ONLY_IF_CACHED);

    /**
     * private
     */
    public final static CacheControlDirective<Void> PRIVATE = register(CacheControlDirectiveName.PRIVATE);

    /**
     * proxy-revalidate
     */
    public final static CacheControlDirective<Void> PROXY_REVALIDATE = register(CacheControlDirectiveName.PROXY_REVALIDATE);

    /**
     * public
     */
    public final static CacheControlDirective<Void> PUBLIC = register(CacheControlDirectiveName.PUBLIC);

    /**
     * Factory that creates a new {@link CacheControlDirective} with the provided name and parameter.
     */
    public static <T> CacheControlDirective<T> with(final CacheControlDirectiveName<T> name,
                                                    final Optional<T> parameter) {
        Objects.requireNonNull(name, "name");
        name.checkValue(parameter);

        return parameter.isPresent() ?
                new CacheControlDirective<T>(name, parameter) :
                withoutParameter(name, parameter);
    }

    private static <T> CacheControlDirective<T> withoutParameter(final CacheControlDirectiveName<T> name,
                                                                 final Optional<T> parameter) {
        final CacheControlDirective<?> constant = CONSTANTS.get(name);
        return null != constant ?
                Cast.to(constant) :
                new CacheControlDirective<T>(name, parameter);
    }

    /**
     * Private use constant or factory
     */
    CacheControlDirective(final CacheControlDirectiveName<T> name, final Optional<T> parameter) {
        super();
        this.name = name;
        this.parameter = parameter;
    }

    public CacheControlDirectiveName<T> value() {
        return this.name;
    }

    private final CacheControlDirectiveName<T> name;

    /**
     * Returns the parameter.
     */
    public final Optional<T> parameter() {
        return this.parameter;
    }

    /**
     * Would be setter that returns a {@link CacheControlDirective} with the given parameter value
     * creating a new instance if necessary.
     */
    public final CacheControlDirective<T> setParameter(final Optional<T> parameter) {
        this.name.checkValue(parameter);

        return this.parameter().equals(parameter) ?
                this :
                this.replace(parameter);
    }

    private final Optional<T> parameter;

    /**
     * Factory that creates a new {@link CacheControlDirective}
     */
    private CacheControlDirective<T> replace(final Optional<T> parameter) {
        return new CacheControlDirective<T>(this.name, parameter);
    }

    // HasHeaderScope............................................................................................

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return this.name.isRequest();
    }

    @Override
    public boolean isResponse() {
        return this.name.isResponse();
    }

    // HeaderValue....................................................................................................

    @Override
    public String toHeaderText() {
        final String name = this.name.toString();
        final Optional<T> parameter = this.parameter;
        return parameter.isPresent() ?
                name + "=" + CharSequences.quoteIfChars(parameter.get()) :
                name;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    // Object.........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.name, this.parameter);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CacheControlDirective &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CacheControlDirective<?> other) {
        return this.name.equals(other.name) &&
                this.parameter().equals(other.parameter());
    }

    @Override
    public final String toString() {
        return this.toHeaderText();
    }
}

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

package walkingkooka.net.http.server;

import walkingkooka.Cast;
import walkingkooka.build.Builder;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.CookieName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.predicate.Predicates;
import walkingkooka.routing.Routing;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link Builder} that builds a {@link Routing} that matches the given http request attributes.
 */
final public class HttpRequestAttributeRoutingBuilder<T> implements Builder<Routing<HttpRequestAttribute<?>, T>> {

    /**
     * Creates an empty builder without any predicates.
     */
    public static <T> HttpRequestAttributeRoutingBuilder<T> with(final T target) {
        Objects.requireNonNull(target, "target");

        return new HttpRequestAttributeRoutingBuilder<T>(target);
    }

    /**
     * Private ctor use factory.
     */
    private HttpRequestAttributeRoutingBuilder(final T target) {
        super();
        this.target = target;
    }

    // transport ..................................................................................

    /**
     * Adds a requirement for a particular {@link HttpTransport}.<br>
     * This method will throw an exception if called more than once.
     */
    public HttpRequestAttributeRoutingBuilder<T> transport(final HttpTransport transport) {
        failIfSecondChange(HttpRequestAttributes.TRANSPORT,
                transport,
                this.transport);
        this.transport = transport;

        return this;
    }

    private HttpTransport transport;

    // protocol .......................................................................................

    /**
     * Adds a requirement for a particular {@link HttpProtocolVersion}.<br>
     * This method will throw an exception if called more than once.
     */
    public HttpRequestAttributeRoutingBuilder<T> protocolVersion(final HttpProtocolVersion protocolVersion) {
        failIfSecondChange(HttpRequestAttributes.HTTP_PROTOCOL_VERSION,
                protocolVersion,
                this.protocolVersion);
        this.protocolVersion = protocolVersion;

        return this;
    }

    private HttpProtocolVersion protocolVersion;

    private <TT> void failIfSecondChange(final HttpRequestAttribute<?> attribute, final TT neww, final TT previous) {
        Objects.requireNonNull(neww, attribute.toString());

        if (null != previous && false == neww.equals(previous)) {
            throw new IllegalArgumentException("New " + attribute + " " + neww + " is different from previous " + previous);
        }
        this.attributeToPredicate.put(attribute, Predicates.is(neww));
    }

    // methods ......................................................................................

    /**
     * Adds a requirement for a particular {@link HttpMethod}. Subsequent calls change the test to require any of the methods.
     */
    public HttpRequestAttributeRoutingBuilder<T> method(final HttpMethod method) {
        Objects.requireNonNull(method, "method");

        this.methods.add(method);
        return this;
    }

    private void addMethodPredicate(final Map<HttpRequestAttribute<?>, Predicate<Object>> attributeToPredicate) {
        final Set<Object> copy = Sets.ordered();
        copy.addAll(this.methods);
        if (!copy.isEmpty()) {
            attributeToPredicate.put(HttpRequestAttributes.METHOD, Predicates.setContains(copy));
        }
    }

    private Set<HttpMethod> methods = Sets.ordered();

    // path ......................................................................................

    /**
     * Adds a requirement for a particular path component by name.
     */
    public HttpRequestAttributeRoutingBuilder<T> path(final int pathComponent, UrlPathName pathName) {
        return this.path(pathComponent, Predicates.is(pathName));
    }

    /**
     * Adds a predicate for a path component.
     */
    public HttpRequestAttributeRoutingBuilder<T> path(final int pathComponent,
                                                      final Predicate<UrlPathName> predicate) {
        if (pathComponent < 0) {
            throw new IllegalArgumentException("Invalid path component " + pathComponent + " < 0");
        }
        failIfSecondChange("path",
                HttpRequestAttributes.pathComponent(pathComponent),
                predicate);
        return this;
    }

    // header ......................................................................................

    /**
     * Adds a requirement that a {@link HttpHeaderName} has the provided value.
     * An exception will be thrown if multiple different predicates for the same header are set.
     */
    public <H> HttpRequestAttributeRoutingBuilder<T> headerAndValue(final HttpHeaderName<H> header,
                                                                    final H headerValue) {
        return this.header(header, Predicates.is(headerValue));
    }

    /**
     * Adds a requirement for a particular {@link HttpHeaderName}.<br>
     * An exception will be thrown if multiple different predicates for the same header are set.
     */
    public <H> HttpRequestAttributeRoutingBuilder<T> header(final HttpHeaderName<H> header,
                                                            final Predicate<H> headerValue) {
        Objects.requireNonNull(header, "header");
        Objects.requireNonNull(headerValue, "headerValue");

        failIfSecondChange("header",
                header,
                headerValue);
        return this;
    }

    // cookies ......................................................................................

    /**
     * Adds a requirement for a particular {@link CookieName}.<br>
     * This method will throw an exception if called more than once for a particular cookie name.
     */
    public HttpRequestAttributeRoutingBuilder<T> cookie(final CookieName cookieName,
                                                        final Predicate<ClientCookie> cookie) {
        Objects.requireNonNull(cookieName, "cookieName");
        Objects.requireNonNull(cookie, "cookie");

        failIfSecondChange("cookie", cookieName, cookie);
        return this;
    }

    // parameter ......................................................................................

    /**
     * Adds a requirement that a {@link HttpRequestParameterName} has the provided value.
     * An exception will be thrown if multiple different predicates for the same parameter are set.
     */
    public HttpRequestAttributeRoutingBuilder<T> parameterAndValue(final HttpRequestParameterName parameter,
                                                                   final String parameterValue) {
        return this.parameter(parameter, Predicates.is(parameterValue));
    }


    /**
     * Adds a requirement for a particular {@link HttpRequestParameterName}.<br>
     * An exception will be thrown if multiple different predicates for the same parameter are set.
     */
    public HttpRequestAttributeRoutingBuilder<T> parameter(final HttpRequestParameterName parameter,
                                                           final Predicate<String> parameterValue) {
        Objects.requireNonNull(parameter, "parameter");
        Objects.requireNonNull(parameterValue, "parameterValue");

        failIfSecondChange("parameter",
                parameter,
                HttpRequestAttributeRoutingBuilderParameterValuePredicate.with(parameterValue));
        return this;
    }

    // helpers ......................................................................................

    /**
     * Each request attribute can only have one predicate.
     */
    private void failIfSecondChange(final String label,
                                    final HttpRequestAttribute<?> key,
                                    final Predicate<?> predicate) {
        Objects.requireNonNull(key, label);
        Objects.requireNonNull(predicate, "predicate");

        final Predicate<Object> previous = this.attributeToPredicate.get(key);
        if (null != previous && false == predicate.equals(previous)) {
            throw new IllegalArgumentException("New " + label + " " + predicate + " is different from previous " + previous);
        }
        this.attributeToPredicate.put(key, Cast.to(predicate));
    }

    // build ......................................................................................

    @Override
    public Routing<HttpRequestAttribute<?>, T> build() throws BuilderException {
        final Map<HttpRequestAttribute<?>, Predicate<Object>> attributeToPredicate = Maps.ordered();
        attributeToPredicate.putAll(this.attributeToPredicate);
        this.addMethodPredicate(attributeToPredicate);

        if (attributeToPredicate.isEmpty()) {
            throw new BuilderException("Builder empty!");
        }

        Routing<HttpRequestAttribute<?>, T> routing = Cast.to(Routing.with(HttpRequestAttribute.class, this.target));
        for (Entry<HttpRequestAttribute<?>, Predicate<Object>> attributeAndPredicate : attributeToPredicate.entrySet()) {
            routing = routing.andPredicateTrue(attributeAndPredicate.getKey(), attributeAndPredicate.getValue());
        }
        return routing;
    }

    private final T target;

    /**
     * Each added predicate will set this to false.
     */
    private final Map<HttpRequestAttribute<?>, Predicate<Object>> attributeToPredicate = Maps.ordered();
}

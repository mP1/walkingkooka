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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.Cookie;
import walkingkooka.net.header.CookieName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.predicate.Predicates;
import walkingkooka.routing.Router;
import walkingkooka.routing.RouterBuilder;
import walkingkooka.routing.Routing;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpRequestAttributeRoutingBuilderTest extends ClassTestCase<HttpRequestAttributeRoutingBuilder<String>>
        implements BuilderTesting<HttpRequestAttributeRoutingBuilder<String>, Routing<HttpRequestAttribute<?>, String>> {

    private final static String TARGET = "target123";

    @Test
    public void testWithNullTargetFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpRequestAttributeRoutingBuilder.with(null);
        });
    }

    // transport ...............................................................................

    @Test
    public void testTransportNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().transport(null);
        });
    }

    @Test
    public void testTransportMultipleSame() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final HttpTransport transport = HttpTransport.UNSECURED;
        builder.transport(transport)
                .transport(transport);
    }

    @Test
    public void testTransportMultipleDifferentFails() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        builder.transport(HttpTransport.UNSECURED);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.transport(HttpTransport.SECURED);
        });
    }

    // method ...............................................................................

    @Test
    public void testMethodNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().method(null);
        });
    }

    @Test
    public void testMethodMultiple() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        builder.method(HttpMethod.GET)
                .method(HttpMethod.POST);
    }

    // path ...............................................................................

    @Test
    public void testPathNegativePathComponentIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createBuilder().path(-1, Predicates.fake());
        });
    }

    @Test
    public void testPathNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().path(0, (Predicate<UrlPathName>) null);
        });
    }

    @Test
    public void testPathRepeatedPathComponentDifferentNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createBuilder()
                    .path(0, Predicates.fake())
                    .path(0, Predicates.fake());

        });
    }

    @Test
    public void testPathRepeatedPathComponentSamePredicate() {
        final Predicate<UrlPathName> predicate = Predicates.fake();
        this.createBuilder()
                .path(0, predicate)
                .path(0, predicate);
    }

    // protocol ...............................................................................

    @Test
    public void testProtocolVersionNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().protocolVersion(null);
        });
    }

    @Test
    public void testProtocolVersionMultipleSame() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        final HttpProtocolVersion protocolVersion = HttpProtocolVersion.VERSION_1_0;
        builder.protocolVersion(protocolVersion)
                .protocolVersion(protocolVersion);
    }

    @Test
    public void testProtocolVersionMultipleDifferentFails() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        builder.protocolVersion(HttpProtocolVersion.VERSION_1_0);
        assertThrows(IllegalArgumentException.class, () -> {
            builder.protocolVersion(HttpProtocolVersion.VERSION_1_1);
        });
    }

    // header .................................................................................................

    @Test
    public void testHeaderNullHeaderNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().header(null, Predicates.<Object>fake());
        });
    }

    @Test
    public void testHeaderNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().header(HttpHeaderName.CONNECTION, (Predicate<String>) null);
        });
    }

    @Test
    public void testHeaderSamePredicate() {
        final HttpHeaderName<String> headerName = HttpHeaderName.CONNECTION;
        final Predicate<String> predicate = Predicates.is("value");
        this.createBuilder()
                .header(headerName, predicate)
                .header(headerName, predicate);
    }

    // cookie ...............................................................................

    @Test
    public void testCookieNullCookieNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().cookie(null, Predicates.fake());
        });
    }

    @Test
    public void testCookieNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().cookie(cookieName(), null);
        });
    }

    @Test
    public void testCookieSamePredicate() {
        final CookieName cookieName = this.cookieName();
        final Predicate<ClientCookie> predicate = Predicates.is(Cookie.client(cookieName, "value456"));
        this.createBuilder()
                .cookie(cookieName, predicate)
                .cookie(cookieName, predicate);
    }

    // parameter .................................................................................................

    @Test
    public void testParameterNullParameterNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().parameter(null, Predicates.fake());
        });
    }

    @Test
    public void testParameterNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().parameter(parameterName(), null);
        });
    }

    @Test
    public void testParameterSamePredicate() {
        final HttpRequestParameterName parameterName = this.parameterName();
        final Predicate<String> predicate = Predicates.is("value");
        this.createBuilder()
                .parameter(parameterName, predicate)
                .parameter(parameterName, predicate);
    }

    // build ...................................................................................................

    @Test
    public void testBuildEmptyFails() {
        assertThrows(BuilderException.class, () -> {
            this.createBuilder().build();
        });
    }

    @Test
    public void testBuild() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        assertSame(builder, builder.transport(HttpTransport.SECURED));
        assertSame(builder, builder.method(HttpMethod.GET));
        assertSame(builder, builder.protocolVersion(HttpProtocolVersion.VERSION_1_0));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(HttpRequestAttributes.TRANSPORT, HttpTransport.SECURED);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.METHOD, HttpMethod.GET);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.HTTP_PROTOCOL_VERSION, HttpProtocolVersion.VERSION_1_0);
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testMethods() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();
        assertSame(builder, builder.transport(HttpTransport.SECURED));
        assertSame(builder, builder.method(HttpMethod.GET).method(HttpMethod.POST));
        assertSame(builder, builder.protocolVersion(HttpProtocolVersion.VERSION_1_0));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(HttpRequestAttributes.TRANSPORT, HttpTransport.SECURED);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.METHOD, HttpMethod.GET);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.HTTP_PROTOCOL_VERSION, HttpProtocolVersion.VERSION_1_0);
        this.routeAndCheck(router, parameters);

        parameters.put(HttpRequestAttributes.METHOD, HttpMethod.POST);
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testPath() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final UrlPathName path1 = UrlPathName.with("path1");
        assertSame(builder, builder.path(1, path1));

        final UrlPathName path2 = UrlPathName.with("path2");
        assertSame(builder, builder.path(2, path2));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(HttpRequestAttributes.pathComponent(1), path1);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.pathComponent(2), path1);
        this.routeFails(router, parameters);

        parameters.put(HttpRequestAttributes.pathComponent(2), path2);
        this.routeAndCheck(router, parameters);

        parameters.put(HttpRequestAttributes.pathComponent(1), path2);
        parameters.put(HttpRequestAttributes.pathComponent(2), path1);
        this.routeFails(router, parameters);
    }

    @Test
    public void testHeader() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final HttpHeaderName<String> headerName1 = HttpHeaderName.with("header111").stringValues();
        final HttpHeaderName<String> headerName2 = HttpHeaderName.with("header222").stringValues();

        assertSame(builder, builder.header(headerName1, (c) -> null != c && c.contains("1")));
        assertSame(builder, builder.header(headerName2, (c) -> null != c && c.contains("2")));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(headerName1, "value1");
        this.routeFails(router, parameters);

        parameters.put(headerName2, "999");
        this.routeFails(router, parameters);

        parameters.put(headerName2, "value2");
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testHeaderAndValue() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final HttpHeaderName<String> headerName1 = HttpHeaderName.with("header111").stringValues();
        final HttpHeaderName<String> headerName2 = HttpHeaderName.with("header222").stringValues();

        final String headerValue1 = "value1";
        final String headerValue2 = "value2";

        assertSame(builder, builder.headerAndValue(headerName1, headerValue1));
        assertSame(builder, builder.headerAndValue(headerName2, headerValue2));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(headerName1, headerValue1);
        this.routeFails(router, parameters);

        parameters.put(headerName2, "999");
        this.routeFails(router, parameters);

        parameters.put(headerName2, headerValue2);
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testCookies() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final CookieName cookieName1 = CookieName.with("cookie111");
        final CookieName cookieName2 = CookieName.with("cookie222");

        assertSame(builder, builder.cookie(cookieName1, (c) -> null != c && c.value().contains("1")));
        assertSame(builder, builder.cookie(cookieName2, (c) -> null != c && c.value().contains("2")));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(cookieName1, ClientCookie.client(cookieName1, "value1"));
        this.routeFails(router, parameters);

        parameters.put(cookieName2, ClientCookie.client(cookieName2, "999"));
        this.routeFails(router, parameters);

        parameters.put(cookieName2, ClientCookie.client(cookieName2, "value2"));
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testParameterAndValue() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final HttpRequestParameterName parameter1 = HttpRequestParameterName.with("parameter1");
        final HttpRequestParameterName parameter2 = HttpRequestParameterName.with("parameter2");

        assertSame(builder, builder.parameterAndValue(parameter1, "value1"));
        assertSame(builder, builder.parameterAndValue(parameter2, "value2"));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(parameter1, Lists.of("value1"));
        this.routeFails(router, parameters);

        parameters.put(parameter2, Lists.of("999"));
        this.routeFails(router, parameters);

        parameters.put(parameter2, Lists.of("999", "value2"));
        this.routeAndCheck(router, parameters);
    }

    @Test
    public void testParameterPredicate() {
        final HttpRequestAttributeRoutingBuilder<String> builder = this.createBuilder();

        final HttpRequestParameterName parameter1 = HttpRequestParameterName.with("parameter1");
        final HttpRequestParameterName parameter2 = HttpRequestParameterName.with("parameter2");

        assertSame(builder, builder.parameter(parameter1, (v) -> null != v && v.contains("1")));
        assertSame(builder, builder.parameter(parameter2, (v) -> null != v && v.contains("2")));

        final Router<HttpRequestAttribute<?>, String> router = this.build(builder);

        final Map<HttpRequestAttribute<?>, Object> parameters = Maps.ordered();

        parameters.put(parameter1, Lists.of("value1"));
        this.routeFails(router, parameters);

        parameters.put(parameter2, Lists.of("999"));
        this.routeFails(router, parameters);

        parameters.put(parameter2, Lists.of("999", "value2"));
        this.routeAndCheck(router, parameters);
    }

    private Router<HttpRequestAttribute<?>, String> build(final HttpRequestAttributeRoutingBuilder<String> builder) {
        return RouterBuilder.<HttpRequestAttribute<?>, String>empty()
                .add(builder.build())
                .build();
    }

    private void routeAndCheck(final Router<HttpRequestAttribute<?>, String> routers,
                               final Map<HttpRequestAttribute<?>, Object> parameters) {
        assertEquals(Optional.of(TARGET),
                routers.route(parameters),
                "Routing of parameters=" + parameters + " failed");
    }

    private void routeFails(final Router<HttpRequestAttribute<?>, String> routers,
                            final Map<HttpRequestAttribute<?>, Object> parameters) {
        assertEquals(Optional.empty(),
                routers.route(parameters),
                "Routing of parameters=" + parameters + " should have failed");
    }

    // helpers.................................................................................................

    @Override
    public HttpRequestAttributeRoutingBuilder<String> createBuilder() {
        return HttpRequestAttributeRoutingBuilder.with(TARGET);
    }

    private CookieName cookieName() {
        return CookieName.with("cookie123");
    }

    private HttpRequestParameterName parameterName() {
        return HttpRequestParameterName.with("parameter");
    }

    @Override
    public Class<HttpRequestAttributeRoutingBuilder<String>> type() {
        return Cast.to(HttpRequestAttributeRoutingBuilder.class);
    }

    @Override
    public Class<Routing<HttpRequestAttribute<?>, String>> builderProductType() {
        return Cast.to(Routing.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}

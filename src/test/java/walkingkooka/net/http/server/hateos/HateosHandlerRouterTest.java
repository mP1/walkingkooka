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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.header.AcceptCharset;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpRequestParameterName;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.TestRecordingHttpResponse;
import walkingkooka.routing.RouterTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class HateosHandlerRouterTest implements ClassTesting2<HateosHandlerRouter<JsonNode>>,
        RouterTesting<HateosHandlerRouter<JsonNode>,
                HttpRequestAttribute<?>,
                BiConsumer<HttpRequest, HttpResponse>> {

    private final static String NO_JSON = null;

    final static BigInteger ID = BigInteger.valueOf(123);
    final static BigInteger NO_ID = null;
    final static Range<BigInteger> ALL = Range.all();
    final static Range<BigInteger> RANGE = Range.greaterThanEquals(BigInteger.valueOf(123)).and(Range.lessThanEquals(BigInteger.valueOf(456)));

    private final static FakeHateosHandler<BigInteger, TestHateosResource> HANDLER = new FakeHateosHandler<>();

    // BAD REQUEST................................................................................................

    @Test
    public void testMethodNotSupported() {
        this.routeAndCheck(HttpMethod.HEAD,
                "/api/resource/*/self",
                HttpStatusCode.METHOD_NOT_ALLOWED.setMessage("HEAD"));
    }

    @Test
    public void testBadRequestMissingBase() {
        this.routeAndCheck("/missing/",
                null);
    }

    @Test
    public void testBadRequestMissingResourceName() {
        this.routeAndCheck("/api/",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestMissingResourceName2() {
        this.routeAndCheck("/api//",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestInvalidResourceName() {
        this.routeAndCheck("/api/999-invalid",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid resource name \"999-invalid\""));
    }

    @Test
    public void testNotFoundHandlerMissing() {
        this.routeAndCheck("/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissingWithRelation() {
        this.routeAndCheck("/api/unknown123/1/self",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissing2() {
        this.routeAndCheck(this.builder().add(this.resourceName2(), this.relation1(), this.mapper()),
                "/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testBadRequestInvalidRelation() {
        assertThrows(IllegalArgumentException.class, () -> {
            LinkRelation.with("!!");
        });
        this.routeAndCheckWithGetHandler("/api/resource1/1/!!",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid link relation \"!!\""));
    }

    @Test
    public void testBadRequestInvalidResourceId() {
        this.routeAndCheckWithGetHandler("/api/resource1/@/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid id \"@\""));
    }

    @Test
    public void testBadRequestMissingRangeBeginResourceId() {
        this.routeAndCheckWithGetHandler("/api/resource1/-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"-999\""));
    }

    @Test
    public void testBadRequestInvalidRangeBeginResourceId() {
        this.routeAndCheckWithGetHandler("/api/resource1/!-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"!-999\""));
    }

    @Test
    public void testBadRequestMissingRangeEndResourceId() {
        this.routeAndCheckWithGetHandler("/api/resource1/999-/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-\""));
    }

    @Test
    public void testBadRequestInvalidRangeEndResourceId() {
        this.routeAndCheckWithGetHandler("/api/resource1/999-!/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-!\""));
    }

    @Test
    public void testBadRequestInvalidRange() {
        this.routeAndCheckWithGetHandler("/api/resource1/1-2-3/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1-2-3\""));
    }

    @Test
    public void testBadRequestInvalidRange2() {
        this.routeAndCheckWithGetHandler("/api/resource1/1\\-2-3-4/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1\\\\-2-3-4\""));
    }

    private void routeAndCheckWithGetHandler(final String url,
                                             final HttpStatus status,
                                             final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(HANDLER)),
                url,
                status,
                entities);
    }

    @Test
    public void testBadRequestIdAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/123/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testBadRequestMissingIdAndInvalidJson() {
        this.routePostAndCheck("/api/resource1//self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testBadRequestWildcardAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/*/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testBadRequestRangeAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/1-2/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    private void routePostAndCheck(final String url,
                                   final String requestBody,
                                   final HttpStatus status,
                                   final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().post(HANDLER)),
                HttpMethod.POST,
                url,
                this.contentType(),
                requestBody,
                status,
                entities);
    }

    private void routeAndCheck(final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(HttpMethod.GET,
                url,
                status,
                entities);
    }

    private void routeAndCheck(final HttpMethod method,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(this.builder(),
                method,
                url,
                status,
                entities);
    }

    @Test
    public void testNotImplementedResource() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public Optional<TestHateosResource> handle(final BigInteger id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException();
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testNotImplementedResource2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public Optional<TestHateosResource> handle(final BigInteger id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException(message);
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testInternalServerErrorResource() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public Optional<TestHateosResource> handle(final BigInteger id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException();
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testInternalServerErrorResource2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public Optional<TestHateosResource> handle(final BigInteger id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException(message);
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }


    @Test
    public void testNotImplementedResourceCollection() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public List<TestHateosResource> handleCollection(final Range<BigInteger> ids,
                                                                                final List<TestHateosResource> resources,
                                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException();
                               }
                           },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testNotImplementedResourceCollection2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public List<TestHateosResource> handleCollection(final Range<BigInteger> ids,
                                                                                final List<TestHateosResource> resources,
                                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException(message);
                               }
                           },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testInternalServerErrorResourceCollection() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public List<TestHateosResource> handleCollection(final Range<BigInteger> ids,
                                                                                final List<TestHateosResource> resources,
                                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException();
                               }
                           },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testInternalServerErrorResourceCollection2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public List<TestHateosResource> handleCollection(final Range<BigInteger> ids,
                                                                                final List<TestHateosResource> resources,
                                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException(message);
                               }
                           },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }

    private void routeAndCheck(final HateosHandler<BigInteger, TestHateosResource> handler,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(handler)),
                HttpMethod.GET,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(builder,
                HttpMethod.GET,
                url,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final HttpMethod method,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(builder,
                method,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    // GET RESOURCE ................................................................................................

    @Test
    public void testIdWithoutRequestResource() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource responseResource = this.resource(234);

        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final BigInteger i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(id, i);
                                      assertEquals(Optional.empty(), r);
                                      return Optional.of(responseResource);
                                  }
                              },
                "/api/resource1/123/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndResponseResource() {
        this.idWithRequestResourceAndResponseResourceDifferentCharset(this.contentType());
    }

    @Test
    public void testIdWithRequestResourceAndResponseResourceDifferentCharset() {
        this.idWithRequestResourceAndResponseResourceDifferentCharset(this.contentTypeUtf16());
    }

    private void idWithRequestResourceAndResponseResourceDifferentCharset(final MediaType contentType) {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final TestHateosResource responseResource = TestHateosResource.with(BigInteger.valueOf(234));

        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final BigInteger i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(id, i);
                                      assertEquals(Optional.of(requestResource), r);
                                      return Optional.of(responseResource);
                                  }
                              },
                "/api/resource1/123/self",
                contentType,
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", contentType));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourceGet() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                               @Override
                               public Optional<TestHateosResource> handle(final BigInteger i,
                                                                          final Optional<TestHateosResource> r,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   assertEquals(id, i);
                                   assertEquals(Optional.of(requestResource), r);
                                   return Optional.empty();
                               }
                           },
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourcePost() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().post(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                    @Override
                    public Optional<TestHateosResource> handle(final BigInteger i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.POST,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("POST resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourcePut() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().put(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                    @Override
                    public Optional<TestHateosResource> handle(final BigInteger i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.PUT,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("PUT resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourceDelete() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().delete(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                    @Override
                    public Optional<TestHateosResource> handle(final BigInteger i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("DELETE resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdEscaped() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final BigInteger i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(id, i);
                                      assertEquals(Optional.of(requestResource), r);
                                      return Optional.empty();
                                  }
                              },
                "/api/resource1/\\1\\2\\3/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdDifferentTypeFromHateosResourceIdType() {
        final Long id = 123L;
        final TestHateosResource requestResource = TestHateosResource.with(BigInteger.valueOf(99));
        final TestHateosResource responseResource = TestHateosResource.with(BigInteger.valueOf(id));

        final HateosHandlerBuilder<JsonNode> builder = this.builder()
                .add(this.resourceName1(), this.relation1(), HateosHandlerMapper.with(Long::parseLong, TestHateosResource.class)
                        .get(new FakeHateosHandler<Long, TestHateosResource>() {
                            @Override
                            public Optional<TestHateosResource> handle(final Long i,
                                                                       final Optional<TestHateosResource> resource,
                                                                       final Map<HttpRequestAttribute<?>, Object> parameters) {
                                assertEquals(id, i, "id");
                                return Optional.of(responseResource);
                            }
                        }));
        this.routeAndCheck(builder,
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                                "  \"id\": \"123\",\n" +
                                "  \"_links\": [{\n" +
                                "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                                "    \"method\": \"GET\",\n" +
                                "    \"rel\": \"self\",\n" +
                                "    \"type\": \"application/hal+json\"\n" +
                                "  }]\n" +
                                "}",
                        this.contentType()));
    }

    // GET COLLECTION  ................................................................................................

    @Test
    public void testAllWithoutRequestResourcesWithoutResponseResources() {
        this.routeGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testAllWithoutRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource.with(BigInteger.valueOf(123))),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testAllWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource.with(BigInteger.valueOf(999))),
                "/api/resource1//self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testWildcardWithoutRequestResourcesWithoutResponseResources() {
        this.routeGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testWildcardWithoutRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource.with(BigInteger.valueOf(123))),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testWildcardWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource.with(BigInteger.valueOf(999))),
                "/api/resource1/*/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }


    @Test
    public void testRangeWithoutRequestResourcesWithoutResponseResources() {
        this.routeGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testRangeIdEscaped() {
        this.routeGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/\\1-\\2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testRangeWithoutRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.of(TestHateosResource.with(BigInteger.valueOf(123))),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testRangeWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testRangeWithRequestResourcesWithResponseResourcesDifferentContentType() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)), TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentTypeUtf16(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    private Range<BigInteger> rangeBetween(final long begin, final long end) {
        return Range.greaterThanEquals(BigInteger.valueOf(begin))
                .and(Range.lessThanEquals(BigInteger.valueOf(end)));
    }

    private void routeGetCollectionAndCheck(final Range<BigInteger> range,
                                            final List<TestHateosResource> requestResources,
                                            final List<TestHateosResource> responseResources,
                                            final String url,
                                            final MediaType contentType,
                                            final HttpStatus status,
                                            final String responseBody) {
        this.routeGetCollectionAndCheck(range,
                requestResources,
                responseResources,
                url,
                contentType,
                HasJsonNode.toJsonNodeList(requestResources).toString(),
                status,
                responseBody);
    }

    private void routeGetCollectionAndCheck(final Range<BigInteger> range,
                                            final List<TestHateosResource> requestResources,
                                            final List<TestHateosResource> responseResources,
                                            final String url,
                                            final MediaType contentType,
                                            final String requestBody,
                                            final HttpStatus status,
                                            final String responseBody) {
        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource>() {
                                  @Override
                                  public List<TestHateosResource> handleCollection(final Range<BigInteger> i,
                                                                                   final List<TestHateosResource> r,
                                                                                   final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(range, i);
                                      assertEquals(requestResources, r);
                                      return responseResources;
                                  }
                              },
                url,
                contentType,
                requestBody,
                status,
                this.httpEntity(responseBody, contentType));
    }

    // HELPERS ............................................................................................

    @Override
    public HateosHandlerRouter<JsonNode> createRouter() {
        final HateosHandlerBuilder<JsonNode> builder = this.builder();
        //builder.add(this.resourceName1(), this.relation1(), new FakeHateosGetHandler<BigInteger, JsonNode>());
        return Cast.to(builder.build()); // builder returns interface which is HHBR class.
    }

    private HateosHandlerBuilder<JsonNode> builder() {
        return HateosHandlerBuilder.with(
                this.base(),
                HateosContentType.json());
    }

    private AbsoluteUrl base() {
        return Url.parseAbsolute("http://www.example.com/api");
    }

    private HateosResourceName resourceName1() {
        return HateosResourceName.with("resource1");
    }

    private HateosResourceName resourceName2() {
        return HateosResourceName.with("resource2");
    }

    private LinkRelation<?> relation1() {
        return LinkRelation.SELF;
    }

    private LinkRelation<?> relation2() {
        return LinkRelation.NEXT;
    }

    private MediaType contentType() {
        return HateosContentType.json().contentType();
    }

    private MediaType contentTypeUtf16() {
        return this.contentType().setCharset(CharsetName.UTF_16);
    }

    private TestHateosResource resource(final long id) {
        return TestHateosResource.with(BigInteger.valueOf(id));
    }

    private HttpEntity[] httpEntity(final String body,
                                    final MediaType contentType) {
        HttpEntity[] entities = new HttpEntity[0];

        if (!CharSequences.isNullOrEmpty(body)) {
            final CharsetName charsetName = MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8);
            final byte[] bytes = this.bytes(body, contentType);

            final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();
            headers.put(HttpHeaderName.CONTENT_TYPE, HateosContentType.json().contentType().setCharset(charsetName));
            headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(bytes(body, contentType).length));

            entities = new HttpEntity[]{HttpEntity.with(headers, bytes)};
        }
        return entities;
    }

    private HateosHandlerMapper<BigInteger, TestHateosResource> mapper() {
        return HateosHandlerMapper.with(BigInteger::new, TestHateosResource.class);
    }

    private void routeGetAndCheck(final HateosHandler<BigInteger, TestHateosResource> handler,
                                  final String url,
                                  final MediaType contentType,
                                  final String body,
                                  final HttpStatus status,
                                  final HttpEntity... entities) {
        this.routeAndCheck(handler,
                HttpMethod.GET,
                url,
                contentType,
                body,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandler<BigInteger, TestHateosResource> handler,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final HateosHandlerMapper<BigInteger, TestHateosResource> mapper = this.mapper();
        do {
            if (method.equals(HttpMethod.GET)) {
                mapper.get(handler);
                break;
            }
            if (method.equals(HttpMethod.POST)) {
                mapper.post(handler);
                break;
            }
            if (method.equals(HttpMethod.PUT)) {
                mapper.put(handler);
                break;
            }
            if (method.equals(HttpMethod.DELETE)) {
                mapper.delete(handler);
                break;
            }
            fail("Unknown method=" + method);
        } while (false);

        this.routeAndCheck(mapper,
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerMapper<BigInteger, TestHateosResource> mapper,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), mapper),
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final TestRecordingHttpResponse response = HttpResponses.testRecording();

        final HttpRequest request = this.request(method,
                url,
                contentType,
                bytes(body, contentType));
        final Optional<BiConsumer<HttpRequest, HttpResponse>> handle = builder.build()
                .route(request.routingParameters());
        if (handle.isPresent()) {
            handle.get()
                    .accept(request, response);
        }

        response.check(request, status, entities);
    }

    private byte[] bytes(final String body, final MediaType contentType) {
        return null != body ?
                body.getBytes(MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8).charset().get()) :
                null;
    }

    private HttpRequest request(final HttpMethod method,
                                final String url,
                                final MediaType contentType,
                                final byte[] body) {
        return new HttpRequest() {

            @Override
            public HttpTransport transport() {
                return HttpTransport.UNSECURED;
            }

            @Override
            public HttpProtocolVersion protocolVersion() {
                return HttpProtocolVersion.VERSION_1_0;
            }

            @Override
            public HttpMethod method() {
                return method;
            }

            @Override
            public RelativeUrl url() {
                return RelativeUrl.parse(url);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();
                headers.put(HttpHeaderName.ACCEPT_CHARSET, AcceptCharset.parse(MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8).toHeaderText()));
                headers.put(HttpHeaderName.CONTENT_TYPE, contentType);
                return headers;
            }

            @Override
            public byte[] body() {
                return null != body ?
                        body.clone() :
                        body;
            }

            @Override
            public Map<HttpRequestParameterName, List<String>> parameters() {
                final Map<HttpRequestParameterName, List<String>> parameters = Maps.sorted();

                this.url()
                        .query()
                        .parameters()
                        .entrySet()
                        .forEach(e -> parameters.put(HttpRequestParameterName.with(e.getKey().value()), e.getValue()));

                return parameters;
            }

            @Override
            public List<String> parameterValues(final HttpRequestParameterName parameterName) {
                final List<String> values = this.parameters().get(parameterName);
                return null == values ?
                        Lists.empty() :
                        values;
            }

            @Override
            public String toString() {
                return this.method() + " " + this.url() + " " + parameters();
            }
        };
    }

    private void checkParameters(Map<HttpRequestAttribute<?>, Object> parameters) {
        assertEquals(Lists.of("value1"),
                parameters.get(UrlParameterName.with("param1")),
                "parameters");
    }

    @Override
    public Class<HateosHandlerRouter<JsonNode>> type() {
        return Cast.to(HateosHandlerRouter.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}

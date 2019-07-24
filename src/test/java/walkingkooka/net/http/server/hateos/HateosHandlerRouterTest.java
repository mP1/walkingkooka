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

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
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
import walkingkooka.net.http.server.RecordingHttpResponse;
import walkingkooka.routing.RouterTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class HateosHandlerRouterTest extends HateosHandlerRouterTestCase<HateosHandlerRouter<JsonNode>>
        implements RouterTesting<HateosHandlerRouter<JsonNode>,
        HttpRequestAttribute<?>,
        BiConsumer<HttpRequest, HttpResponse>> {

    private final static String NO_JSON = null;

    private final static BigInteger ID = BigInteger.valueOf(31);
    private final static BigInteger ID2 = BigInteger.valueOf(127);
    private final static Range<BigInteger> ALL = Range.all();
    private final static Range<BigInteger> RANGE1_2 = Range.greaterThanEquals(ID).and(Range.lessThanEquals(ID2));

    private final static HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> HATEOS_RESOURCE_HANDLER = new FakeHateosHandler<>();

    private final static TestHateosResource RESOURCE_IN = TestHateosResource.with(ID);
    private final static TestHateosResource RESOURCE_OUT = TestHateosResource.with(ID2);
    private final static TestHateosResource2 COLLECTION_RESOURCE_IN = TestHateosResource2.with(Range.singleton(ID));
    private final static TestHateosResource2 COLLECTION_RESOURCE_OUT = TestHateosResource2.with(RANGE1_2);

    // BAD REQUEST................................................................................................

    @Test
    public void testMethodNotSupported() {
        this.routeAndCheck(HttpMethod.HEAD,
                "/api/resource/*/self",
                HttpStatusCode.METHOD_NOT_ALLOWED.setMessage("HEAD"));
    }

    @Test
    public void testBadRequestMissingBase() {
        this.routeGetAndCheck("/missing/",
                null);
    }

    @Test
    public void testBadRequestMissingResourceName() {
        this.routeGetAndCheck("/api/",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestMissingResourceName2() {
        this.routeGetAndCheck("/api//",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestInvalidResourceName() {
        this.routeGetAndCheck("/api/999-invalid",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid resource name \"999-invalid\""));
    }

    @Test
    public void testNotFoundHandlerMissing() {
        this.routeGetAndCheck("/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissingWithRelation() {
        this.routeGetAndCheck("/api/unknown123/1/self",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissing2() {
        this.routeAndCheck(this.builder().add(this.resourceName2(), this.relation1(), this.mapper()),
                "/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testGetBadRequestInvalidRelation() {
        assertThrows(InvalidCharacterException.class, () -> {
            LinkRelation.with("!!");
        });
        this.routeGetAndCheck("/api/resource1/1/!!",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid link relation \"!!\""));
    }

    @Test
    public void testGetBadRequestInvalidResourceId() {
        this.routeGetAndCheck("/api/resource1/@/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid id \"@\""));
    }

    @Test
    public void testGetBadRequestMissingRangeBeginResourceId() {
        this.routeGetAndCheck("/api/resource1/-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"-999\""));
    }

    @Test
    public void testGetBadRequestInvalidRangeBeginResourceId() {
        this.routeGetAndCheck("/api/resource1/!-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"!-999\""));
    }

    @Test
    public void testGetBadRequestMissingRangeEndResourceId() {
        this.routeGetAndCheck("/api/resource1/999-/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-\""));
    }

    @Test
    public void testGetBadRequestInvalidRangeEndResourceId() {
        this.routeGetAndCheck("/api/resource1/999-!/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-!\""));
    }

    @Test
    public void testGetBadRequestInvalidRange() {
        this.routeGetAndCheck("/api/resource1/1-2-3/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1-2-3\""));
    }

    @Test
    public void testGetBadRequestInvalidRange2() {
        this.routeGetAndCheck("/api/resource1/1\\-2-3-4/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1\\\\-2-3-4\""));
    }

    private void routeGetAndCheck(final String url,
                                  final HttpStatus status,
                                  final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(HATEOS_RESOURCE_HANDLER)),
                url,
                status,
                entities);
    }

    // POST..................................................................................................

    @Test
    public void testPostBadRequestIdAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/1f/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testPostBadRequestMissingIdAndInvalidJson() {
        this.routePostAndCheck("/api/resource1//self");
    }

    @Test
    public void testPostBadRequestWildcardAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/*/self");
    }

    @Test
    public void testPostBadRequestRangeAndInvalidJson() {
        this.routePostAndCheck("/api/resource1/1-2/self");
    }

    private void routePostAndCheck(final String url) {
        this.routePostAndCheck(url,
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    private void routePostAndCheck(final String url,
                                   final String requestBody,
                                   final HttpStatus status,
                                   final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().post(HATEOS_RESOURCE_HANDLER)),
                HttpMethod.POST,
                url,
                this.contentType(),
                requestBody,
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

    // HateosHandler.handle...................................................................................

    @Test
    public void testHateosHandlerNotImplementedResource() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource> handle(final Optional<BigInteger> id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException();
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosHandlerNotImplementedResource2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource> handle(final Optional<BigInteger> id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new UnsupportedOperationException(message);
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosHandlerInternalServerErrorResource() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource> handle(final Optional<BigInteger> id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException();
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosHandlerInternalServerErrorResource2() {
        final String message = "abc123";
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource> handle(final Optional<BigInteger> id,
                                                                          final Optional<TestHateosResource> resource,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   throw new IllegalArgumentException(message);
                               }
                           },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }

    private void routeAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
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

    // helpers.........................................................

    private void routeAndCheck(final HateosHandlerRouterBuilder<JsonNode> builder,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(builder,
                HttpMethod.GET,
                url,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerRouterBuilder<JsonNode> builder,
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

    // handleId no ID.. ................................................................................................

    @Test
    public void testMissingIdWithoutRequestResource() {
        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(Optional.empty(), i);
                                      assertEquals(Optional.empty(), r);
                                      return Optional.of(RESOURCE_OUT);
                                  }
                              },
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"127\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", this.contentType()));
    }

    @Test
    public void testMissingIdWithRequestResource() {
        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(Optional.empty(), i);
                                      assertEquals(Optional.of(RESOURCE_IN), r);
                                      return Optional.of(RESOURCE_OUT);
                                  }
                              },
                "/api/resource1//self",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"127\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", this.contentType()));
    }

    // handleID WITH ID.................................................................................................

    @Test
    public void testIdWithoutRequestResource() {
        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(Optional.of(ID), i);
                                      assertEquals(Optional.empty(), r);
                                      return Optional.of(RESOURCE_OUT);
                                  }
                              },
                "/api/resource1/1f/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"127\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/7f\",\n" +
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
        this.routeGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                  @Override
                                  public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                                             final Optional<TestHateosResource> r,
                                                                             final Map<HttpRequestAttribute<?>, Object> parameters) {
                                      assertEquals(Optional.of(ID), i);
                                      assertEquals(Optional.of(RESOURCE_IN), r);
                                      return Optional.of(RESOURCE_OUT);
                                  }
                              },
                "/api/resource1/1f/self",
                contentType,
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"127\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", contentType));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourceGet() {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                                          final Optional<TestHateosResource> r,
                                                                          final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   assertEquals(Optional.of(ID), i);
                                   assertEquals(Optional.of(RESOURCE_IN), r);
                                   return Optional.empty();
                               }
                           },
                HttpMethod.GET,
                "/api/resource1/1f/self",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourcePost() {
        this.routeAndCheck(this.mapper().post(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(Optional.of(ID), i);
                        assertEquals(Optional.of(RESOURCE_IN), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.POST,
                "/api/resource1/1f/self",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("POST resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourcePut() {
        this.routeAndCheck(this.mapper().put(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(Optional.of(ID), i);
                        assertEquals(Optional.of(RESOURCE_IN), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.PUT,
                "/api/resource1/1f/self",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("PUT resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testIdWithRequestResourceAndWithoutResponseResourceDelete() {
        this.routeAndCheck(this.mapper().delete(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(Optional.of(ID), i);
                        assertEquals(Optional.of(RESOURCE_IN), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/1f/self",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("DELETE resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testExtraPathIgnored() {
        this.routeAndCheck(this.mapper().delete(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource> handle(final Optional<BigInteger> i,
                                                               final Optional<TestHateosResource> r,
                                                               final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(Optional.of(ID), i);
                        assertEquals(Optional.of(RESOURCE_IN), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/1f/self/extra-ignored-path",
                this.contentType(),
                RESOURCE_IN.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("DELETE resource successful"),
                this.httpEntity("", this.contentType()));
    }

    private void routeGetAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
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

    // handleCollection WILDCARD........................................................................................

    @Test
    public void testWildcardWithoutRequestResourcesWithoutResponseResources() {
        this.routeGetCollectionAndCheck(ALL,
                Optional.empty(),
                Optional.empty(),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                "");
    }

    @Test
    public void testWildcardWithoutRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(ALL,
                Optional.empty(),
                Optional.of(COLLECTION_RESOURCE_OUT),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                "{\n" +
                        "  \"id\": {\n" +
                        "    \"type\": \"range\",\n" +
                        "    \"value\": {\n" +
                        "      \"lower-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"31\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"upper-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"127\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/1f-7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testWildcardWithRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(ALL,
                Optional.of(COLLECTION_RESOURCE_IN),
                Optional.of(COLLECTION_RESOURCE_OUT),
                "/api/resource1/*/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                "{\n" +
                        "  \"id\": {\n" +
                        "    \"type\": \"range\",\n" +
                        "    \"value\": {\n" +
                        "      \"lower-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"31\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"upper-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"127\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/1f-7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testRangeWithoutRequestResourcesWithoutResponseResources() {
        this.routeGetCollectionAndCheck(RANGE1_2,
                Optional.empty(),
                Optional.empty(),
                "/api/resource1/1f-7f/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                "");
    }

    @Test
    public void testRangeWithoutRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(RANGE1_2,
                Optional.empty(),
                Optional.of(COLLECTION_RESOURCE_OUT),
                "/api/resource1/1f-7f/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                "{\n" +
                        "  \"id\": {\n" +
                        "    \"type\": \"range\",\n" +
                        "    \"value\": {\n" +
                        "      \"lower-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"31\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"upper-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"127\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/1f-7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testRangeWithRequestResourcesWithResponseResources() {
        this.routeGetCollectionAndCheck(RANGE1_2,
                Optional.of(COLLECTION_RESOURCE_IN),
                Optional.of(COLLECTION_RESOURCE_OUT),
                "/api/resource1/1f-7f/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                "{\n" +
                        "  \"id\": {\n" +
                        "    \"type\": \"range\",\n" +
                        "    \"value\": {\n" +
                        "      \"lower-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"31\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"upper-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"127\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/1f-7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    @Test
    public void testRangeWithRequestResourcesWithResponseResourcesDifferentContentType() {
        this.routeGetCollectionAndCheck(RANGE1_2,
                Optional.of(COLLECTION_RESOURCE_IN),
                Optional.of(COLLECTION_RESOURCE_OUT),
                "/api/resource1/1f-7f/self",
                this.contentTypeUtf16(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                "{\n" +
                        "  \"id\": {\n" +
                        "    \"type\": \"range\",\n" +
                        "    \"value\": {\n" +
                        "      \"lower-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"31\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"upper-bound\": {\n" +
                        "        \"inclusive\": {\n" +
                        "          \"type\": \"big-integer\",\n" +
                        "          \"value\": \"127\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/1f-7f\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}");
    }

    private void routeGetCollectionAndCheck(final Range<BigInteger> range,
                                            final Optional<TestHateosResource2> requestResource,
                                            final Optional<TestHateosResource2> responseResource,
                                            final String url,
                                            final MediaType contentType,
                                            final HttpStatus status,
                                            final String responseBody) {
        this.routeGetCollectionAndCheck(range,
                requestResource,
                responseResource,
                url,
                contentType,
                requestResource.map(r -> r.toJsonNode().toString()).orElse(""),
                status,
                responseBody);
    }

    private void routeGetCollectionAndCheck(final Range<BigInteger> range,
                                            final Optional<TestHateosResource2> requestResource,
                                            final Optional<TestHateosResource2> responseResource,
                                            final String url,
                                            final MediaType contentType,
                                            final String requestBody,
                                            final HttpStatus status,
                                            final String responseBody) {
        this.routeAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                               @Override
                               public Optional<TestHateosResource2> handleCollection(final Range<BigInteger> i,
                                                                                     final Optional<TestHateosResource2> r,
                                                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                   assertEquals(range, i);
                                   assertEquals(requestResource, r);
                                   return responseResource;
                               }
                           },
                HttpMethod.GET,
                url,
                contentType,
                requestBody,
                status,
                this.httpEntity(responseBody, contentType));
    }

    private void routeAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper = this.mapper();
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

    // HELPERS ............................................................................................

    @Override
    public HateosHandlerRouter<JsonNode> createRouter() {
        final HateosHandlerRouterBuilder<JsonNode> builder = this.builder();
        //builder.add(this.resourceName1(), this.relation1(), new FakeHateosGetHandler<String, JsonNode>());
        return Cast.to(builder.build()); // builder returns interface which is HHBR class.
    }

    private HateosHandlerRouterBuilder<JsonNode> builder() {
        return HateosHandlerRouterBuilder.with(
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

    private HttpEntity[] httpEntity(final String body,
                                    final MediaType contentType) {
        HttpEntity[] entities = new HttpEntity[0];

        if (!CharSequences.isNullOrEmpty(body)) {
            final CharsetName charsetName = MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8);
            final byte[] bytes = this.bytes(body, contentType);

            final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();
            headers.put(HttpHeaderName.CONTENT_TYPE, HateosContentType.json().contentType().setCharset(charsetName));
            headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(bytes(body, contentType).length));

            entities = new HttpEntity[]{HttpEntity.with(headers, Binary.with(bytes))};
        }
        return entities;
    }

    private void routeAndCheck(final HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper,
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

    private void routeAndCheck(final HateosHandlerRouterBuilder<JsonNode> builder,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final RecordingHttpResponse recording = HttpResponses.recording();

        final HttpRequest request = this.request(method,
                url,
                contentType,
                bytes(body, contentType));
        final Optional<BiConsumer<HttpRequest, HttpResponse>> handle = builder.build()
                .route(request.routingParameters());
        handle.ifPresent(h -> h.accept(request, recording));

        this.checkResponse(recording, request, status, entities);
    }

    private byte[] bytes(final String body, final MediaType contentType) {
        return null != body ?
                body.getBytes(MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8).charset().get()) :
                null;
    }

    private HateosHandlerRouterMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper() {
        return HateosHandlerRouterMapper.with(HateosHandlerRouterTest::parseId,
                TestHateosResource.class,
                TestHateosResource2.class);
    }

    private static BigInteger parseId(final String id) {
        return BigInteger.valueOf(Integer.parseInt(id, 16));
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
                return Url.parseRelative(url);
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

    private void checkResponse(final RecordingHttpResponse response,
                               final HttpRequest request,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final HttpResponse expected = HttpResponses.recording();

        if (null != status) {
            expected.setStatus(status);
        }

        Arrays.stream(entities)
                .forEach(expected::addEntity);

        assertEquals(expected,
                response,
                () -> request.toString());
    }

    @Override
    public Class<HateosHandlerRouter<JsonNode>> type() {
        return Cast.to(HateosHandlerRouter.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    String typeNamePrefix2() {
        return "";
    }
}
